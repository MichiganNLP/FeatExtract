package featextractors.counters.expandedliwc;

import featextractors.WekaInstanceFeatureExtractor;

public abstract class ExpandedLIWCFeatureExtractor<T> extends WekaInstanceFeatureExtractor<T, Integer> {

	private  String prefix = "eliwc_";
	

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	protected static ExpandedLIWCReader eLIWCReader = ExpandedLIWCReader
			.getInstance();


	
	

}
