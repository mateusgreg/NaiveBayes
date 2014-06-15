package br.ufrj.dcc.ad20141;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;

import org.apache.commons.math3.distribution.NormalDistribution;

public class Main {

	private static final int FEATURES = 57;
	private static final int INSTANCES = 4601;
	private static final int SPAM = 1813;
	private static final int NSPAM = INSTANCES - SPAM;
	private static final double DELTA = 0.1;
	private static final double percentSpam = 1.0*SPAM/INSTANCES;
	private static final double percentNSpam = 1.0*NSPAM/INSTANCES;


	public static void main(String[] args) {

		Double[][] spambase = readDataset("resources/datasets/spambase.data");
		
		NormalDistribution[] normalSpam = new NormalDistribution[FEATURES];
		NormalDistribution[] normalNSpam = new NormalDistribution[FEATURES];
		
		for(int feature = 0; feature<FEATURES; feature++){
			int numberSpamsTrainningSet = (int)(SPAM * 0.8);
			normalSpam[feature] = calculaNormal(spambase, feature, 0, numberSpamsTrainningSet);
			
			int numberNSpamsTrainningSet = (int)(NSPAM * 0.8);
			normalNSpam[feature] = calculaNormal(spambase, feature, SPAM, SPAM + numberNSpamsTrainningSet);
		}


		Double [] percentHit = new Double[FEATURES];
		for(int feature = 0; feature<FEATURES; feature++){
			System.out.println("feature: " + feature);
			int countErrosSpam = 0;
			for (int i=0; i < SPAM; i++) {
				if(normalSpam[feature].probability(spambase[i][feature] - DELTA, spambase[i][feature] + DELTA)*percentSpam < 
						normalNSpam[feature].probability(spambase[i][feature] - DELTA, spambase[i][feature] + DELTA)*percentNSpam){
					countErrosSpam++;
				}
			}
			int countErrosNSpam = 0;
			for (int i=SPAM; i < SPAM + NSPAM; i++) {
				if(normalSpam[feature].probability(spambase[i][feature] - DELTA, spambase[i][feature] + DELTA)*percentSpam > 
				normalNSpam[feature].probability(spambase[i][feature] - DELTA, spambase[i][feature] + DELTA)*percentNSpam){
					countErrosNSpam++;
				}
			}
			percentHit[feature] = (1.0 - 1.0*(countErrosSpam + countErrosNSpam)/INSTANCES);
			System.out.println("percentual acerto spam: " + (1.0 - 1.0*countErrosSpam/SPAM));
			System.out.println("percentual acerto nSpam: " + (1.0 - 1.0*countErrosNSpam/NSPAM));
			System.out.println("percentual acerto: " + percentHit[feature]);
			System.out.println();
		}
		System.out.println();
		System.out.println();


		int countErrosSpam = 0;
		int[] features = {2,4,6,15,20,22,23,51,52,55};
		System.out.println("features: {2,4,6,15,20,22,23,51,52,55}");
		for (int i=0; i < SPAM; i++) {
			double sumNormalSpam = Math.log(percentSpam);
			double sumNormalNSpam = Math.log(percentNSpam);
			for(int j = 0; j < features.length; j++){
				sumNormalSpam += Math.log(normalSpam[features[j]].probability(spambase[i][features[j]] - DELTA, spambase[i][features[j]] + DELTA));
				sumNormalNSpam += Math.log(normalNSpam[features[j]].probability(spambase[i][features[j]] - DELTA, spambase[i][features[j]] + DELTA));
			}
			if(sumNormalSpam <= sumNormalNSpam){
				countErrosSpam++;
			}
		}
		int countErrosNSpam = 0;
		for (int i=SPAM; i < SPAM + NSPAM; i++) {
			double sumNormalSpam = Math.log(percentSpam);
			double sumNormalNSpam = Math.log(percentNSpam);
			for(int j = 0; j < features.length; j++){
				sumNormalSpam += Math.log(normalSpam[features[j]].probability(spambase[i][features[j]] - DELTA, spambase[i][features[j]] + DELTA));
				sumNormalNSpam += Math.log(normalNSpam[features[j]].probability(spambase[i][features[j]] - DELTA, spambase[i][features[j]] + DELTA));
			}
			if(sumNormalSpam >= sumNormalNSpam){
				countErrosNSpam++;
			}
		}
		System.out.println("percentual acerto spam: " + (1.0 - 1.0*countErrosSpam/SPAM));
		System.out.println("percentual acerto nSpam: " + (1.0 - 1.0*countErrosNSpam/NSPAM));
		System.out.println("percentual acerto: " + (1.0 - 1.0*(countErrosSpam + countErrosNSpam)/INSTANCES));
		System.out.println();
		System.out.println();


		countErrosSpam = 0;
		System.out.println("Todas features.");
		for (int i=0; i < SPAM; i++) {
			double sumNormalSpam = Math.log(percentSpam);
			double sumNormalNSpam = Math.log(percentNSpam);
			for(int feature = 0; feature < FEATURES; feature++){
				sumNormalSpam += Math.log(normalSpam[feature].probability(spambase[i][feature] - DELTA, spambase[i][feature] + DELTA));
				sumNormalNSpam += Math.log(normalNSpam[feature].probability(spambase[i][feature] - DELTA, spambase[i][feature] + DELTA));
			}
			if(sumNormalSpam <= sumNormalNSpam){
				countErrosSpam++;
			}
		}
		countErrosNSpam = 0;
		for (int i=SPAM; i < SPAM + NSPAM; i++) {
			double sumNormalSpam = Math.log(percentSpam);
			double sumNormalNSpam = Math.log(percentNSpam);
			for(int feature = 0; feature < FEATURES; feature++){
				sumNormalSpam += Math.log(normalSpam[feature].probability(spambase[i][feature] - DELTA, spambase[i][feature] + DELTA));
				sumNormalNSpam += Math.log(normalNSpam[feature].probability(spambase[i][feature] - DELTA, spambase[i][feature] + DELTA));
			}
			if(sumNormalSpam >= sumNormalNSpam){
				countErrosNSpam++;
			}
		}
		System.out.println("percentual acerto spam: " + (1.0 - 1.0*countErrosSpam/SPAM));
		System.out.println("percentual acerto nSpam: " + (1.0 - 1.0*countErrosNSpam/NSPAM));
		System.out.println("percentual acerto: " + (1.0 - 1.0*(countErrosSpam + countErrosNSpam)/INSTANCES));
	}

	public static Double[][] readDataset(String datasetPath) {
		Double [][] spambase = new Double[INSTANCES][FEATURES];
		
		try{
			CSVReader reader;
			reader = new CSVReader(new FileReader(datasetPath));

			String [] nextLine;

			int lin = 0;
			while ((nextLine = reader.readNext()) != null) { 	
				for (int col=0; col<FEATURES; col++){
					spambase[lin][col] = Double.parseDouble(nextLine[col]);
				}
				lin++;
			}

			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return spambase;
	}
	
	public static NormalDistribution calculaNormal(Double [][] spambase, int feature, int inicial, int numberTrainningSet){
		double meanEstimator = 0;
		double varianceEstimator = 0;

		for (int i=inicial; i < numberTrainningSet; i++) {
			meanEstimator += spambase[i][feature];
		}
		meanEstimator /= numberTrainningSet;

		for (int i=inicial; i < numberTrainningSet; i++) {
			varianceEstimator += (spambase[i][feature] - meanEstimator) * (spambase[i][feature] - meanEstimator);
		}
		varianceEstimator /= numberTrainningSet;

		double stdDeviation = Math.sqrt(varianceEstimator);

		if(stdDeviation == 0) stdDeviation += 0.0001;
		return new NormalDistribution(meanEstimator, stdDeviation);	
	}
}
