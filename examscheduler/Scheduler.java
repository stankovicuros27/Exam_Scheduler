package examscheduler;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scheduler {
	
	private int duration;
	private List<Exam> exams;
	private List<Auditorium> auditoriums;
	private static Scheduler scheduler = null;
	static final String[] examTimes = {"08:00", "11:30", "15:00", "18:30"};
	private SchedulingStrategy strategy = null; 
	
	private Map<Exam, List<Domain>> examDomains = null;
	
	private Scheduler() {}
	
	public static Scheduler getInstance() {
		if (scheduler == null) {
			return (scheduler = new Scheduler());
		} else {
			return scheduler;
		}
	}
	
	public void scheduleExams() throws Exception {
		strategy = new MostAttendenceFirst();
		strategy.schedule(this);
	}
	
	public void loadExams(String path) {
		exams = new JSONParserCustom().parseExams(path, this);
	}
	
	public void loadAuditoriums(String path) {
		auditoriums = new JSONParserCustom().parseAuditoriums(path);
	}
	
	public void printScheduledExams() {
		for (Exam exam : examDomains.keySet()) {
			System.out.println(exam);
			System.out.println(examDomains.get(exam));
			System.out.println("\n\n\n");
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("	-~-~*EXAM SCHEDULER*~-~-");
		sb.append("\n\nDuration: " + duration + " day/s");
		sb.append("\n\nAuditoriums:");
		for (Auditorium auditorium : auditoriums) {
			sb.append("\n" + auditorium);
		}
		sb.append("\n\nExams:");
		for (Exam exam : exams) {
			sb.append("\n" + exam);
		}
		return sb.toString();
	}
	
	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public List<Exam> getExams() {
		return exams;
	}
	
	public List<Auditorium> getAuditoriums() {
		return auditoriums;
	}

	public Map<Exam, List<Domain>> getExamDomains() {
		return examDomains;
	}

	public void setExamDomains(Map<Exam, List<Domain>> examDomains) {
		this.examDomains = examDomains;
	}
}
