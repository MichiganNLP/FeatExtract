import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import util.ObjectSerializer;
import featextractors.tf.data.TFComputer;

public class TFIDF {
	static String rootdir = "/home/jmaxk/temp/fb/data";
	static String vecDir = "/home/jmaxk/temp/fb/vecs/";
	static String vecHR = "/home/jmaxk/temp/fb/vecs-hr/";

	static int NUM_TO_PRINT = 500;
	static ObjectSerializer<TFComputer> tfcs = new ObjectSerializer<TFComputer>();

	public static void main(String[] args) {

//		makeVecs();
		loadVecs();

	}

	public static void makeVecs() {
		Map<String, String> otherData = generateDataFromDir(new File(rootdir));

		TFComputer c = new TFComputer(otherData);
		tfcs.save("/home/jmaxk/temp/vecs/tfcs", c);

//		Set<String> keys = new HashSet<String>(otherData.values());
//		for (String key : keys) {
//			System.out.println(key);
//			Map<String, Double> tfidf = c.computeTFIDF(key);
//			ObjectSerializer<Map<String, Double>> dr = new ObjectSerializer<Map<String, Double>>();
//			String file = "/home/jmaxk/temp/fb/vecs/" + key;
//			dr.save(file, tfidf);
//		}
	}

	public static void loadVecs() {
		TFComputer c = tfcs.load("/home/jmaxk/temp/vecs/tfcs");

		for (File f : new File(vecDir).listFiles()) {
			try {
				PrintWriter w = new PrintWriter(new FileWriter(new File(vecHR + f.getName() + ".txt")));
				System.out.println(f.getName());
				
				ObjectSerializer<Map<String, Double>> dr = new ObjectSerializer<Map<String, Double>>();
				Map<String, Double> vecs = dr.load(f);
				Map<String, Double> sortedVecs = sortMapByValue(vecs);
				int i = 0;
				for (Entry<String, Double> e : sortedVecs.entrySet()) {
					if (++i > NUM_TO_PRINT)
						break;
					w.println(e);
				}
				w.flush();
				w.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static Map<String, Double> sortMapByValue(Map<String, Double> map) {
		List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(
				map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1,
					Map.Entry<String, Double> o2) {
				return (o2.getValue().compareTo(o1.getValue()));
			}
		});

		Map<String, Double> result = new LinkedHashMap<String, Double>();
		for (Iterator<Map.Entry<String, Double>> it = list.iterator(); it
				.hasNext();) {
			Map.Entry<String, Double> entry = it.next();
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	public static Map<String, String> generateDataFromDir(File root) {
		Map<String, String> data = new HashMap<String, String>();
		for (File langFile : root.listFiles()) {

			String label = langFile.getName().split("\\.")[0];
			try {
				BufferedReader b = new BufferedReader(new FileReader(langFile));
				b.lines().parallel().forEach((line) -> data.put(line, label));
				// String line = "";
				// while ((line = b.readLine()) != null) {
				//
				// }
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return data;
	}

}
