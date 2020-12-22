package examscheduler;

import java.util.List;

public class Main {
	
	private static final String SIMULATION_PATH = "./javni_testovi/";

	public static void main(String[] args) {
		Scheduler scheduler = Scheduler.getInstance();
		for (int i = 1; i <= 5; i++) {
			scheduler.loadAuditoriums(SIMULATION_PATH + "sale" + i + ".json");
			scheduler.loadExams(SIMULATION_PATH + "rok" + i + ".json");
			scheduler.scheduleExams();
			System.out.println(i);
		}		
	}
}
