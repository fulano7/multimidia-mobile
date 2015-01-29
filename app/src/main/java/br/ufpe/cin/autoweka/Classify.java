package br.ufpe.cin.autoweka;

import java.io.StringReader;

import weka.attributeSelection.AttributeSelection;
import weka.classifiers.Classifier;
import weka.core.Instances;

public class Classify {	
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
	
	public static void main(String[] args){
		try {
			String s = classifySingleUnlabeledInstance(new MusicInstance(80.207, 0.49482558127419307, 0.031195728086989782, 0.09090272012346592, 0.33858609898708314), "trained.0.model", "trained.0.attributeselection");
			System.out.println(s);
			String s2 = classifySingleUnlabeledInstance(new MusicInstance(140.008, 0.436097823900063, 0.03222021207000658, 0.1057052477494161, 0.6061255397822767), "trained.0.model", "trained.0.attributeselection");
			System.out.println(s2);
		} catch (Exception e) {
			e.printStackTrace();
		}
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