package br.ufrj.dcc.ad20141;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;

import org.apache.commons.math3.distribution.NormalDistribution;

public class Main {

	private static final int FEATURES = 58;
	private static final int INSTANCES = 4601;
	private static final int SPAM = 1813;
	private static final int NSPAM = INSTANCES - SPAM;

	/**
	 * @param args
	 */
	/**
	 * @param args
	 */
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			CSVReader reader;
			reader = new CSVReader(new FileReader("resources/datasets/spambase.data"));

			String [] nextLine;
			Double [][] spambase = new Double[INSTANCES][FEATURES];

			int lin = 0;
			while ((nextLine = reader.readNext()) != null) { 	

				for (int col=0; col<FEATURES; col++){
					spambase[lin][col] = Double.parseDouble(nextLine[col]);
				}

				//System.out.println(spambase[lin][0] + " " + spambase[lin][1] + " etc.\n");
				lin++;
			}

			reader.close();

			/*double meanEstimator = 0;
			double varianceEstimator = 0;
			int numberSpamsTrainningSet = (int)(SPAM * 0.8);

			for (int i=0; i < numberSpamsTrainningSet; i++) {
				meanEstimator += spambase[i][0];
			}
			meanEstimator /= numberSpamsTrainningSet;

			for (int i=0; i < numberSpamsTrainningSet; i++) {
				varianceEstimator += (spambase[i][0] - meanEstimator) * (spambase[i][0] - meanEstimator);
			}
			varianceEstimator /= numberSpamsTrainningSet;

			double stdDeviation = Math.sqrt(varianceEstimator);

			//NormalDistribution normal = new NormalDistribution(0, 1);
			NormalDistribution normal = new NormalDistribution(meanEstimator, stdDeviation);
			*/
			NormalDistribution[] normalSpam = new NormalDistribution[FEATURES];
			NormalDistribution[] normalNSpam = new NormalDistribution[FEATURES];
			double percentSpam = 0.5;
			double percentNSpam = 0.5;
			for(int feature = 0; feature<FEATURES; feature++){
				int numberSpamsTrainningSet = (int)(SPAM * 0.8);
				normalSpam[feature] = calculaNormal(spambase, feature, 0, numberSpamsTrainningSet);
				int numberNSpamsTrainningSet = (int)(NSPAM * 0.8);
				normalNSpam[feature] = calculaNormal(spambase, feature, SPAM, numberNSpamsTrainningSet);
			}
			Double [] percentHit = new Double[FEATURES];
			for(int feature = 0; feature<FEATURES; feature++){
				System.out.println("feature: " + feature);
				normalSpam[feature].density(0);
			    int countErrosSpam = 0;
			    for (int i=0; i < SPAM; i++) {
			    	if(normalSpam[feature].density(spambase[i][feature])*percentSpam < 
			    			normalNSpam[feature].density(spambase[i][feature])*percentNSpam){
			    		countErrosSpam++;
			    	}
				}
			    int countErrosNSpam = 0;
			    for (int i=SPAM; i < SPAM + NSPAM; i++) {
			    	if(normalSpam[feature].density(spambase[i][feature])*percentSpam > 
			    			normalNSpam[feature].density(spambase[i][feature])*percentNSpam){
			    		countErrosNSpam++;
			    	}
				}
//			    System.out.println("percentual erro nao spam: " + 1.0*countErrosNSpam/NSPAM);
//			    System.out.println("percentual erro spam: " + 1.0*countErrosSpam/SPAM);
//			    System.out.println("percentual erro total: " + 1.0*(countErrosSpam + countErrosNSpam)/INSTANCES);
			    percentHit[feature] = (1.0 - 1.0*(countErrosSpam + countErrosNSpam)/INSTANCES);
			    System.out.println("percentual acerto: " + percentHit[feature]);
			    System.out.println();
			}
			int numFeature = 10;
			for(int feature = 0; feature < FEATURES-numFeature+1; feature++){
				System.out.println("feature: " + feature);
			    int countErrosSpam = 0; 
			    for (int i=0; i < SPAM; i++) {
			    	double mulNormalSpam = percentSpam;
			    	double mulNormalNSpam = percentNSpam;
			    	for (int j = 0; j<numFeature; j++){
			    		mulNormalSpam *= normalSpam[feature].density(spambase[i][feature+j]);
			    		mulNormalNSpam *= normalNSpam[feature].density(spambase[i][feature+j]);
			    	}
			    	if(mulNormalSpam < mulNormalNSpam){
			    		countErrosSpam++;
			    	}
				}
			    int countErrosNSpam = 0; 
			    for (int i=SPAM; i < SPAM + NSPAM; i++) {
			    	int mulNormalSpam = 1;
			    	int mulNormalNSpam = 1;
			    	for (int j = 0; j<numFeature; j++){
			    		mulNormalSpam *= normalSpam[feature].density(spambase[i][feature+j]);
			    		mulNormalNSpam *= normalNSpam[feature].density(spambase[i][feature+j]);
			    	}
			    	if(mulNormalSpam > mulNormalNSpam){
			    		countErrosSpam++;
			    	}
			    	/*if(normalSpam[feature].density(spambase[i][feature])*normalSpam[feature].density(spambase[i][feature]) > 
			    	normalNSpam[feature].density(spambase[i][feature])*normalNSpam[feature].density(spambase[i][feature])){
			    		countErrosNSpam++;
			    	}*/
				}
			    System.out.println("percentual erro nao spam: " + 1.0*countErrosNSpam/NSPAM);
			    System.out.println("percentual erro spam: " + 1.0*countErrosSpam/SPAM);
			    System.out.println("percentual acerto: " + (1.0 - 1.0*(countErrosSpam + countErrosNSpam)/INSTANCES));
			    System.out.println();
			}
/*
			System.out.println("FEATURE: word_freq_make\n");

			System.out.println("===============================");
			System.out.println("TRAINNING SET: 80% OF SPAM BASE");
			System.out.println("===============================\n");
			System.out.println("SPAMS OF TRAINNING SET: 1 a " + numberSpamsTrainningSet + "\n");

			System.out.println("Média:         " + normalSpam.getMean());
			System.out.println("Variância:     " + normalSpam.getStandardDeviation()*normalSpam.getStandardDeviation());//varianceEstimator);
			System.out.println("Desvio Padrão: " + normalSpam.getStandardDeviation() + "\n");

			System.out.println("===============================================");
			System.out.println("TESTING SET: PDF EVALUATED FOR 20% OF SPAM BASE");
			System.out.println("===============================================\n");
			System.out.println("SPAMS OF TESTING SET: " + (numberSpamsTrainningSet+1) + " a " + SPAM + "\n");

			//for (int i=numberSpamsTrainningSet; i < SPAM; i++) {
			//	System.out.println("Email " + (i+1) + ": " + normal.density(spambase[i][0]));
			//}
*/

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static NormalDistribution calculaNormal(Double [][] spambase, int feature, int inicial, int numberTrainningSet){
		double meanEstimator = 0;
		double varianceEstimator = 0;
		//int numberSpamsTrainningSet = (int)(SPAM * 0.8);

		for (int i=inicial; i < numberTrainningSet; i++) {
			meanEstimator += spambase[i][feature];
		}
		meanEstimator /= numberTrainningSet;

		for (int i=inicial; i < numberTrainningSet; i++) {
			varianceEstimator += (spambase[i][feature] - meanEstimator) * (spambase[i][feature] - meanEstimator);
		}
		varianceEstimator /= numberTrainningSet;

		double stdDeviation = Math.sqrt(varianceEstimator);

		//NormalDistribution normal = new NormalDistribution(0, 1);
		if(stdDeviation == 0) stdDeviation += 0.0001;
		return new NormalDistribution(meanEstimator, stdDeviation);	
	}
}
