package featextractors.counters.expandedliwc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

import featextractors.counters.Util;

public class ExpandedLIWCFileExtractor extends ExpandedLIWCFeatureExtractor<File>{

	@Override
	public Map<String, Integer> extractFeaturesForObject(File item) {
		Map<String, Integer> categoryCountsOfText = null;
		try {
			String content = new Scanner(item).useDelimiter("\\Z").next();
			categoryCountsOfText = eLIWCReader
					.getCategoryCountsOfText(content);
			categoryCountsOfText = Util.addPrefixToIMap(categoryCountsOfText, getPrefix());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return categoryCountsOfText;
	}

}
