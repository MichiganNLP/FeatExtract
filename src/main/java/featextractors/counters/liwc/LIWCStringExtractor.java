package featextractors.counters.liwc;

import java.util.Map;

import featextractors.counters.Util;

public class LIWCStringExtractor extends LIWCFeatureExtractor<String> {

	@Override
	public Map<String, Integer> extractFeaturesForObject(String content) {
		Map<String, Integer> categoryCountsOfText = null;
		categoryCountsOfText = liwcCounter.getCategoryCountsOfText(content);
		categoryCountsOfText = Util.addPrefixToIMap(categoryCountsOfText,
				getPrefix());

		return categoryCountsOfText;
	}

}
