package util;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class VectorSaver {

	public static void saveWekaVectorsAsSVMFull(Instances instances,
			String outputFile) {
		try {
			File trainFile = new File(outputFile);
			PrintWriter writer = new PrintWriter(new FileWriter(trainFile));
			int classIdx = instances.classIndex();

			// Go through all instances
			Enumeration enumn = instances.enumerateInstances();
			while (enumn.hasMoreElements()) {
				Instance trainInstance = (Instance) enumn.nextElement();

				// output the class value
				int ci = trainInstance.classIndex();
				Attribute ca = trainInstance.attribute(ci);
				String className = trainInstance.stringValue(ca);
				writer.print(className + " ");

				// output the attributes; iterating using numValues() skips
				// 'missing' values for SparseInstances
				for (int j = 0; j < trainInstance.numValues(); j++) {
					Attribute attribute = trainInstance.attributeSparse(j);
					// Attribute index must be greater than 0
					int attrIdx = attribute.index();
					if (attrIdx != classIdx) {
						writer.print(attribute.name() + ":"
								+ trainInstance.value(attrIdx) + " ");
					}
				}
				writer.println();
			}
			writer.close();
		} catch (Exception e) {
			System.err.println("Error when dumping training instances: " + e);
			e.printStackTrace();
		}
	}

	public static void saveWekaVectorsAsSVMLight(Instances instances,
			String outputFile) {
		try {
			File trainFile = new File(outputFile);
			String m_trainFilename = trainFile.getPath();
			PrintWriter writer = new PrintWriter(new FileWriter(trainFile));
			int classIdx = instances.classIndex();

			// Go through all instances
			Enumeration enumn = instances.enumerateInstances();
			while (enumn.hasMoreElements()) {
				Instance trainInstance = (Instance) enumn.nextElement();

				// output the class value
				double classValue = 0;
				if (!trainInstance.classIsMissing()) {
					classValue = trainInstance.classValue();
					if (classValue == 0) {
						classValue = -1;
					} else {
						classValue = 1;
					}
				}
				writer.print((int) classValue + " ");

				// output the attributes; iterating using numValues() skips
				// 'missing' values for SparseInstances
				for (int j = 0; j < trainInstance.numValues(); j++) {
					Attribute attribute = trainInstance.attributeSparse(j);
					// Attribute index must be greater than 0
					int attrIdx = attribute.index();
					if (attrIdx != classIdx) {
						writer.print((attrIdx + 1) + ":"
								+ trainInstance.value(attrIdx) + " ");
					}
				}
				writer.println();
			}
			writer.close();
		} catch (Exception e) {
			System.err.println("Error when dumping training instances: " + e);
			e.printStackTrace();
		}
	}

	public static void saveWekaVectorsAsSVMLight(
			List<FeatureVectors> instances, String outputFile) {
		try {
			Map<String, Integer> classId = new HashMap<String, Integer>();
			Map<String, Integer> featId = new HashMap<String, Integer>();
			int currentClassIdx = 0;
			int currentFeatIdx = 0;
			File trainFile = new File(outputFile);
			PrintWriter writer = new PrintWriter(new FileWriter(trainFile));

			// Go through all instances
			for (FeatureVectors fv : instances) {

				// output the class value

				Integer classValue = classId.get(fv.className);
				if (classValue == null) {
					classId.put(fv.className, currentClassIdx);
					classValue = currentClassIdx;
					currentClassIdx++;
				}
				if (classValue == 0) {
					classValue = -1;
				} else {
					classValue = 1;
				}

				writer.print((int) classValue + " ");

				// output the attributes; iterating using numValues() skips
				// 'missing' values for SparseInstances
				for (Map<String, Object> featuresVals : fv.instances) {
					for (Entry<String, Object> featVal : featuresVals
							.entrySet()) {
						String featName = featVal.getKey();
						Integer featIdx = featId.get(featName);
						if (featIdx == null) {
							featId.put(featName, currentFeatIdx);
							featIdx = currentFeatIdx;
							currentFeatIdx++;
						}
						writer.print(featIdx + ":" + featVal.getValue() + " ");
					}
				}
				writer.println();
			}

			writer.close();
		} catch (Exception e) {
			System.err.println("Error when dumping training instances: " + e);
			e.printStackTrace();
		}
	}
}
