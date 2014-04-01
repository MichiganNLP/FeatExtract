import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomBaseLine {
	public List<String> countsList = new ArrayList<String>();

	public static void main(String[] args) {

		RandomBaseLine b = new RandomBaseLine();

		b.load();
	}

	public void load() {
		File root = new File("/home/jmaxk/temp/fb/data");
		List<File> files = Arrays.asList(root.listFiles());
		files.forEach((f) -> parseFile2(f));
		int min = 0;
		int max = countsList.size();
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		System.out.println(countsList.get(randomNum));
	}

	public void parseFile2(File f) {

		try {
			String name = f.getName().replaceAll(".txt", "");
			BufferedReader b = new BufferedReader(new FileReader(f));
			b.lines().parallel().forEach((line) -> countsList.add(name));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
