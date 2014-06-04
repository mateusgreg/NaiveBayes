package br.ufrj.dcc.ad20141;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;

import org.apache.commons.math3.distribution.NormalDistribution;

public class Main {

	private static final int FEATURES = 58;
	private static final int INTANCES = 4601;
	private static final int SPAM = 1813;
	private static final int NSPAM = INTANCES - SPAM;

	public static void main(String[] args) {

		try {
			CSVReader reader;
			reader = new CSVReader(new FileReader("resources/datasets/spambase.data"));

			String [] nextLine;
			Double [][] spambase = new Double[INTANCES][FEATURES];

			int lin = 0;
			while ((nextLine = reader.readNext()) != null) { 	

				for (int col=0; col<FEATURES; col++){
					spambase[lin][col] = Double.parseDouble(nextLine[col]);
				}

				//System.out.println(spambase[lin][0] + " " + spambase[lin][1] + " etc.\n");
				lin++;
			}

			reader.close();

			
			double meanEstimator = 0;
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
			NormalDistribution normal = new NormalDistribution(meanEstimator, stdDeviation*2);

			System.out.println("FEATURE: word_freq_make\n");

			System.out.println("===============================");
			System.out.println("TRAINNING SET: 80% OF SPAM BASE");
			System.out.println("===============================\n");
			System.out.println("SPAMS OF TRAINNING SET: 1 a " + numberSpamsTrainningSet + "\n");

			System.out.println("Média:         " + normal.getMean());
			System.out.println("Variância:     " + varianceEstimator);
			System.out.println("Desvio Padrão: " + normal.getStandardDeviation() + "\n");

			System.out.println("===============================================");
			System.out.println("TESTING SET: PDF EVALUATED FOR 20% OF SPAM BASE");
			System.out.println("===============================================\n");
			System.out.println("SPAMS OF TESTING SET: " + (numberSpamsTrainningSet+1) + " a " + SPAM + "\n");

			for (int i=numberSpamsTrainningSet; i < SPAM; i++) {
				System.out.println("Email " + (i+1) + ": " + normal.density(spambase[i][0]));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
