package featextractors.counters.wordnetaffect;

import featextractors.WekaInstanceFeatureExtractor;

public abstract class WNAFeatureExtractor<T> extends WekaInstanceFeatureExtractor<T, Integer> {

	private  String prefix = "wna_";
	

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	protected static WordNetAffectReader wnaReader = WordNetAffectReader
			.getInstance();


	
	

}
