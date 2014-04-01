package featextractors.counters.ngram;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.shingle.ShingleAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

public class NGramExtractor {

	
	Analyzer analyzer;
	String text = "";
	Boolean stopWords = true;
	Boolean overlap = false;
	int length = 3;

	// We store ngrams, unique ngrams, and the frequencies of the ngrams
	LinkedList<String> nGrams;
	LinkedList<String> uniqueNGrams;
	HashMap<String, Integer> nGramFreqs;

	/**
	 * Default constructor. Initializes the ngram extractor
	 */
	public NGramExtractor() {

	}

	/**
	 * Extracts NGrams from a String of text. Can handle ngrams of any length
	 * and also perform stop word removal before extraction
	 * 
	 * @param text
	 *            the text that the ngrams should be extracted from
	 * @param length
	 *            the length of the ngrams
	 */
	public void extract(String text, int length) throws FileNotFoundException,
			IOException {
		extract(text, length, this.stopWords, this.overlap);
	}

	/**
	 * Extracts NGrams from a String of text. Can handle ngrams of any length
	 * and also perform stop word removal before extraction
	 * 
	 * @param text
	 *            the text that the ngrams should be extracted from
	 * @param length
	 *            the length of the ngrams
	 * @param stopWords
	 *            whether or not stopwords should be removed before extraction
	 */
	public void extract(String text, int length, Boolean stopWords)
			throws FileNotFoundException, IOException {
		extract(text, length, stopWords, this.overlap);
	}

	/**
	 * Extracts NGrams from a String of text. Can handle ngrams of any length
	 * and also perform stop word removal before extraction
	 * 
	 * @param text
	 *            the text that the ngrams should be extracted from
	 * @param length
	 *            the length of the ngrams
	 * @param stopWords
	 *            whether or not stopwords should be removed before extraction
	 * @param overlap
	 *            whether or not the ngrams should overlap
	 */
	@SuppressWarnings("deprecation")
	public void extract(String text, int length, Boolean stopWords,
			Boolean overlap) throws FileNotFoundException, IOException {

		this.text = text;
		this.length = length;
		this.stopWords = stopWords;
		this.overlap = overlap;

		nGrams = new LinkedList<String>();
		uniqueNGrams = new LinkedList<String>();
		nGramFreqs = new HashMap<String, Integer>();

		/*
		 * If the minLength and maxLength are both 1, then we want unigrams Make
		 * use of a StopAnalyzer when stopwords should be removed Make use of a
		 * SimpleAnalyzer when stop words should be included
		 */
		if (length == 1) {
			if (this.stopWords) {
				analyzer = new StandardAnalyzer(Version.LUCENE_36);
			} else {
				analyzer = new SimpleAnalyzer(Version.LUCENE_36);
			}
		} else { // Bigger than unigrams so use ShingleAnalyzerWrapper. Once
					// again, different analyzers depending on stop word removal
			if (this.stopWords) {
				analyzer = new ShingleAnalyzerWrapper(new StopAnalyzer(
						Version.LUCENE_CURRENT), length, length, " ", false,
						false);
			} else {
				analyzer = new ShingleAnalyzerWrapper(new SimpleAnalyzer(
						Version.LUCENE_36), length, length, " ", false, false);
			}
		}

		// Code to process and extract the ngrams
		TokenStream tokenStream = analyzer.tokenStream("text",
				new StringReader(this.text));
		CharTermAttribute charTermAttribute = tokenStream
				.addAttribute(CharTermAttribute.class);

		tokenStream.reset();
		while (tokenStream.incrementToken()) {

			String termToken = charTermAttribute.toString(); // The actual token
																// term
			nGrams.add(termToken); // Add all ngrams to the ngram LinkedList

			// If n-grams are not allowed to overlap, then increment to point of
			// no overlap
			if (!overlap) {
				for (int i = 0; i < length - 1; i++) {
					tokenStream.incrementToken();
				}
			}

		}

		// Store unique nGrams and frequencies in hash tables
		for (String nGram : nGrams) {
			if (nGramFreqs.containsKey(nGram)) {
				nGramFreqs.put(nGram, nGramFreqs.get(nGram) + 1);
			} else {
				nGramFreqs.put(nGram, 1);
				uniqueNGrams.add(nGram);
			}
		}

	}

	/**
	 * Returns the frequency of an ngram
	 * 
	 * @param ngram
	 *            the ngram whose frequency should be returned
	 * @return the frequency of the specified ngram
	 */
	public int getNGramFrequency(String ngram) {
		return nGramFreqs.get(ngram);
	}

	/**
	 * Returns all ngrams
	 * 
	 * @return all ngrams
	 */
	public LinkedList<String> getNGrams() {
		return nGrams;
	}

	/**
	 * Returns unique ngrams
	 * 
	 * @return the unique ngrams
	 */
	public LinkedList<String> getUniqueNGrams() {
		return uniqueNGrams;
	}

	/**
	 * Sets whether or not stopword should be removed from the text
	 * 
	 * @param stopWords
	 *            whether or not stopwords should be removed
	 */
	public void setStopWords(Boolean stopWords) {
		this.stopWords = stopWords;
	}

	/**
	 * Sets whether or not ngrams are allowed to overlap
	 * 
	 * @param overlap
	 *            whether or not ngrams are allowed to overlap
	 */
	public void setOverlap(Boolean overlap) {
		this.overlap = overlap;
	}

}
