package br.ufrj.dcc.ad20141;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;

public class SpamClassifier {

	private String datasetPath;
	private int nInstances;
	private int nFeatures;
	private int nSpams;
	private int nNotSpams;
	
	private Double[][] spamTrainingSet;
	private Double[][] spamTestingSet;
	private Double[][] notSpamTrainingSet;
	private Double[][] notSpamTestingSet;

	
	public SpamClassifier(String datasetPath, int nInstances, int nFeatures, int nSpam, int nNotSpam) {
		this.datasetPath = datasetPath;
		this.nInstances = nInstances;
		this.nFeatures = nFeatures;
		this.nSpams = nSpam;
		this.nNotSpams = nNotSpam;
	}

	
	public void readDatasetSplitting(double trainingPercentage) {
		Double[][] dataset = readEntireDataset();
		
		//SPAMS DATASETS:
		int nSpamsTrainingSet = (int)(nSpams * trainingPercentage);
		spamTrainingSet = new Double[nSpamsTrainingSet][nFeatures];
		
		int nSpamsTestingSet = nSpams - nSpamsTrainingSet;
		spamTestingSet = new Double[nSpamsTestingSet][nFeatures];
		
		//NOT SPAMS DATASETS:
		int nNotSpamsTrainingSet = (int)(nNotSpams * trainingPercentage);
		notSpamTrainingSet = new Double[nNotSpamsTrainingSet][nFeatures];
		
		int nNotSpamsTestingSet = nNotSpams - nNotSpamsTrainingSet;
		notSpamTestingSet = new Double[nNotSpamsTestingSet][nFeatures];
				
		
		int indexSpamTraining=0, indexSpamTesting=0, indexNotSpamTraining=0, indexNotSpamTesting=0;
		for (int i=0; i < nInstances; i++) {
			for (int j=0; j < nFeatures; j++) {
				
				if (i < nSpams){
					if (i < nSpamsTrainingSet) {
						spamTrainingSet[indexSpamTraining][j] = dataset[i][j];
						indexSpamTraining++;
					}else {
						spamTestingSet[indexSpamTesting][j] = dataset[i][j];
						indexSpamTesting++;
					}
				}else if (i >= nSpams) {
					if (i < nSpams + nNotSpamsTrainingSet) {
						notSpamTrainingSet[indexNotSpamTraining][j] = dataset[i][j];
						indexNotSpamTraining++;
					}else {
						notSpamTestingSet[indexNotSpamTesting][j] = dataset[i][j];
						indexNotSpamTesting++;
					}
					
				}
			}
		}
	}

	
	private Double[][] readEntireDataset() {
		Double[][] dataset = new Double[nInstances][nFeatures];

		try{
			CSVReader reader;
			reader = new CSVReader(new FileReader(datasetPath));

			String [] nextLine;

			int lin = 0;
			while ((nextLine = reader.readNext()) != null) { 	
				for (int col=0; col < nFeatures; col++){
					dataset[lin][col] = Double.parseDouble(nextLine[col]);
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

		return dataset;
	}

	
	public void train() {
		// TODO Auto-generated method stub
	}

}
