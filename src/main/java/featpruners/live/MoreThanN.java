package featpruners.live;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import util.FeatureVectors;

public class MoreThanN extends FeaturePruner {

	private Integer N;

	public MoreThanN(int n) {
		this.N = n;
	}

	public Set<String> prune(List<FeatureVectors> vecs, Map<String,Integer> totalFeatCounts) {
		Set<String> allowedFeats = new HashSet<String>();
		System.out.println("started with" + totalFeatCounts.size());
		for (Entry<String, Integer> e : totalFeatCounts.entrySet()) {
			if (e.getValue() > N)
				allowedFeats.add(e.getKey());
		}
		System.out.println("ended with" + allowedFeats.size());
		return allowedFeats;
	}

	

}
