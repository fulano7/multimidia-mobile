package br.ufpe.cin.autoweka;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import weka.attributeSelection.AttributeSelection;
import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.classifiers.Evaluation;

import weka.core.converters.ConverterUtils.DataSource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;


public class Classify {

    public static void classifyInstances(String attributeSelectionObjPath, String modelObjPath, String instancesPath) throws Exception{
    DataSource dataSource = new DataSource(new FileInputStream(instancesPath));
    Instances instances = dataSource.getDataSet();

    // O ultimo atributo eh o q queremos inferir
    instances.setClassIndex(instances.numAttributes() - 1);

    //Carregar a classe de selecao de atributos, se necessario
    if(attributeSelectionObjPath != null){
        AttributeSelection as = (AttributeSelection)weka.core.SerializationHelper.read(attributeSelectionObjPath);
        instances = as.reduceDimensionality(instances);
    }

    //Carregar classificador
    Classifier classifier = (Classifier)weka.core.SerializationHelper.read(modelObjPath);

    //Fazer a avaliacao
    Evaluation eval = new Evaluation(instances);

    // terceiro parametro eh obrigatorio, por qneuanto nao vamos usa-lo
    Object[] nul = new Object[0];

    eval.evaluateModel(classifier, instances, nul);

}




    public static String classifySingleUnlabeledInstance(MusicInstance mi, String modelObjPath, String attributeSelectionObjPath) throws Exception{
		Classifier classifier = (Classifier)weka.core.SerializationHelper.read(modelObjPath);
		
		Instances instances = new Instances(new StringReader(mi.toArffSingleInstanceFileValenceString()));
		// set class attribute
		instances.setClassIndex(instances.numAttributes() - 1);

		// performing attribute selection
		AttributeSelection as = (AttributeSelection) weka.core.SerializationHelper.read(attributeSelectionObjPath);
		
		instances = as.reduceDimensionality(instances);
		
		// label this single instance
		// clsLabel is 0.0 or 1.0
		double classLabel = classifier.classifyInstance(instances.instance(0));
		
		instances.instance(0).setClassValue(classLabel);
		
		String classification = instances.instance(0).classAttribute().value((int) classLabel);
		
		return classification;
	}

    //(String attributeSelectionObjPath, String modelObjPath, String instancesPath)
	public void run(Context ctx) throws IOException {
        String path = "file:///android_asset/ativacao.arff";

        boolean exists = new File(path).exists();

        System.out.print(exists);
    }
}

class MusicInstance{
	private double tempo, dance, speech, liveness, energy;
	private String valenceClass, activationClass;
	
	public MusicInstance(double tempo, double dance, double speech, double liveness, double energy){
		this.dance = dance;
		this.tempo = tempo;
		this.energy = energy;
		this.speech = speech;
		this.liveness = liveness;
		this.valenceClass = this.activationClass = "?";
	}
	
	public MusicInstance(double tempo, double dance, double speech, double liveness, double energy, String valenceClass, String activationClass){
		this(tempo, dance, speech, liveness, energy);
		
		this.valenceClass = valenceClass;
		this.activationClass = activationClass;
	}
	
	public String toArffSingleInstanceFileValenceString(){
		return "@relation whatever\n\n"+
				"@attribute Tempo numeric\n"+
				"@attribute Dance numeric\n"+
				"@attribute Speech numeric\n"+
				"@attribute Liveness numeric\n"+
				"@attribute Energy numeric\n"+
				"@attribute Valencia { triste , alegre } \n\n\n"+
				"@data\n"+
				this.toArffValenceString();
	}
	
	public String toArffValenceString(){
		return (this.tempo+" , "+this.dance+" , "+this.speech+" , "+this.liveness+" , "+this.energy+" , "+this.valenceClass+" \n");
	}
	
	// TODO :{ estes metodos
	/*public String toArffSingleInstanceFileActivationString(){
		return null;
	}
	
	public String toArffActivationString(){
		return null;
	}*/
	// }// fim - TODO
	
	public void setValenceClass(String valenceClass){
		this.valenceClass = valenceClass;
	}
	
	public void setActivationClass(String activationClass){
		this.activationClass = activationClass;
	}
	
	public String getValenceClass(String valenceClass){
		return this.valenceClass;
	}
	
	public String getActivationClass(String activationClass){
		return this.activationClass;
	}
}