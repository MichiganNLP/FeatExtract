package featextractors.counters.expandedliwc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import max.nlp.wrappers.stanford.StanfordNLP;
import config.Config;
import featextractors.counters.Counter;

public class ExpandedLIWCReader extends Counter {

	private static ExpandedLIWCReader self = null;

	/*
	 * Each key is a pattern (using Kleene stars), and each value is a LIWC
	 * pattern
	 */
	private static HashMap<String, String> liwcPatterns = new HashMap<String, String>();
	private static StanfordNLP stanford = StanfordNLP.getInstance();
	private HashSet<String> liwcCategories = new HashSet<String>();

	public static HashMap<String, String> getLiwcPatterns() {
		return liwcPatterns;
	}

	private ExpandedLIWCReader() {
		readInWordFile(Config.getLIWCExpandedFile());
	}

	public static ExpandedLIWCReader getInstance() {
		if (self == null)
			self = new ExpandedLIWCReader();
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
					String startingPattern = data[0].trim().replaceAll("\\*",
							"");
					String type = data[1];
					liwcPatterns.put(startingPattern, type);
					liwcCategories.add(type);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param word
	 *            , must be lowercase
	 * @return null, or the matching LIWC pattern
	 */
	public String findCategory(String word) {
		for (Entry<String, String> liwcEntry : liwcPatterns.entrySet()) {
			String prefix = liwcEntry.getKey();
			if (word.startsWith(prefix))
				return liwcEntry.getValue();
		}
		return null;
	}

	public HashMap<String, Integer> getCategoryCountsOfText(String text) {
		HashMap<String, Integer> counts = new HashMap<String, Integer>();
		List<String> words = stanford.convertTextToTokens(text);
		for (String word : words) {
			String matchingCategory = findCategory(word);
			if (matchingCategory != null) { // returns null if no match
				System.out.println(word + " " + matchingCategory);

				Integer countForCategory = counts.get(matchingCategory);
				if (countForCategory == null)
					counts.put(matchingCategory, 1);
				else
					counts.put(matchingCategory, countForCategory + 1);
			}
		}

		return counts;
	}

	public void filter(List<String> allowedTags, String output) {
		try {
			PrintWriter w = new PrintWriter(new FileWriter(output));
			for (Entry<String, String> entry : liwcPatterns.entrySet()) {
				String word = entry.getKey();
				String cat = entry.getValue();
				if (allowedTags.contains(cat))
					w.println(word + ", " + cat);
				else
					System.out.println(word + ", " + cat);
			}

			w.flush();
			w.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
