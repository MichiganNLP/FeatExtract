package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class VectorLoader {

	public static void saveVectorsAARF(String filename, Instances instances) {
		try {
			ArffSaver saver = new ArffSaver();
			saver.setInstances(instances);
			saver.setFile(new File(filename));
			saver.writeBatch();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void toMallet(String vecFile, List<FeatureVectors> vectors) {

		try {
			PrintWriter writer = new PrintWriter(new FileWriter(vecFile));
			for (FeatureVectors vector : vectors) {
				writer.print(vector.className + " ");
				for (Map<String, Object> allInstanceData : vector.instances) {
					for (Entry<String, Object> instanceDatum : allInstanceData
							.entrySet()) {
						String featName = instanceDatum.getKey();
						String featVal = instanceDatum.getValue().toString();
						writer.print(featName + ":" + featVal + " ");
					}
				}
				writer.println();
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static List<FeatureVectors> fromMallet(String vecFile) {
		List<FeatureVectors> vecs = new ArrayList<FeatureVectors>();
		try {
			BufferedReader b = new BufferedReader(new FileReader(vecFile));
			String line = "";
			while ((line = b.readLine()) != null) {

				String[] sects = line.replaceAll("  ", " ").split(" ");
				String label = null;
				List<Map<String, Object>> feats = new ArrayList<Map<String, Object>>();
				Map<String,Object> e = new HashMap<String,Object>();

				for (String feat : sects) {
					String[] featVal = feat.split(":");
					if (featVal.length == 2) {
						String feature = featVal[0];
						String value = featVal[1];
						e.put(feature,value);
						if (label != null) {
						} else {
							System.out.println("wtf");
						}
					} else if (featVal.length == 1) {
						label = featVal[0];
					}
				}
				feats.add(e);
				vecs.add(new FeatureVectors(feats, label));

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vecs;
	}

	public static void toMalletByClass(String dir, List<FeatureVectors> vectors) {
		try {
			Map<String, PrintWriter> pws = new HashMap<String, PrintWriter>();
			for (FeatureVectors vector : vectors) {
				String label = vector.className;

				PrintWriter writer = pws.get(label);
				if (writer == null) {
					String outputFile = dir + label + ".vecs";
					writer = new PrintWriter(new FileWriter(outputFile));
					pws.put(label, writer);
				}
				writer.print(label + " ");
				for (Map<String, Object> allInstanceData : vector.instances) {
					for (Entry<String, Object> instanceDatum : allInstanceData
							.entrySet()) {
						String featName = instanceDatum.getKey();
						String featVal = instanceDatum.getValue().toString();
						writer.print(featName + ":" + featVal + " ");
					}
				}
				writer.println();
			}
			for (PrintWriter pw : pws.values()) {
				pw.flush();
				pw.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void toSVMLightByClass(String dir,
			List<FeatureVectors> vectors) {
		try {
			HashMap<String, Integer> featureNames = new HashMap<String, Integer>();
			HashMap<String, Integer> featureVals = new HashMap<String, Integer>();

			HashMap<String, Integer> labelNames = new HashMap<String, Integer>();
			Map<Integer, PrintWriter> pws = new HashMap<Integer, PrintWriter>();

			Integer currentFeatName = 0;
			Integer currentFeatVal = 0;

			Integer currentLabel = 0;
			for (FeatureVectors vector : vectors) {

				// Get the label for this class (as an integer for svm lite)
				Integer label = labelNames.get(vector.className);
				if (label == null) {
					label = currentLabel;
					labelNames.put(vector.className, label);
					currentLabel++;
				}

				// get the output file for this class (or make a new one)
				PrintWriter writer = pws.get(label);
				if (writer == null) {
					String outputFile = dir + label + ".vecs";
					writer = new PrintWriter(new FileWriter(outputFile));
					pws.put(label, writer);
				}

				// line starts with the class Id
				writer.print(label + " ");

				for (Map<String, Object> allInstanceData : vector.instances) {
					for (Entry<String, Object> instanceDatum : allInstanceData
							.entrySet()) {

						// turn the feature into an int
						// ngram_3 : the_big_dog becomes 13243:43
						String featName = instanceDatum.getKey();
						Integer featNameID = featureNames.get(featName);
						if (featNameID == null) {
							featNameID = currentFeatName;
							featureNames.put(featName, featNameID);
							currentFeatName++;
						}
						String featVal = instanceDatum.getValue().toString();
						Integer featValID = featureVals.get(featVal);
						if (featValID == null) {
							featValID = currentFeatVal;
							featureVals.put(featVal, featValID);
							currentFeatVal++;
						}
						writer.print(featNameID + ":" + featVal + " ");
					}
				}
				writer.println();
			}
			for (PrintWriter pw : pws.values()) {
				pw.flush();
				pw.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void toSVMLight(String file, List<FeatureVectors> vectors) {
		try {
			HashMap<String, Integer> featureNames = new HashMap<String, Integer>();
			HashMap<String, Integer> featureVals = new HashMap<String, Integer>();

			HashMap<String, Integer> labelNames = new HashMap<String, Integer>();
			PrintWriter writer = new PrintWriter(new FileWriter(new File(file)));
			Integer currentFeatName = 0;
			Integer currentFeatVal = 0;

			Integer currentLabel = 0;
			for (FeatureVectors vector : vectors) {

				// Get the label for this class (as an integer for svm lite)
				Integer label = labelNames.get(vector.className);
				if (label == null) {
					label = currentLabel;
					labelNames.put(vector.className, label);
					currentLabel++;
				}

				// line starts with the class Id
				writer.print(label);

				for (Map<String, Object> allInstanceData : vector.instances) {
					for (Entry<String, Object> instanceDatum : allInstanceData
							.entrySet()) {

						// turn the feature into an int
						// ngram_3 : the_big_dog becomes 13243:43
						String featName = instanceDatum.getKey();
						Integer featNameID = featureNames.get(featName);
						if (featNameID == null) {
							featNameID = currentFeatName;
							featureNames.put(featName, featNameID);
							currentFeatName++;
						}
						String featVal = instanceDatum.getValue().toString();
						Integer featValID = featureVals.get(featVal);
						if (featValID == null) {
							featValID = currentFeatVal;
							featureVals.put(featVal, featValID);
							currentFeatVal++;
						}
						writer.print(" " + featNameID + ":" + featVal);
					}
				}
				writer.println();
			}

			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
