package featpruners.live;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.FeatureVectors;

public class None extends FeaturePruner {

	public Set<String> prune(List<FeatureVectors> vecs,
			Map<String, Integer> totalFeatCounts) {
		return new HashSet<String>();
	}
}
