package featextractors.counters.roget;

import java.util.Map;

import featextractors.counters.Util;

public class RogetStringExtractor extends RogetFeatureExtractor<String> {

	public Map<String, Integer> extractFeaturesForObject(String content) {
		Map<String, Integer> categoryCountsOfText = null;
		categoryCountsOfText = rogetFinder.getCategoryCountsOfText(content);
		categoryCountsOfText = Util.addPrefixToIMap(categoryCountsOfText,
				getPrefix());

		return categoryCountsOfText;
	}

}
