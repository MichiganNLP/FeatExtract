package featextractors.counters.wordnetaffect;

import java.util.Map;

import featextractors.counters.Util;

public class WNAStringExtractor extends WNAFeatureExtractor<String>{

	@Override
	public Map<String, Integer> extractFeaturesForObject(String content) {
		Map<String, Integer> categoryCountsOfText = null;
			categoryCountsOfText = wnaReader
					.getCategoryCountsOfText(content);
			categoryCountsOfText = Util.addPrefixToIMap(categoryCountsOfText, getPrefix());

		return categoryCountsOfText;
	}

}
