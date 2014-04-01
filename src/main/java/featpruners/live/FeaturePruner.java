package featpruners.live;

import java.util.List;
import java.util.Map;
import java.util.Set;

import util.FeatureVectors;

public abstract class FeaturePruner {

	

	
	public  abstract Set<String> prune(List<FeatureVectors> vecs ,Map<String,Integer> totalFeatCounts);

}
