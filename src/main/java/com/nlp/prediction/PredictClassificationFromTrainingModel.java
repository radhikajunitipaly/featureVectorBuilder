package com.nlp.prediction;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class PredictClassificationFromTrainingModel {
	public static void main(String[] args) throws Exception {
		
		 // Read all the instances in the file (ARFF, CSV, XRFF, ...)
		 DataSource source = new DataSource("");
		 Instances instances = source.getDataSet();
		 
		 // Make the last attribute be the class
		 instances.setClassIndex(instances.numAttributes() - 1);
		 
		 // Print header and instances.
		 System.out.println("\nDataset:\n");
		 System.out.println(instances);
		 
		 
	}
}
