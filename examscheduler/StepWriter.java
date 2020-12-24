package examscheduler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class StepWriter {
	
	private static final String OUTPUT_PATH = "./output/";
	private static final String PREFIX = "steps";
	private File outputFile;
	private PrintWriter myWriter;
	
	public StepWriter(int id) {
		outputFile = new File(OUTPUT_PATH + PREFIX + id + ".txt");
		try {
			outputFile.createNewFile();
			myWriter = new PrintWriter(new FileWriter(outputFile, true));
		} catch (IOException e) {
			System.out.println("File already exists");
			e.printStackTrace();
		}
	}
	
	public void addForwardStep(Exam exam, Domain domain) {
		myWriter.write(exam + "\n" + domain + "\n\n");
		myWriter.close();
	}
	
	public void addBacktrackStep(Exam exam) {
		myWriter.write("Backtrack @\n" + exam + "\n\n");
		myWriter.close();
	}
}
