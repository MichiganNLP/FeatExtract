package featpruners.live.chisquared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;

import util.FeatureVectors;
import util.MapSorter;
import featpruners.live.FeaturePruner;

public class ChiSquared extends FeaturePruner{

	private Set<String> allFeatures = new HashSet<String>();
	private Set<String> allLabels = new HashSet<String>();
	private HashMap<String, Double> classCounts = new HashMap<String, Double>();

	public HashMap<String, HashMap<String, Double>> featCountsForClasses = new HashMap<String, HashMap<String, Double>>();
	public HashMap<String, Integer> rawFeatCounts = new HashMap<String, Integer>();
	private Integer DF = null;
	private Double P;
	
	public ChiSquared(Double p){
		this.P = p;
	}


	public Set<String> getAllowedFeatures() {
		SortedSet<Entry<String, Double>> chisqVals = sortByChiSquared(false);
		Set<String> goodFeats = new HashSet<String>();
		for (Entry<String, Double> featVals : chisqVals) {
			String feat = featVals.getKey();
			Double val = featVals.getValue();
			double pForFeat = ChiSquaredUtils.pochisq(val, DF);
			if (pForFeat < P){
				goodFeats.add(feat);
				if (pForFeat != 0)
					System.out.println(pForFeat);
			}
		}
		return goodFeats;
	}

	public SortedSet<Entry<String, Double>> sortByChiSquared(boolean print) {
		HashMap<String, Double> featVals = new HashMap<String, Double>();
		for (String feature : allFeatures) {
			List<ArrayList<Double>> observed = buildObserved(feature);
			if (DF == null)
				calculateDF(observed);
			double chi = computeChiSquared(observed);
			featVals.put(feature, chi);
		}
		SortedSet<Entry<String, Double>> sortedFeatVals = MapSorter
				.entriesSortedByValues(featVals, false);
		if (print) {
			for (Entry<String, Double> featVal : sortedFeatVals) {
				String feature = featVal.getKey();
				Double chi = featVal.getValue();
				Integer docCount = rawFeatCounts.get(feature);
				System.out.println(feature + " " + chi + " " + docCount);
			}
			return null;
		} else
			return sortedFeatVals;
	}
	
	public void load(List<FeatureVectors>vecs){
		for(FeatureVectors vector: vecs){
			String label = vector.className;
			allLabels.add(label);
			updateClassCounts(label, 1.0);

			for(Map<String, Object>  instances: vector.instances){
				for(Entry<String, Object>  instance: instances.entrySet()){
					String feature = instance.getKey();
					updateFeatCountsForLabel(feature, label);
					updateRawFeatCounts(feature);
					allFeatures.add(feature);
				}
			}
		}
	}

	public void calculateDF(List<ArrayList<Double>> table) {
		int rows = table.size();
		int columns = table.get(0).size();
		DF = getDF(rows, columns);
	}

	


	public void updateFeatCountsForLabel(String feat, String label) {
		// Get counts for feature, init if we haven't seen it before
		HashMap<String, Double> counts = featCountsForClasses.get(feat);
		if (counts == null) {
			counts = new HashMap<String, Double>();
		}

		// get counts for feature and class, init if we haven't seen it before
		Double countForClass = counts.get(label);
		if (countForClass == null) {
			countForClass = 1.0;
		} else {
			countForClass = countForClass + 1.0;
		}
		counts.put(label, countForClass);
		featCountsForClasses.put(feat, counts);
	}

	public void updateRawFeatCounts(String feat) {
		Integer count = rawFeatCounts.get(feat);
		if (count == null)
			count = 1;
		else {
			count = count + 1;
		}
		rawFeatCounts.put(feat, count);
	}

	public List<ArrayList<Double>> buildObserved(String feat) {
		HashMap<String, Double> countsForLabel = featCountsForClasses.get(feat);
		if (countsForLabel == null) {
			System.out.println("haven't seen this before");
			return null;
		}
		List<ArrayList<Double>> observed = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> row1 = new ArrayList<Double>();
		ArrayList<Double> row2 = new ArrayList<Double>();

		for (String label : allLabels) {
			Double countForLabel = countsForLabel.get(label);
			if (countForLabel == null)
				countForLabel = 0.0;
			Double totalCountForLabel = classCounts.get(label);
			row1.add(totalCountForLabel - countForLabel);
			row2.add(countForLabel);
		}
		observed.add(row1);
		observed.add(row2);
		return observed;
	}

	public List<ArrayList<Double>> buildExpected(String feat) {
		HashMap<String, Double> counts = featCountsForClasses.get(feat);
		if (counts == null) {
			System.out.println("haven't seen this before");
			return null;
		}
		List<ArrayList<Double>> observed = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> row1 = new ArrayList<Double>();
		ArrayList<Double> row2 = new ArrayList<Double>();

		for (String label : allLabels) {
			Double totalCountForLabel = classCounts.get(label);
			row1.add(totalCountForLabel / 2);
			row2.add(totalCountForLabel / 2);
		}
		observed.add(row1);
		observed.add(row2);
		return observed;
	}

	public double getExpectedValue(int columnTotal, int numRows, int tableTotal) {
		double expected = (double) columnTotal / (double) numRows;
		return expected;
	}

	private void updateClassCounts(String label, Double countToAdd) {
		Double oldCount = classCounts.get(label);
		if (oldCount == null) {
			classCounts.put(label, countToAdd);
		} else {
			classCounts.put(label, oldCount + countToAdd);
		}
	}

	public double computeChiSquared(List<ArrayList<Double>> observed) {
		List<Double> rowTotal = new ArrayList<Double>();
		List<Double> columnTotal = new ArrayList<Double>(observed.get(0).size());

		for (int i = 0; i < observed.size(); i++) {
			ArrayList<Double> firstRow = observed.get(i);
			Double row = 0.0;
			for (int j = 0; j < firstRow.size(); j++) {
				Double toAdd = firstRow.get(j);
				row += toAdd;
				if (columnTotal.size() < firstRow.size()) {
					columnTotal.add(toAdd);
				} else {
					Double oldColumn = columnTotal.get(j);
					columnTotal.set(j, oldColumn + toAdd);
				}

			}
			rowTotal.add(row);
		}
		// got row totals
		// got column totals
		int tableTotal = 0;
		for (Double i : rowTotal) {
			tableTotal += i;
		}
		// got table total
		List<ArrayList<Double>> expected = new ArrayList<ArrayList<Double>>();

		for (int i = 0; i < rowTotal.size(); i++) {
			Double row = rowTotal.get(i);
			ArrayList<Double> allExpected = new ArrayList<Double>();
			for (int j = 0; j < columnTotal.size(); j++) {
				Double column = columnTotal.get(j);
				allExpected.add(getExpectedValue(row, column, tableTotal));
			}
			expected.add(allExpected);
		}
		double chi = 0.0;
		for (int i = 0; i < expected.size(); i++) {
			ArrayList<Double> observation = observed.get(i);
			ArrayList<Double> expect = expected.get(i);
			chi += getChiSquareValue(observation, expect);
		}
		// got chi value
		//bug???
		//int df = getDF(rowTotal.size(), columnTotal.size());
		// got degree of freedom
		return chi;
	}

	public static double getChiSquareValue(ArrayList<Double> obs,
			ArrayList<Double> expected) {
		double chi = 0.0;
		for (int i = 0; i < obs.size(); i++) {
			double subtract = obs.get(i) - expected.get(i);
			chi += (subtract * subtract) / expected.get(i);
		}
		return chi;
	}

	public static double getExpectedValue(Double rowTotal, Double columnTotal,
			int tableTotal) {
		double expected = ((rowTotal * columnTotal) / (double) tableTotal);
		return expected;
	}

	public int getDF(int rows, int columns) {
		int df = (rows - 1) * (columns - 1);
		return df;
	}

	public Set<String> prune(List<FeatureVectors> vecs,
			Map<String, Integer> featCounts) {
		load(vecs);
		Set<String> allowedFeats = getAllowedFeatures();
		return allowedFeats;
		
	}



	
}