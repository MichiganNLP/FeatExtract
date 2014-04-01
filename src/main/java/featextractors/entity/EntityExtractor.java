package featextractors.entity;

import featextractors.WekaInstanceFeatureExtractor;

public abstract class EntityExtractor<T> extends WekaInstanceFeatureExtractor<T, Integer> {

	private static final String prefix = "ent_";
	
	public String getPrefix() {
		return prefix;
	}


}
