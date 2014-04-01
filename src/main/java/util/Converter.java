package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.core.converters.SVMLightLoader;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.SvmLight2FeatureVectorAndLabel;
import cc.mallet.pipe.iterator.SelectiveFileLineIterator;
import cc.mallet.types.Alphabet;
import cc.mallet.types.FeatureVector;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;

public class Converter {

	public static String convert2ARFF(InstanceList instances, String description) {
		Alphabet dataAlphabet = instances.getDataAlphabet();
		Alphabet targetAlphabet = instances.getTargetAlphabet();
		StringBuilder sb = new StringBuilder();
		sb.append("@Relation \"").append(description).append("\"\n\n");
		int size = dataAlphabet.size();
		for (int i = 0; i < size; i++) {
			sb.append("@attribute \"")
					.append(dataAlphabet.lookupObject(i).toString()
							.replaceAll("\\s+", "_"));
			sb.append("\" numeric\n");
		}
		sb.append("@attribute target {");
		for (int i = 0; i < targetAlphabet.size(); i++) {
			if (i != 0)
				sb.append(",");
			sb.append(targetAlphabet.lookupObject(i).toString()
					.replace(",", ";"));
		}
		sb.append("}\n\n@data\n");
		for (int i = 0; i < instances.size(); i++) {
			Instance instance = instances.get(i);
			sb.append("{");
			FeatureVector fv = (FeatureVector) instance.getData();
			int[] indices = fv.getIndices();
			double[] values = fv.getValues();
			boolean[] attrFlag = new boolean[size];
			double[] attrValue = new double[size];
			for (int j = 0; j < indices.length; j++) {
				attrFlag[indices[j]] = true;
				attrValue[indices[j]] = values[j];
			}
			for (int j = 0; j < attrFlag.length; j++) {
				if (attrFlag[j]) {
					// sb.append(j).append(" 1, ");
					sb.append(j).append(" ").append(attrValue[j]).append(", ");
				}
			}
			sb.append(attrFlag.length).append(" ")
					.append(instance.getTarget().toString().replace(",", ";"));
			sb.append("}\n");
		}
		return sb.toString();
	}

	/**
	 * Converts Mallet InstanceList into Weka Instances
	 * 
	 * @param instanceList
	 * @return
	 * @throws IOException
	 */
	public static Instances convert2WekaInstances(InstanceList instanceList) {
		try {

			String arff = convert2ARFF(instanceList, "DESC");
			StringReader reader = new StringReader(arff);
			Instances instances = new Instances(reader);
			instances.setClassIndex(instances.numAttributes() - 1);
			return instances;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static InstanceList loadMalletInstances(File inputFile) {
		try {
			Pipe instancePipe;

			// Build a new pipe
			ArrayList<Pipe> pipeList = new ArrayList<Pipe>();
			pipeList.add(new SvmLight2FeatureVectorAndLabel());
			instancePipe = new SerialPipes(pipeList);

			InstanceList instances = new InstanceList(instancePipe);
			Reader fileReader = new InputStreamReader(new FileInputStream(
					inputFile));
			// Read instances from the file
			instances.addThruPipe(new SelectiveFileLineIterator(fileReader,
					"^\\s*#.+"));
			return instances;

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public static void main(String[] args) {
		String malletFile = "/home/jmaxk/temp/test.vecs";
		InstanceList instances = loadMalletInstances(new File(malletFile));
		Instances wekaInstances = convert2WekaInstances(instances);
		VectorSaver.saveWekaVectorsAsSVMLight(wekaInstances,
				"/home/jmaxk/temp/vecs2.weka");

		int n = 400; // number of features to select
		AttributeSelection attributeSelection = new AttributeSelection();
		Ranker ranker = new Ranker();
		ranker.setNumToSelect(n);

		InfoGainAttributeEval infoGainAttributeEval = new InfoGainAttributeEval();
		attributeSelection.setEvaluator(infoGainAttributeEval);
		attributeSelection.setSearch(ranker);

		try {
			attributeSelection.setInputFormat(wekaInstances);
			Instances newInsts = Filter.useFilter(wekaInstances,
					attributeSelection);
			VectorSaver.saveWekaVectorsAsSVMLight(newInsts,
					"/home/jmaxk/temp/vecs.weka");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	//
//	public static void main(String[] args) {
//		// List<FeatureVectors> vecs = VectorLoader
//		// .fromMallet("/home/jmaxk/temp/DE.vecs");
//		// VectorSaver.saveWekaVectorsAsSVMLight(vecs,
//		// "/home/jmaxk/temp/DE-we.vecs");
//		String malletFile = "/home/jmaxk/temp/test.vecs";
//		InstanceList instances = loadMalletInstances(new File(malletFile));
//		Instances wekaInstances = convert2WekaInstances(instances);
//		VectorSaver.saveWekaVectorsAsSVMLight(wekaInstances,
//				"/home/jmaxk/temp/vecs2.weka");
//		try {
//			SVMLightLoader l = new SVMLightLoader();
//			l.setSource(new File("/home/jmaxk/temp/DE-we.vecs"));
//			Instances data = l.getDataSet();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}

	// public static void main(String[] args) {
	// try {
	// SVMLightLoader l = new SVMLightLoader();
	// l.setSource(new File("/home/jmaxk/temp/DE.vecs"));
	// // Instances data = l.getDataSet();
	// // if (data.classIndex() == -1)
	// // data.setClassIndex(data.numAttributes() - 1);
	// //
	// // NaiveBayes nb = new NaiveBayes();
	// // nb.buildClassifier(data);
	// // Evaluation eval = new Evaluation(data);
	// // eval.crossValidateModel(nb, data, 10, new Random(1));
	// // String strSummary = eval.toSummaryString(true);
	// // System.out.println(strSummary);
	//
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }

}
