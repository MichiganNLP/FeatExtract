import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import util.FeatureVectors;
import util.VectorLoader;
import weka.WekaFeatureBuilder;

public class Experiment {
	protected static Logger log = Logger.getLogger(Experiment.class);

	public static void main(String[] args) {
		ExperimentConfig config = ExperimentConfig.fromFile(args[0]);
		String root = config.getDataDir();
		String experimentDir = config.getExperimentDir();
		WekaFeatureBuilder<String> builder = config.getFeatureBuilder();
		if (!new File(experimentDir).exists())
			new File(experimentDir).mkdir();
		ArrayList<FeatureVectors> allVecs = new ArrayList<FeatureVectors>();
		Map<String, String> labeledData = generateDataFromDir(new File(root));
		System.out.println(labeledData.entrySet().size());
		allVecs.addAll(builder.extractAll(labeledData));

		List<FeatureVectors> prunedVecs = builder.prune(allVecs);

		VectorLoader.toMallet(experimentDir + "allVecs.txt", prunedVecs);
	}

	public static Map<String, String> generateDataFromDir(File root) {
		Map<String, String> data = new HashMap<String, String>();
		for (File langFile : root.listFiles()) {

			String label = langFile.getName().split("\\.")[0];
			try {
				BufferedReader b = new BufferedReader(new FileReader(langFile));
				String line = "";
				while ((line = b.readLine()) != null) {
					data.put(line, label);

				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return data;
	}
//
//	public static Map<String, String> generateDataFromFile(File langFile) {
//
//		Map<String, String> data = new HashMap<String, String>();
//		String label = langFile.getName().split("\\.")[0];
//		try {
//			BufferedReader b = new BufferedReader(new FileReader(langFile));
//			String line = "";
//			while ((line = b.readLine()) != null) {
//				data.put(line, label);
//
//			}
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return data;
//	}

}
