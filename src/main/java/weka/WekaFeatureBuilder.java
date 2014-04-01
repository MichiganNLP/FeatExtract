package weka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import util.FeatureVectors;

import featextractors.WekaCorpusFeatureExtractor;
import featextractors.WekaFeatureExtractor;
import featextractors.WekaInstanceFeatureExtractor;
import featpruners.live.FeaturePruner;

public class WekaFeatureBuilder<T> {

	private List<WekaFeatureExtractor<T>> featureExtractors = new ArrayList<WekaFeatureExtractor<T>>();
	private List<FeaturePruner> pruners = new ArrayList<FeaturePruner>();


	public void addFeatureExtractor(WekaFeatureExtractor<T> e) {
		featureExtractors.add(e);
	}

	public void addFeaturePruner(FeaturePruner p) {
		pruners.add(p);
	}

	public List<WekaFeatureExtractor<T>> getFeatureExtractors() {
		return featureExtractors;
	}

	public void setFeatureExtractors(
			List<WekaFeatureExtractor<T>> featureExtractors) {
		this.featureExtractors = featureExtractors;
	}

	private Map<String, Integer> featCounts = new HashMap<String, Integer>();
	private Set<String> allowedFeatures;

	public void updateFeatCounts(Set<String> keys) {
		for (String key : keys) {
			Integer count = featCounts.get(key);
			if (count == null)
				count = 1;
			featCounts.put(key, count + 1);
		}
	}

	public void findAllowedFeats(List<FeatureVectors> vecs) {
		allowedFeatures = new HashSet<String>();
		for (FeaturePruner f : pruners) {
			System.out.println("start: " + featCounts.size());
			allowedFeatures.addAll(f.prune(vecs, featCounts));
			System.out.println("current: " + allowedFeatures.size());
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<FeatureVectors> extractAll(Map<T, String> labeledData) {
		List<FeatureVectors> allVectors = new ArrayList<FeatureVectors>();

		int counter = 0;
		for (Entry<T, String> labeledInstance : labeledData.entrySet()) {
			List<Map<String, Object>> featureVectorsForClass = new ArrayList<Map<String, Object>>();

			String label = labeledInstance.getValue();
			T data = labeledInstance.getKey();
			for (WekaFeatureExtractor<T> f : featureExtractors) {
				Map<String, Object> vec = null;
				if (f instanceof WekaInstanceFeatureExtractor) {
					WekaInstanceFeatureExtractor extractor = (WekaInstanceFeatureExtractor) f;
					vec = extractor.extractFeaturesForObject(data);
				} else if (f instanceof WekaCorpusFeatureExtractor) {
					WekaCorpusFeatureExtractor extractor = (WekaCorpusFeatureExtractor) f;
					vec = extractor.extractFeaturesForObject(labeledData, data);
				}
				updateFeatCounts(vec.keySet());
				featureVectorsForClass.add(vec);
			}
			if (++counter % 100000 == 0) {
				System.out.println(counter);
			}
			FeatureVectors classVecs = new FeatureVectors(
					featureVectorsForClass, label);
			allVectors.add(classVecs);
		}
		
		return allVectors;

//		if (pruners.isEmpty())
//			return allVectors;
//		else {
//			findAllowedFeats(allVectors);
//			List<FeatureVectors> filteredVectors = filterVectors(allVectors);
//			return filteredVectors;
//		}
	}
	
	public List<FeatureVectors> prune( List<FeatureVectors> allVectors){
		if (pruners.isEmpty())
			return allVectors;
		else {
			findAllowedFeats(allVectors);
			List<FeatureVectors> filteredVectors = filterVectors(allVectors);
			return filteredVectors;
		}
	}

	public List<FeatureVectors> filterVectors(List<FeatureVectors> vecs) {
		List<FeatureVectors> newVecs = new ArrayList<FeatureVectors>();
		for (FeatureVectors v : vecs) {
			List<Map<String, Object>> newData = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> data = v.instances;
			for (Map<String, Object> datum : data) {
				HashMap<String, Object> newDatum = new HashMap<String, Object>();
				for (Entry<String, Object> e : datum.entrySet()) {
					String key = e.getKey();
					if (allowedFeatures.contains(key))
						newDatum.put(key, e.getValue());
				}
				if (!newDatum.isEmpty()) {
					newData.add(newDatum);
				}
			}
			newVecs.add(new FeatureVectors(newData, v.className));
		}
		return newVecs;

	}



}
