package featextractors.counters.liwc;

import featextractors.WekaInstanceFeatureExtractor;

public abstract class LIWCFeatureExtractor<T> extends WekaInstanceFeatureExtractor<T, Integer> {
	protected static String prefix = "liwc_";
	protected  static LIWCReader liwcCounter = LIWCReader.getInstance();
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String p) {
		prefix = p;
	}

		
	

}
