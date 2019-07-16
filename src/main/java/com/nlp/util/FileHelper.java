package com.nlp.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.opencsv.CSVWriter;

public class FileHelper extends BaseHelper {
	
	public void saveToFile(String content, String baseDir, String fileName, String encoding) {

		verifyFileNameHasValue(fileName, "File name is null");
		if (content == null) {
			logErrorAndThrowException("content is null");
		}

		//File file = makeAbsoluteFile(baseDir, fileName);
		FileWriter file = makeAbsoluteFileWriter(baseDir, fileName);
		//verifyFileExists(file);
		
		List<String[]> data = new ArrayList<String[]>(); 

		CSVWriter writer = new CSVWriter(file, '|', 
                CSVWriter.NO_QUOTE_CHARACTER, 
                CSVWriter.DEFAULT_ESCAPE_CHARACTER, 
                CSVWriter.DEFAULT_LINE_END); 
		
		String[] arrayContent = content.split("\n");

        for (int i = 0; i < arrayContent.length; i++) { 
            String row = arrayContent[i]; 
            String[] rowdata = row.split("\\|"); 
            data.add(rowdata); 
        } 
        
		try {
			//FileUtils.writeStringToFile(file, content, encoding, true);
			writer.writeAll(data); 
			  
            // closing writer connection 
            writer.close(); 
		} catch (IOException|UnsupportedCharsetException e) {
			logErrorAndThrowException("Couldn't save input text to file: ", e);
		}
	}	
	
	public void verifyFileExists(File file) {
		
		if (! file.isFile()) {
			logErrorAndThrowException("File does not exist: " + file.getPath()); // + " (Absolutt sti: " + file.getAbsolutePath() + ")");
		}
	}

	public void verifyFileNameHasValue(String fileName, String errorMsg) {
		
		if (StringUtils.isBlank(fileName)) {
			logErrorAndThrowException(errorMsg);
		}
	}
	
	public FileWriter makeAbsoluteFileWriter(String baseDir, String fileName) {
		
		File absBaseDir = makeAbsoluteFile(baseDir);
		File file = new File(absBaseDir+fileName);
		
		/*try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		FileWriter outputfile = null;
		try {
			outputfile = new FileWriter(file, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return outputfile;
		
	}
	
	public File makeAbsoluteFile(String baseDir, String fileName) {
		
		File absBaseDir = makeAbsoluteFile(baseDir);
		File file = new File(absBaseDir+fileName);
		
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return file;
		
	}

	public File makeAbsoluteFile(String folderName ) {
		
		if (folderName == null) {
			folderName = "";
		}
		
		Path currentRelativePath = Paths.get("");
		String absolutePath = currentRelativePath.toAbsolutePath().toString();
		System.out.println("Current relative path is: " + absolutePath);
		
		final File outputRootDir = new File(absolutePath+"/resources/"+folderName);
		return outputRootDir;
	}

}
