package featextractors.counters.ngram;

import featextractors.WekaInstanceFeatureExtractor;

public abstract class NGramFeatureExtractor<T> extends
		WekaInstanceFeatureExtractor<T, Integer> {

	private String prefix = "ngram_";

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
}
