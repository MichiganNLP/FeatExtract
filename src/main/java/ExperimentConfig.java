import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import weka.WekaFeatureBuilder;

import com.thoughtworks.xstream.XStream;

import featextractors.counters.liwc.LIWCStringExtractor;
import featextractors.counters.ngram.NGramStringExtractor;
import featextractors.counters.opinionfinder.OpinionFinderStringExtractor;
import featextractors.counters.roget.RogetStringExtractor;
import featextractors.counters.wordnetaffect.WNAStringExtractor;
import featextractors.tf.TFStringExtractor;
import featpruners.live.MoreThanN;
import featpruners.live.chisquared.ChiSquared;

public class ExperimentConfig {

	private String experimentDir, dataDir;
	private WekaFeatureBuilder<String> featureBuilder;
	private static XStream xstream;

	
	static {
		xstream = new XStream();
		xstream.alias("ngram-string", NGramStringExtractor.class);
		xstream.alias("liwc-string", LIWCStringExtractor.class);
		xstream.alias("roget-string", RogetStringExtractor.class);
		xstream.alias("opf-string", OpinionFinderStringExtractor.class);
		xstream.alias("tf-string", TFStringExtractor.class);

	}

	public String getExperimentDir() {
		return experimentDir;
	}

	public void setExperimentDir(String experimentDir) {
		this.experimentDir = experimentDir;
	}

	public String getDataDir() {
		return dataDir;
	}

	public void setDataDir(String dataDir) {
		this.dataDir = dataDir;
	}

	public WekaFeatureBuilder<String> getFeatureBuilder() {
		return featureBuilder;
	}

	public void setFeatureBuilder(WekaFeatureBuilder<String> featureBuilder) {
		this.featureBuilder = featureBuilder;
	}
 
	public ExperimentConfig(String experimentDir, String dataDir,
			WekaFeatureBuilder<String> featureBuilder) {
		this.experimentDir = experimentDir;
		this.dataDir = dataDir;
		this.featureBuilder = featureBuilder;

	}

	public static void main(String[] args) {
		WekaFeatureBuilder<String> builder = new WekaFeatureBuilder<String>();
//		builder.addFeatureExtractor(new NGramStringExtractor(3));
//		builder.addFeatureExtractor(new NGramStringExtractor(2));
//		builder.addFeatureExtractor(new NGramStringExtractor(1));
		builder.addFeatureExtractor(new TFStringExtractor(1, ""));

		builder.addFeaturePruner(new MoreThanN(5));
		ExperimentConfig c = new ExperimentConfig(
				"/home/jmaxk/classificationExperiments/fb/vecs-testmtn/",
				"/home/jmaxk/classificationExperiments/fb/data/", builder);
		c.write("/home/jmaxk/temp/tf-config.xml");

	}

	@Override
	public String toString() {
		return "ExperimentConfig [\nexperimentDir=" + experimentDir
				+ "\ndataDir=" + dataDir + "\nfeatureBuilder=" + featureBuilder
				+ "]";
	}

	public void write(String outputFile) {
		try {
			ObjectOutputStream out = xstream
					.createObjectOutputStream(new FileWriter(outputFile));
			out.writeObject(this);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ExperimentConfig fromFile(String file) {

		try {
			ObjectInputStream in = xstream
					.createObjectInputStream(new FileReader(file));
			ExperimentConfig c = (ExperimentConfig) in.readObject();
			return c;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

}
