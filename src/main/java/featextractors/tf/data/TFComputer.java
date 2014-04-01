package featextractors.tf.data;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TFComputer implements Serializable {


	private static final long serialVersionUID = 1L;

	private Map<String, RawTF> stateTFs = new HashMap<String, RawTF>();

	private ConcurrentHashMap<String, IDFCount> rawIDF = new ConcurrentHashMap<String, IDFCount>();
	private ConcurrentHashMap<String, Double> normalizedIDF = new ConcurrentHashMap<String, Double>();
	private int totalDocs = 0;




	//

	// private Map<String, List<String>> convertDataToCorpus(
	// Map<String, String> allData) {
	// Map<String, List<String>> corpus = new HashMap<String, List<String>>();
	// for (Entry<String, String> e : allData.entrySet()) {
	// String data = e.getKey();
	// String className = e.getValue();
	// List<String> allInstances = corpus.get(className);
	// if (allInstances == null) {
	// allInstances = new ArrayList<String>();
	// }
	// allInstances.add(data);
	// corpus.put(className, allInstances);
	// }
	// return corpus;
	// }

	public TFComputer(Map<String, String> allData) {
		Set<String> allClasses = new HashSet<String>();
		for (Entry<String, String> entry : allData.entrySet()) {
			String key = entry.getValue();
			allClasses.add(key);

			String data = entry.getKey();
			countState(data, key);

		}
		totalDocs = allClasses.size();
		normalizeIDF(totalDocs);
	}

	public Map<String, Double> computeTFIDF(String className) {
		Map<String, Double> tfIDF = new HashMap<String, Double>();
		RawTF tfForState = stateTFs.get(className);
		ConcurrentHashMap<String, Integer> rawCounts = tfForState.getCounts();
		for (Entry<String, Integer> e : rawCounts.entrySet()) {
			String word = e.getKey();
			double idf = normalizedIDF.get(word);
			double tf = (double) e.getValue();
			tfIDF.put(word, tf * idf);
		}
		return tfIDF;

	}

	private void countState(String line, String className) {

		RawTF termFreqs = stateTFs.get(className);
		if (termFreqs == null) {
			termFreqs = new RawTF();
			stateTFs.put(className, termFreqs);
		}
		ConcurrentHashMap<String, Integer> counts = termFreqs.getCounts();
		String[] words = line.split(" ");
		for (String word : words) {

			// TF
			Integer count = counts.get(word);
			if (count == null)
				count = 0;
			counts.put(word, count + 1);
			termFreqs.incremenentWordTotal();

			// idf
			IDFCount idfCount = rawIDF.get(word);
			if (idfCount == null) {
				idfCount = new IDFCount();
			}
			idfCount.addCountForClass(className);
			rawIDF.put(word, idfCount);
		}
	}

	private void normalizeIDF(int totalDocs) {
		for (Entry<String, IDFCount> e : rawIDF.entrySet()) {
			double normalized = Math.log(totalDocs / e.getValue().getCount());
			normalizedIDF.put(e.getKey(), normalized);
		}
	}

	public class RawTF {

		private ConcurrentHashMap<String, Integer> counts = new ConcurrentHashMap<String, Integer>();
		private int totalWords = 0;

		public ConcurrentHashMap<String, Integer> getCounts() {
			return counts;
		}

		public void setCounts(ConcurrentHashMap<String, Integer> counts) {
			this.counts = counts;
		}

		public int getTotalWords() {
			return totalWords;
		}

		public void setTotalWords(int totalWords) {
			this.totalWords = totalWords;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (Entry<String, Integer> e : counts.entrySet()) {
				sb.append(e.getKey() + ":" + e.getValue() + "\n");
			}
			sb.append(totalWords);
			return sb.toString();
		}

		public void incremenentWordTotal() {
			totalWords++;
		}

	}

	public class IDFCount {

		private Set<String> classes = new HashSet<String>();

		public void addCountForClass(String docClass) {
			classes.add(docClass);
		}

		public int getCount() {
			return classes.size();
		}
	}
}
