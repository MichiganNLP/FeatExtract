package featextractors.counters.expandedliwc;

import java.util.Map;

import featextractors.counters.Util;

public class ExpandedLIWCStringExtractor extends
		ExpandedLIWCFeatureExtractor<String> {

	@Override
	public Map<String, Integer> extractFeaturesForObject(String content) {
		Map<String, Integer> categoryCountsOfText = null;
		categoryCountsOfText = eLIWCReader.getCategoryCountsOfText(content);
		categoryCountsOfText = Util.addPrefixToIMap(categoryCountsOfText,
				getPrefix());

		return categoryCountsOfText;
	}

}
