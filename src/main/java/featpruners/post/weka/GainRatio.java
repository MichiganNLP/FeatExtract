package featpruners.post.weka;

import java.io.File;

import util.Converter;
import util.VectorSaver;
import weka.attributeSelection.GainRatioAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import cc.mallet.types.InstanceList;
import featpruners.post.PostFeaturePruner;

public class GainRatio extends PostFeaturePruner{

	
	
	public GainRatio(Double threshold) {	super(threshold);}
	
	
	public GainRatio(Integer n) {		super(n);	}

	public void prune(String intputFile, String outputFile) {
		InstanceList instances = Converter.loadMalletInstances(new File(intputFile));
		Instances wekaInstances = Converter.convert2WekaInstances(instances);
		AttributeSelection attributeSelection = new AttributeSelection();
		Ranker ranker = new Ranker();
		ranker.setNumToSelect(N);

		GainRatioAttributeEval infoGainAttributeEval = new GainRatioAttributeEval();
		attributeSelection.setEvaluator(infoGainAttributeEval);
		attributeSelection.setSearch(ranker);

		try {
			attributeSelection.setInputFormat(wekaInstances);
			Instances newInsts = Filter.useFilter(wekaInstances,
					attributeSelection);
			VectorSaver.saveWekaVectorsAsSVMFull(newInsts,
					outputFile);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public static void main(String[] args) {
		GainRatio ig = new GainRatio(500);
		ig.prune("/home/jmaxk/temp/test.vecs", "/home/jmaxk/temp/test-pruned.vecs");
	}
}