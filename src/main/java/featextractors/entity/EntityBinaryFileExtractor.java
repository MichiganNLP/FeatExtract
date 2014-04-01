package featextractors.entity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

import max.nlp.wrappers.stanford.StanfordNER;

public class EntityBinaryFileExtractor  extends EntityExtractor<File>{

	private static StanfordNER ner = StanfordNER.getInstance();

	public HashMap<String, Integer> extractFeaturesForObject(File item) {
	
		HashMap<String, Integer> entityBinaryCounts = new HashMap<String,Integer>();
		try {
			String content = new Scanner(item).useDelimiter("\\Z").next();
			HashMap<String, List<String>> entities = ner
					.extractEntities(content);
			for(Entry<String, List<String>>  entititiesOfType : entities.entrySet()){
				String category = entititiesOfType.getKey();
				for(String entity : entititiesOfType.getValue()){
					String ent = category + "_" + entity;
					entityBinaryCounts.put(getPrefix() + ent, 1);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return entityBinaryCounts;
	}

}
