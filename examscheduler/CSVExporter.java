package examscheduler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVWriter;

public class CSVExporter {
	
	private static final String OUTPUT_PATH = "./output/";
	
	public void exportCSV(Scheduler scheduler, int index) {	
		try {
			FileWriter fileWriter = new FileWriter(new File(OUTPUT_PATH + "result" + index + ".csv"));
			CSVWriter csvWriter = new CSVWriter(fileWriter, ',', 
					CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
			
			writeToCSV(scheduler, csvWriter);
			csvWriter.close();
			
		} catch (IOException e) {
			System.out.println("Invalid file");
			e.printStackTrace();
		}
	}

	private void writeToCSV(Scheduler scheduler, CSVWriter writer) {
		for (int day = 1; day <= scheduler.getDuration(); day++) {
			String[] currentLine = new String[1];
			StringBuilder sb = new StringBuilder();
			sb.append("Dan " + day + ",");
			for (Auditorium auditorium : scheduler.getAuditoriums()) {
				sb.append(auditorium.getName() + ",");
			}
			sb.deleteCharAt(sb.length() - 1);
			currentLine[0] = sb.toString();
			writer.writeNext(currentLine);
			for (String examTime : Scheduler.examTimes) {
				sb = new StringBuilder();
				sb.append(examTime + ",");
				for (Auditorium auditorium : scheduler.getAuditoriums()) {
					Exam exam = findExam(scheduler.getExamDomains(), day, examTime, auditorium);
					if (exam != null) {
						sb.append(exam.getCode() + ",");
					} else {
						sb.append("X,");
					}
				}
				sb.deleteCharAt(sb.length() - 1);
				currentLine[0] = sb.toString();
				writer.writeNext(currentLine);
			}
			writer.writeNext(new String[] {"\n"});
		}
	}

	private Exam findExam(Map<Exam, List<Domain>> examDomains, int day, String examTime, Auditorium auditorium) {
		for (Exam exam : examDomains.keySet()) {
			Domain domain = examDomains.get(exam).get(0);
			if (domain.getTime().equals(examTime) && domain.getDay() == day && domain.getSelectedAuditoriums().contains(auditorium)) {
				return exam;
			}
		}
		return null;
	}
}
