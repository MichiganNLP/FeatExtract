package featextractors.tf;

import java.util.HashMap;
import java.util.Map;

public class TFStringExtractor extends TFExtractor<String> {

	public TFStringExtractor(Integer n, String vecDir) {
		super(n,vecDir);
	}

	@Override
	public Map<String, Integer> extractFeaturesForObject(
			Map<String, String> otherData, String item) {
		Map<String, Integer> tfIDFCounts = new HashMap<String, Integer>();

		if (tf == null)
			init(otherData);
		String[] words = item.split(" ");
		for (String word : words) {
			if (topWords.contains(word))
				tfIDFCounts.put(word, 1);
		}

		return tfIDFCounts;
	}

}
