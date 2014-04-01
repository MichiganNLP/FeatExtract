package featextractors.counters.ngram;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class NGramStringExtractor extends NGramFeatureExtractor<String> {

	private int length;

	public NGramStringExtractor(int l) {
		length = l;
	}

	public Map<String, Integer> extractFeaturesForObject(String rawContent) {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		try {
			NGramExtractor ng = new NGramExtractor();
			ng.extract(rawContent, length, false, true);
			LinkedList<String> ngrams = ng.getNGrams();
			for (String s : ngrams) {
				counts.put(getPrefix() + s.replaceAll(" ", "_"),
						ng.getNGramFrequency(s));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return counts;
	}

}
