package featextractors.counters.opinionfinder;

import featextractors.WekaInstanceFeatureExtractor;

public abstract class OpinionFinderFeatureExtractor<T> extends WekaInstanceFeatureExtractor<T, Integer> {

	private  String prefix = "opf_";
	

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	protected static OpinionFinderReader opinionFinder = OpinionFinderReader
			.getInstance();


	
	

}
