package featextractors;

import java.util.Map;

public abstract class WekaInstanceFeatureExtractor<InputType, FeatType>   implements WekaFeatureExtractor<InputType>{

	protected String name;

	public abstract Map<String, FeatType> extractFeaturesForObject(InputType item);

	public String toString() {
		
		return this.getClass().getSimpleName();
	}
	
	
	
	
}
