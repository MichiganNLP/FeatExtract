package featextractors.tf;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import featextractors.WekaCorpusFeatureExtractor;
import featextractors.tf.data.TFComputer;

public abstract class TFExtractor<T> extends
		WekaCorpusFeatureExtractor<T, Integer> {

	protected Integer n;
	protected TFComputer tf;
	protected Set<String> topWords;
	protected String vecDir;

	public TFExtractor(Integer n, String vecDir) {
		this.n = n;
		this.vecDir = vecDir;
	}

	protected void init(Map<String, String> otherData) {
		tf = new TFComputer(otherData);
		Set<String> allClasses = new HashSet<String>(otherData.values());
		findAllowableWords(allClasses);
	}

	//

	public void findAllowableWords(Set<String> allClasses) {

		topWords = new HashSet<String>();
		for (String className : allClasses) {
			Map<String, Double> wordsForClass = tf.computeTFIDF(className);
			@SuppressWarnings("unchecked")
			Iterator<Map.Entry<String, Double>> sortedItr = sortByValue(
					wordsForClass).entrySet().iterator();
			int counter = 0;
			while (sortedItr.hasNext() && counter < n) {
				topWords.add(sortedItr.next().getKey());
				counter++;
			}

		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	static Map sortByValue(Map map) {
		List list = new LinkedList(map.entrySet());
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue())
						.compareTo(((Map.Entry) (o2)).getValue());
			}
		});

		Map result = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

}
