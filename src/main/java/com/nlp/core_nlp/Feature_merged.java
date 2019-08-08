package com.nlp.core_nlp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.nlp.util.FileHelper;
import com.nlp.wordnet.WordNetService;
import com.nlp.util.ClassificationCoreLabel;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;

public class Feature_merged {

	static String line = null;
	static FileHelper fileHelper = new FileHelper();
	static Date date = new Date();
	static String content = "SUBJECT|SUB_NER|SCALAR_TYPE|TAG|VERB|VCAT|TAG|OBJECT|OBJ_NER|SCALAR_TYPE|TAG|LABEL \n";
	static String contentForAutoWeka = "S-NER, S-SCALAR, S-TAG, V-CAT, V-TAG, O-NER, O-SCALAR, O-TAG, LABEL\n";
	static Map<String, String> vcat = new HashMap<String, String>();

	public static final String PROPERTIESLIST = "tokenize,ssplit,pos,lemma,depparse,natlog,openie,ner";

	ClassLoader classLoader = getClass().getClassLoader();
	private File folder = null;

	public Feature_merged(String folderName) throws IOException {

		this.folder = new File(classLoader.getResource(folderName).getFile());
		if (this.folder == null) {
			throw new FileNotFoundException("Folder " + folderName + " does not exist.");
		}
	}

	public static void listFilesForFolder(final File folder) throws IOException {

		ClassificationCoreLabel classificationCoreLabel;
		ArrayList<ClassificationCoreLabel> listOfClassificationPerWord;

		String[] oieSubjectArray;
		String[] oieObjectArray;
		String[] oieRelationArray;

		for (final File file : folder.listFiles()) {

			if (file.isDirectory()) {
				listFilesForFolder(file);
			} else {

				BufferedReader fileContent = null;
				try {

					String filename = getFileWithRelativePath(folder, file);

					fileContent = new BufferedReader(new FileReader(new File(filename)));

					String tempLine;
					int sentenceNo = 0;
					while ((tempLine = fileContent.readLine()) != null) {

						line = tempLine;

						// Create the Stanford CoreNLP pipeline
						Properties properties = PropertiesUtils.asProperties("annotators", PROPERTIESLIST);
						StanfordCoreNLP pipeline = new StanfordCoreNLP(properties);

						// Annotate an example document.
						Annotation annotation = new Annotation(line);
						pipeline.annotate(annotation);

						// Loop over sentences in the document

						for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
							System.out.println(
									"\n############################## PROCESSING NEXT SENTENCE #########################\n");
							System.out.println("\nSentence is #" + ++sentenceNo + ": "
									+ sentence.get(CoreAnnotations.TextAnnotation.class) + "\n");

							listOfClassificationPerWord = new ArrayList<ClassificationCoreLabel>();

							for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
								String word = token.get(CoreAnnotations.TextAnnotation.class);
								String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
								String ner = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);

								classificationCoreLabel = new ClassificationCoreLabel(word, pos, ner);
								listOfClassificationPerWord.add(classificationCoreLabel);

							}
							// Get the OpenIE triples for the sentence
							Collection<RelationTriple> triples = sentence
									.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);

							// Find the largest of all the triples
							RelationTriple triple = getLongestTriple(triples);
							if (triple == null) {
								continue;
							}
							// Print the triples
							System.out.println(triple.confidence + "\t" + triple.subjectLemmaGloss() + "\t"
									+ triple.relationGloss() + "\t" + triple.objectLemmaGloss());

							oieSubjectArray = triple.subjectGloss().split(" ");
							oieObjectArray = triple.objectGloss().split(" ");
							oieRelationArray = triple.relationGloss().split(" ");
							String subjectPos = null;
							String objectPos = null;
							String relationPos = null;

							String subjectNer = null;
							String objectNer = null;

							for (ClassificationCoreLabel ccl : listOfClassificationPerWord) {
								if (ccl.getWord().equals(oieSubjectArray[oieSubjectArray.length - 1])) {
									subjectPos = ccl.getPos();
									subjectNer = ccl.getNer();
								}
								if (ccl.getWord().equals(oieObjectArray[oieObjectArray.length - 1])) {
									objectPos = ccl.getPos();
									objectNer = ccl.getNer();
								}
								if (ccl.getWord().equals(oieRelationArray[oieRelationArray.length - 1])) {
									relationPos = ccl.getPos();
								}
							}

							content = content + triple.subjectGloss() + "|" + subjectNer + "|"
							+ ((WordNetService.getInstance().isScalar(triple.subjectGloss()))?1:0) +"|"
									+ subjectPos + "|" +
									triple.relationGloss() + "|" + getvcat(triple.relationGloss()) + 
									"|" + relationPos
									+ "|" + triple.objectGloss() + "|" + objectNer 
									+ "|" + ((WordNetService.getInstance().isScalar(triple.objectGloss()))?1:0) + "|" + objectPos +  "\n";

							contentForAutoWeka = contentForAutoWeka + "'" + subjectNer + "','" + 
									((WordNetService.getInstance().isScalar(triple.subjectGloss()))?1:0) + "','"
									+ subjectPos + "','" + getvcat(triple.relationGloss()) + "','" + relationPos + "','"
									+ objectNer + "','" + ((WordNetService.getInstance().isScalar(triple.objectGloss()))?1:0) +
									"','" + objectPos + "',\n";
						}
					}
					writeToFeatureVector(content, "featureVector");
					writeToFeatureVectorForAutoWeka(contentForAutoWeka, "featureVectorForAutoWeka");

				} catch (Exception e) {
					System.out.println("Program encountered an error while processing the file : " + e);
				} finally {
					if (fileContent != null)
						fileContent.close();
				}
			}
		}
	}

	private static RelationTriple getLongestTriple(Collection<RelationTriple> triples) {
		int count = 0;
		int value = 0;
		RelationTriple resultTriple = null;
		Map<RelationTriple, Integer> tripleCountMap = new HashMap<RelationTriple, Integer>();

		for (RelationTriple triple : triples) {
			count += triple.subjectGloss().split(" ").length;
			count += triple.objectGloss().split(" ").length;
			count += triple.relationGloss().split(" ").length;
			tripleCountMap.put(triple, count);
			count = 0;
		}

		for (Map.Entry<RelationTriple, Integer> entry : tripleCountMap.entrySet()) {
			if (value < entry.getValue()) {
				value = entry.getValue();
				resultTriple = entry.getKey();
			}
		}

		return resultTriple;
	}

	private static void writeToFeatureVector(String content, String filename) {
		DateTime dt = new DateTime(date);
		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd-HH-mm");
		String outputFilename = "\\" + filename + "" + dt.toString(dtf) + ".csv";

		fileHelper.saveToFile(content, "result", outputFilename, "UTF-8");
	}

	private static void writeToFeatureVectorForAutoWeka(String content, String filename) {
		DateTime dt = new DateTime(date);
		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd-HH-mm");
		String outputFilename = "\\" + filename + "" + dt.toString(dtf) + ".csv";

		fileHelper.saveToFile(content, "result", outputFilename, "UTF-8");
	}

	private static String getFileWithRelativePath(final File folder, final File file) {
		return folder + "\\" + file.getName();
	}

	public static void main(String[] args) throws IOException {
		addVerbCategories();
		listFilesForFolder(new Feature_merged("test").folder);
	}

	private static void addVerbCategories() {
		vcat.put("has", "possession");
		vcat.put("have", "possession");
		vcat.put("had", "possession");
		vcat.put("possess", "possession");
		vcat.put("consist of", "comprised of");
		vcat.put("comprised of", "comprised of");
		vcat.put("constituent of", "comprised of");
		vcat.put("compose", "consist");
		vcat.put("form", "consist");
		vcat.put("composed", "consist");
		vcat.put("formed", "consist");
		vcat.put("consist", "consist");
		vcat.put("encompass", "consist");
		vcat.put("embrace", "consist");
		vcat.put("constituted", "consist");
		vcat.put("comprised", "consist");
		vcat.put("constitute", "consist");
		vcat.put("comprise", "consist");
		vcat.put("make-up", "consist");
		vcat.put("made-up-4", "consist");
		vcat.put("has", "containment");
		vcat.put("has", "containment");
		vcat.put("is", "IS-A");
		vcat.put("was", "IS-A");
		vcat.put("are", "IS-A");
		vcat.put("were", "IS-A");
		vcat.put("am", "IS-A");
		vcat.put("regarded as", "IS-A");
		vcat.put("be", "IS-A");
		vcat.put("been", "IS-A");
	}

	private static String getvcat(String verb) {
		return (vcat.get(verb) == null) ? "Other" : vcat.get(verb);
	}
}
