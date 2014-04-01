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
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;

import config.Config;
import featextractors.counters.liwc.LIWCStringExtractor;

public class LIWCExpander {

	public static void main(String[] args) {
		String text = "This is a very reactive and vague doomsday proposal";
		LIWCStringExtractor e = new LIWCStringExtractor();
		Map<String, Integer> v = e.extractFeaturesForObject(text);
		for (Entry<String, Integer> ev : v.entrySet()) {
			System.out.println(ev);
		}
	}

	public void expand() {
		try {
			PrintWriter w = new PrintWriter(new FileWriter(new File(
					Config.getLIWCExpandedFile())));
			HashMap<String, String> patterns = readInWordFile(Config
					.getLiwcFile());
			Set<String> dict = readInDictFile(Config.getUnigramFile());
			for (Entry<String, String> pattern : patterns.entrySet()) {
				String regex = pattern.getKey();
				String category = pattern.getValue();
				if (regex.contains("*")) {
					regex = regex.replaceAll("\\*", "");
					Pattern p = Pattern.compile(regex + ".*");
					boolean matched = false;
					for (String word : dict) {
						Matcher m = p.matcher(word);
						if (m.matches()) {
							w.println(m.group() + "," + category);
							matched = true;
						}
					}
					if (!matched)
						w.println(regex + "," + category);
				} else {
					w.println(regex + "," + category);
				}
			}

			w.flush();
			w.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Set<String> readInDictFile(String file) {
		Set<String> dict = new HashSet<String>();
		try {
			BufferedReader b = new BufferedReader(
					new FileReader(new File(file)));
			String line = "";
			while ((line = b.readLine()) != null) {
				String[] data = line.split("\t");
				dict.add(data[0].toLowerCase().trim());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dict;
	}

	public HashMap<String, String> readInWordFile(String file) {
		HashMap<String, String> liwcPatterns = new HashMap<String, String>();
		try {
			BufferedReader b = new BufferedReader(
					new FileReader(new File(file)));
			String line = "";
			while ((line = b.readLine()) != null) {
				String[] data = line.split(",");
				if (data.length == 2) {
					String startingPattern = data[0].trim();
					String type = data[1];
					liwcPatterns.put(startingPattern, type);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return liwcPatterns;
	}
}
