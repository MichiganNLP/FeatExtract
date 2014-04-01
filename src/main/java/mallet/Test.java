package mallet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import cc.mallet.classify.Classifier;
import cc.mallet.classify.Trial;
import cc.mallet.classify.evaluate.ConfusionMatrix;
import cc.mallet.types.InstanceList;

public class Test {

	public static Classifier loadClassifier(File serializedFile)
			throws FileNotFoundException, IOException, ClassNotFoundException {

		// The standard way to save classifiers and Mallet data
		// for repeated use is through Java serialization.
		// Here we load a serialized classifier from a file.

		Classifier classifier;

		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
				serializedFile));
		classifier = (Classifier) ois.readObject();
		ois.close();

		return classifier;
	}	

	public static void main(String[] args) {  
		try {
			String cFile = "/home/jmaxk/temp/models/ngram.model";
			String instFile = "/home/jmaxk/temp/models/ngram.test.vectors";
			InstanceList instances = InstanceList.load(new File(instFile));
			System.out.println(instances.size());
			Classifier c = loadClassifier(new File(cFile));

			Trial t = new Trial(c, instances);
			System.out.println(t.getAccuracy());
			ConfusionMatrix matrix = new ConfusionMatrix(t);
			for (int i = 0; i < 51; i++) {
				double r = t.getF1(i);
				System.out.println(r);
			}
			System.out.println(matrix);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
