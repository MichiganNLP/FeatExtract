package featextractors.counters.roget;

import featextractors.WekaInstanceFeatureExtractor;


public abstract class RogetFeatureExtractor<T> extends WekaInstanceFeatureExtractor<T, Integer> {

	private static String prefix = "roget_";
	protected static RogetReader rogetFinder = RogetReader.getInstance();
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String p) {
		prefix = p;
	}

}
