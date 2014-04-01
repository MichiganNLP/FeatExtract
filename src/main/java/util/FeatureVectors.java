package util;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class FeatureVectors implements Serializable{
	private static final long serialVersionUID = 1L;
	public List<Map<String,Object>> instances;
	public String className;

	public FeatureVectors(List<Map<String,Object>> vecs, String c) {
		instances = vecs;
		className = c;
	}

	@Override
	public String toString() {
		return "FeatureVectors [instances=" + instances + ", className="
				+ className + "]";
	}
	
	


}