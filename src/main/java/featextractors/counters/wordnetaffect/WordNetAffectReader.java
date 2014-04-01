package featextractors.counters.wordnetaffect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import config.Config;

import max.nlp.wrappers.stanford.StanfordNLP;
import featextractors.counters.Counter;

public class WordNetAffectReader extends Counter {

	private static WordNetAffectReader self = null;
	private static StanfordNLP stanford = StanfordNLP.getInstance();

	private static HashMap<String, String> wordMap = new HashMap<String, String>();
	
	
	private WordNetAffectReader() {
		readInWordFile(Config.getWnaFile());
	}

	public static WordNetAffectReader getInstance() {
		if (self == null)
			self = new WordNetAffectReader();
		return self;
	}
	
	public void readInWordFile(String file) {
		try {
			BufferedReader b = new BufferedReader(
					new FileReader(new File(file)));
			String line = "";
			while ((line = b.readLine()) != null) {
				String[] data = line.split(",");
				if (data.length == 2) {
					String word = data[0].trim().replaceAll("\\*",
							"");
					String sentiment = data[1].replaceAll(" ", "_");
					wordMap.put(word, sentiment);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public HashMap<String, Integer> getCategoryCountsOfText(String text) {
		HashMap<String, Integer> counts = new HashMap<String, Integer>();
		List<String> words = stanford.convertTextToTokens(text);
			for (String word : words) {
			String matchingCategory = wordMap.get(word.toLowerCase());
				if (matchingCategory != null) { // returns null if no match
					Integer countForCategory = counts.get(matchingCategory);
					if (countForCategory == null)
						counts.put(matchingCategory, 1);
					else
						counts.put(matchingCategory, countForCategory + 1);
				}
			}
		
		return counts;
	}
}
