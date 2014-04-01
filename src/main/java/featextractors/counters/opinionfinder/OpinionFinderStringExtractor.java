package featextractors.counters.opinionfinder;

import java.util.Map;

import featextractors.counters.Util;

public class OpinionFinderStringExtractor extends
		OpinionFinderFeatureExtractor<String> {

	@Override
	public Map<String, Integer> extractFeaturesForObject(String content) {
		Map<String, Integer> categoryCountsOfText = null;
		categoryCountsOfText = opinionFinder.getCategoryCountsOfText(content);
		categoryCountsOfText = Util.addPrefixToIMap(categoryCountsOfText,
				getPrefix());

		return categoryCountsOfText;
	}

}
