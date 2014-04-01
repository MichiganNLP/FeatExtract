package featextractors;

import java.util.Map;

/**
 * For when you have to extract features that require additional context 
 * 
 * 
 */
public abstract class WekaCorpusFeatureExtractor<InputType, FeatType> implements WekaFeatureExtractor<InputType> {


	protected String name;

	public abstract Map<String, FeatType> extractFeaturesForObject(Map<InputType, String> otherData, InputType item);

	
}
