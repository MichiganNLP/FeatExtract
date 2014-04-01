package featpruners.post.weka;

import java.io.File;

import util.Converter;
import util.VectorSaver;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import cc.mallet.types.InstanceList;
import featpruners.post.PostFeaturePruner;

public class InformationGain extends PostFeaturePruner {

	private boolean binary;

	public InformationGain(Integer N, boolean binary) {
		super(N);
		this.binary = binary;
	}

	public InformationGain(Double threshold, boolean binary) {
		super(threshold);
		this.binary = binary;

	}

	public void prune(String intputFile, String outputFile) {
		InstanceList instances = Converter.loadMalletInstances(new File(
				intputFile));
		Instances wekaInstances = Converter.convert2WekaInstances(instances);
		AttributeSelection attributeSelection = new AttributeSelection();
		Ranker ranker = new Ranker();
		ranker.setNumToSelect(N);

		InfoGainAttributeEval infoGainAttributeEval = new InfoGainAttributeEval();
		infoGainAttributeEval.setBinarizeNumericAttributes(binary);
		attributeSelection.setEvaluator(infoGainAttributeEval);
		attributeSelection.setSearch(ranker);

		try {
			attributeSelection.setInputFormat(wekaInstances);
			Instances newInsts = Filter.useFilter(wekaInstances,
					attributeSelection);
			VectorSaver.saveWekaVectorsAsSVMFull(newInsts, outputFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println("ig");
		InformationGain ig = new InformationGain(1000, false);
		ig.prune("/home/jmaxk/temp/test.vecs",
				"/home/jmaxk/temp/test-pruned.vecs");
		System.out.println("gr");
		GainRatio ig2 = new GainRatio(500);
		ig2.prune("/home/jmaxk/temp/test-pruned.vecs",
				"/home/jmaxk/temp/test-pruned2.vecs");

	}

}
