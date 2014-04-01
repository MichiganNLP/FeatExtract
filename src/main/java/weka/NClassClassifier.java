package weka;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Random;

import util.ObjectSerializer;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import featextractors.WekaInstanceFeatureExtractor;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class NClassClassifier {
//TODO decouple making the vectors from this. it shouldn't take in a builder, but rather a list of vectors 
//	// Internal
//	private WekaFeatureBuilder featureBuilder;
//	private final String CLASS_NAME = "***CNAME***";
//	private PrintWriter logFile;
//	private Instances trainingInstances = null;
//	private Instances testingInstances = null;
//	private HashMap<String, Attribute> attributesLookup = new HashMap<String, Attribute>();
//	private FastVector allAttributes = new FastVector(100000);
//	private List<FeatureVectors> trainingData = new ArrayList<FeatureVectors>();
//	private List<FeatureVectors> testingData = new ArrayList<FeatureVectors>();
//	private List<FeatureVectors> rawVectors = new ArrayList<FeatureVectors>();
//	private List<String> classNames = new ArrayList<String>();
//	private HashMap<String, String> classNamesFromFile = new HashMap<String, String>();
//	private String experimentDir;
//	private String classifierFile;
//	private String attrFile;
//	private List<String> classFiles = new ArrayList<String>();
//	private String attrLookupFile;
//
//	private String instancesFile;
//
//	// Getters & setters
//	public String getExperimentDir() {
//		return experimentDir;
//	}
//
//	public void setExperimentDir(String experimentDir) {
//		this.experimentDir = experimentDir;
//		classifierFile = experimentDir + "classifier.model";
//		for (String cName : classNames) {
//			String cSerializedFile = experimentDir + cName + ".vecs";
//			classFiles.add(cSerializedFile);
//			classNamesFromFile.put(cName, cSerializedFile);
//		}
//		attrFile = experimentDir + "attributes";
//		instancesFile = experimentDir + "instances";
//		attrLookupFile = experimentDir + "attrLookup";
//		try {
//			logFile = new PrintWriter(new FileWriter(new File(experimentDir
//					+ "log"), true));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	// Configurable
//	private Classifier classifier;
//
//	public Classifier getClassifier() {
//		return classifier;
//	}
//
//	public void setClassifier(Classifier classifier) {
//		this.classifier = classifier;
//	}
//
//	private boolean useSavedFeatureVectors;
//	private boolean balanceClasses;
//	private boolean save;
//
//
//	public void shouldUseSaved(boolean b) {
//		useSavedFeatureVectors = b;
//	}
//
//	public void shouldBalanceClasses(boolean b) {
//		balanceClasses = b;
//	}
//
//	public void shouldSaveResults(boolean b) {
//		save = b;
//	}
//
//	public void evaluateWithCrossFoldValidation() {
//		useCrossFold = true;
//		percentInstancesReservedForTraining = 100;
//
//	}
//
//	private boolean useCrossFold;
//	private int percentInstancesReservedForTraining;
//
//	public void evaluteWithTestingSet(int testingPercent) {
//		useCrossFold = false;
//		percentInstancesReservedForTraining = testingPercent;
//	}
//
//	public NClassClassifier(WekaFeatureBuilder wfb) {
//		featureBuilder = wfb;
//		classNames = wfb.getClasses();
//	}
//
//	private void checkSetup() {
//		if (experimentDir == null) {
//			logFile.println("Experiment directory must be set, using setExperimentDir()");
//			System.exit(0);
//		}
//		if ((Boolean) useCrossFold == null) {
//			logFile.println("Must set evaluation methodology, using either cross fold or testing set");
//			System.exit(0);
//		}
//		if (classifier == null) {
//			logFile.println("Must choose a classifier using setClassifier(Classifier c)");
//			System.exit(0);
//		}
//	}
//
//	public void generateFeatureVectors() {
//		if (useSavedFeatureVectors) {
//			logFile.println("Loading vectors from serialized files");
//			deserializeFeatureVectors();
//		} else {
//			logFile.println("Creating feature vectors from scratch");
//			createAllVectors();
//			if (balanceClasses)
//				balanceClasses();
//			convertRawVectorsToWekaAttributes();
//
//			splitIntoTrainingAndTestingInstances();
//		}
//
//	
//
//	}
//
//	public Integer findMin(Map<String, Integer> map) {
//		PriorityQueue<Integer> pq = new PriorityQueue<Integer>();
//		for (Integer v : map.values()) {
//			pq.add(v);
//		}
//		return pq.peek();
//	}
//
//	public Map<String, Integer> countClasses(List<FeatureVectors> vecs) {
//		Map<String, Integer> classCounts = new HashMap<String, Integer>();
//		for (FeatureVectors trainingVecs : vecs) {
//			String label = trainingVecs.className;
//			Integer oldCount = classCounts.get(label);
//			if (oldCount == null)
//				oldCount = 0;
//			classCounts.put(label, oldCount + 1);
//		}
//		return classCounts;
//	}
//
//	private void balanceClasses() {
//
//		Map<String, Integer> classCounts = countClasses(rawVectors);
//		int smallestTrainingClass = findMin(classCounts);
//
//		List<FeatureVectors> newRawVectors = new ArrayList<FeatureVectors>();
//		classCounts = new HashMap<String, Integer>();
//
//		for (FeatureVectors trainingVecs : rawVectors) {
//			String label = trainingVecs.className;
//			Integer oldCount = classCounts.get(label);
//			if (oldCount == null)
//				oldCount = 0;
//			int newCount = oldCount + 1;
//			classCounts.put(label, newCount);
//			if (newCount <= smallestTrainingClass)
//				newRawVectors.add(trainingVecs);
//		}
//		rawVectors = newRawVectors;
//	}
//
//	public void save() {
//		if (save) {
//			ObjectSerializer<Classifier> classifierSaver = new ObjectSerializer<Classifier>();
//			ObjectSerializer<FastVector> attrSaver = new ObjectSerializer<FastVector>();
//			ObjectSerializer<HashMap<String, Attribute>> attrLookupSaver = new ObjectSerializer<HashMap<String, Attribute>>();
//			ObjectSerializer<Instances> instancesSaver = new ObjectSerializer<Instances>();
//
//			attrSaver.save(attrFile, allAttributes);
//			attrLookupSaver.save(attrLookupFile, attributesLookup);
//			instancesSaver.save(instancesFile, trainingInstances);
//			classifierSaver.save(classifierFile, classifier);
//		}
//	}
//
//	private void evaluateClassifier() {
//		if (useCrossFold)
//			evaluateWithCrossFold();
//		else
//			evaluateWithTestingSet();
//	}
//
//	private void trainClassifier() {
//
//		try {
//			System.out.println(trainingInstances.numInstances());
//			classifier.buildClassifier(trainingInstances);
//		} catch (Exception e) {
//			logFile.println(e.getMessage());
//		}
//	}
//
//	private void evaluateWithTestingSet() {
//		try {
//			logFile.println("EVALUATION");
//			Evaluation eTest = new Evaluation(testingInstances);
//			eTest.evaluateModel(classifier, testingInstances);
//			String strSummary = eTest.toSummaryString();
//			logFile.println(strSummary);
//		} catch (Exception e) {
//			logFile.println(e.getMessage());
//		}
//	}
//
//	private void evaluateWithCrossFold() {
//		try {
//			Evaluation eval = new Evaluation(trainingInstances);
//			eval.crossValidateModel(classifier, trainingInstances, 10,
//					new Random(1));
//			String strSummary = eval.toSummaryString(true);
//			logFile.println(strSummary);
//			for (int i = 0; i < classNames.size(); i++) {
//				logFile.println("Class:" + classNames.get(i));
//				logFile.println("FN: " + eval.numFalseNegatives(i));
//				logFile.println("FP: " + eval.numFalsePositives(i));
//				logFile.println("TN: " + eval.numTrueNegatives(i));
//				logFile.println("TP: " + eval.numTruePositives(i));
//				logFile.println();
//			}
//
//		} catch (Exception e) {
//			logFile.println(e.getMessage());
//		}
//	}
//
//	private void splitIntoTrainingAndTestingInstances() {
//		int instSize = 0;
//		for (FeatureVectors v : rawVectors) {
//			instSize = instSize + v.instances.size();
//		}
//		trainingInstances = new Instances("train", allAttributes, instSize);
//		trainingInstances.setClassIndex(0);
//		testingInstances = new Instances("test", allAttributes, instSize);
//		testingInstances.setClassIndex(0);
//
//		// If we want to do cross-validation, we use them all for training
//		if (percentInstancesReservedForTraining == 100) {
//
//			for (FeatureVectors vec : rawVectors) {
//				convertRawVectorsToWekaAttributesHelper(vec.instances);
//				addInstances(vec.instances, vec.className, trainingInstances);
//
//			}
//
//			logFile.println("Training Instances statistics\n"
//					+ trainingInstances.attributeStats(trainingInstances
//							.classIndex()) + "\n\n");
//		}
//		// Otherwise we split them apprpriately
//		else {
//			for (FeatureVectors f : rawVectors) {
//				List<FeatureVectors> splitVecs = splitForTrainAndTest(f,
//						percentInstancesReservedForTraining);
//				trainingData.add(splitVecs.get(0));
//				testingData.add(splitVecs.get(1));
//			}
//			for (FeatureVectors train : trainingData) {
//				addInstances(train.instances, train.className,
//						trainingInstances);
//			}
//			for (FeatureVectors test : testingData) {
//				addInstances(test.instances, test.className, trainingInstances);
//			}
//			logFile.println("Training Instances statistics\n"
//					+ trainingInstances.attributeStats(trainingInstances
//							.classIndex()) + "\n\n");
//			logFile.println("Testing Instances statistics\n"
//					+ testingInstances.attributeStats(testingInstances
//							.classIndex()) + "\n\n");
//		}
//	}
//
//	private void convertRawVectorsToWekaAttributes() {
//		logFile.println("Converting class1 vectors to weka");
//		Iterator<FeatureVectors> vectorIterator = rawVectors.iterator();
//		while (vectorIterator.hasNext()) {
//			FeatureVectors f = vectorIterator.next();
//			convertRawVectorsToWekaAttributesHelper(f.instances);
//		}
//		// for memory freeing
//
//		// Put the classname in the vecotr
//		FastVector fvClassVal = new FastVector(classNames.size());
//		for (String cName : classNames) {
//			fvClassVal.addElement(cName);
//		}
//		Attribute ClassAttribute = new Attribute(CLASS_NAME, fvClassVal);
//		allAttributes.addElement(ClassAttribute);
//
//		// Put all the attributes in one master list
//		for (Attribute a : attributesLookup.values()) {
//			allAttributes.addElement(a);
//		}
//	}
//
//	private void convertRawVectorsToWekaAttributesHelper(
//			List<Map<String, Object>> vectorList) {
//		for (Map<String, Object> vectorsForBlog : vectorList) {
//			for (Entry<String, Object> vector : vectorsForBlog.entrySet()) {
//				String term = (String) vector.getKey();
//				if (!attributesLookup.containsKey(term)) {
//					attributesLookup.put(term, new Attribute(term));
//				}
//			}
//		}
//	}
//
//	private void createAllVectors() {
//		rawVectors = featureBuilder.extractAll();
//	}
//
//	private void deserializeFeatureVectors() {
//		// ObjectSerializer<FeatureVectors> featureVectorLoader = new
//		// ObjectSerializer<FeatureVectors>();
//		ObjectSerializer<FastVector> attrLoader = new ObjectSerializer<FastVector>();
//		ObjectSerializer<HashMap<String, Attribute>> attrLookupLoader = new ObjectSerializer<HashMap<String, Attribute>>();
//		ObjectSerializer<Instances> instancesLoader = new ObjectSerializer<Instances>();
//
//		// List<File> vectorFiles =
//		// FileUtil.findFilesInDirWithExt(experimentDir,
//		// ".vecs");
//
//		// for (File vecFile : vectorFiles) {
//		// FeatureVectors vecs = featureVectorLoader.load(vecFile.getName());
//		// rawVectors.add(vecs);
//		// }
//		allAttributes = attrLoader.load(attrFile);
//		attributesLookup = attrLookupLoader.load(attrLookupFile);
//		trainingInstances = instancesLoader.load(instancesFile);
//		if (allAttributes == null || attributesLookup == null
//				|| trainingInstances.numInstances() == 0) {
//			logFile.print("ERROR DESERIALZING");
//			System.exit(0);
//		}
//
//	}
//
//	private void addInstances(List<Map<String, Object>> vectorsForClass,
//			String mClass, Instances instances) {
//		Instance inst = new Instance(allAttributes.size());
//		inst.setValue((Attribute) allAttributes.elementAt(0), mClass);
//		for (Map<String, Object> blogVector : vectorsForClass) {
//
//			for (Entry<String, Object> e : blogVector.entrySet()) {
//				Attribute a = attributesLookup.get(e.getKey());
//				if (a != null) {
//					Object val = e.getValue();
//					if (val instanceof Double)
//						inst.setValue(a, (Double) val);
//					else if (val instanceof Integer) {
//						inst.setValue(a, (Integer) val);
//					} else if (val instanceof Boolean) {
//						Boolean bVal = (Boolean) val;
//						if (bVal)
//							inst.setValue(a, 1);
//						else
//							inst.setValue(a, 0);
//
//					}
//				}
//			}
//		}
//		instances.add( inst);
//
//	}
//
//	private List<FeatureVectors> splitForTrainAndTest(FeatureVectors allItems,
//			int percentForTraining) {
//		List<FeatureVectors> lists = new ArrayList<FeatureVectors>();
//
//		List vectors = allItems.instances;
//		int per = (vectors.size() * percentForTraining) / 100;
//		lists.add(new FeatureVectors(new ArrayList(vectors.subList(0, per)),
//				allItems.className));
//		lists.add(new FeatureVectors(new ArrayList(vectors.subList(per,
//				vectors.size())), allItems.className));
//		return lists;
//	}
//
//	@Override
//	public String toString() {
//		int testCount = 0;
//		int trainCount= 0;
//		if (testingInstances != null)
//			testCount = testingInstances.numInstances();
//		if (trainingInstances != null)
//			trainCount = trainingInstances.numInstances();
//		return "NClassClassifier [featureBuilder=" + featureBuilder
//				+ "\n logFile=" + logFile + "\n trainingInstances="
//				+ trainCount + "\n testingInstances="
//				+ testCount + "\n allAttributes="
//				+ allAttributes.size() + "\n " + "trainingData="
//				+ trainingData.size() + "\n testingData=" + testingData.size()
//				+ "\n rawVectors=" + rawVectors.size() + "\n classNames="
//				+ classNames + "\n classNamesFromFile=" + classNamesFromFile
//				+ "\n experimentDir=" + experimentDir + "\n classifierFile="
//				+ classifierFile + "\n attrFile=" + attrFile + "\n classFiles="
//				+ classFiles + "\n attrLookupFile=" + attrLookupFile
//				+ "\n instancesFile=" + instancesFile + "\n classifier="
//				+ classifier.getClass() + "\n useSavedFeatureVectors="
//				+ useSavedFeatureVectors + "\n balanceClasses="
//				+ balanceClasses + "\n save=" + save + "\n useCrossFold="
//				+ useCrossFold + "\n percentInstancesReservedForTraining="
//				+ percentInstancesReservedForTraining + "]";
//	}
//
//	public void run() {
//		logFile.println("\n\n=====================================================================================");
//		logFile.println(new Date());
//		logFile.println("Building a " + classifier.getClass() + " classifier");
//		logFile.println("Using the following Feature Extractors:");
//		List<WekaInstanceFeatureExtractor> extractors = featureBuilder
//				.getFeatureExtractors();
//		for (WekaInstanceFeatureExtractor fe : extractors) {
//			logFile.println("\t" + fe.getClass());
//		}
//		checkSetup();
//		logFile.flush();
//		generateFeatureVectors();
//		logFile.flush();
//		trainClassifier();
//		logFile.flush();
//		evaluateClassifier();
//		logFile.flush();
//		logFile.println("CLASSIFIER INFO: \n");
//		logFile.println(toString());
//		logFile.println(new Date());
//		logFile.flush();
//		save();
//		logFile.flush();
//		logFile.close();
//	}

}
