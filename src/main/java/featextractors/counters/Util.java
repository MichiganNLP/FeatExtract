package featextractors.counters;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Util {


	public  static Map<String,Integer> addPrefixToIMap(Map<String,Integer> oldMap, String prefix){
		Map<String,Integer> prefixedMap = new HashMap<String,Integer>();
		for( Entry<String, Integer>  e: oldMap.entrySet()){
			prefixedMap.put(prefix + e.getKey(), e.getValue());
		}
		return prefixedMap;
	}
	
	public static  Map<String,Double> addPrefixToDMap(Map<String,Double> oldMap, String prefix){
		Map<String,Double> prefixedMap = new HashMap<String,Double>();
		for( Entry<String, Double>  e: oldMap.entrySet()){
			prefixedMap.put(prefix + e.getKey(), e.getValue());
		}
		return prefixedMap;
	}
}
