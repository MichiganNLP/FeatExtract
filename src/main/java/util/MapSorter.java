package util;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

public class MapSorter {

	
	public static <K, V extends Comparable> SortedSet<Map.Entry<K, V>> entriesSortedByValues(
			Map<K, V> map, final boolean reverse) {
//		SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(
//				new Comparator<Map.Entry<K, V>>() {
//					public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
//						if (reverse) {
//							int res = e1.getValue().compareTo(e2.getValue());
//							return res != 0 ? res : 1;
//						}
//						else{
//							int res = -e1.getValue().compareTo(e2.getValue());
//							return res != 0 ? res : 1; 
//
//						}
//					}
//				});
//		sortedEntries.addAll(map.entrySet());
//		return sortedEntries;
		return null;
	}
	
	public static void main(String[] args) {
		HashMap<String,Double> asdf =  new HashMap<String,Double>();
		asdf.put("a", 1.1);
		asdf.put("b", -1.3);
		asdf.put("c", 4.5);
		System.out.println(asdf);
		System.out.println(MapSorter.entriesSortedByValues(asdf, true));
	}

}