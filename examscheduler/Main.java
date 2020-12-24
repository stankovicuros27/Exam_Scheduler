package examscheduler;

public class Main {
	
	private static final String SIMULATION_PATH = "./javni_testovi/";

	public static void main(String[] args) {
		Scheduler scheduler = Scheduler.getInstance();
		for (int i = 1; i <= 1; i++) {
			System.out.print(i + ": ");
			scheduler.loadAuditoriums(SIMULATION_PATH + "sale" + 4 + ".json");
			scheduler.loadExams(SIMULATION_PATH + "rok" + 3 + ".json");
			try {
				scheduler.scheduleExams();
				scheduler.printScheduledExams();
				CSVExporter csvExporter = new CSVExporter();
				csvExporter.exportCSV(scheduler, i);
			} catch (Exception e) {
				System.out.print("Cannot schedule exams!");
			}
		}
		
		/*for (int i = 1; i <= 5; i++) {
			for (int j = 1; j <= 5; j++) {
				System.out.print("Sale " + i + "," + " Ispiti " + j + ": ");
				scheduler.loadAuditoriums(SIMULATION_PATH + "sale" + i + ".json");
				scheduler.loadExams(SIMULATION_PATH + "rok" + j + ".json");
				try {
					scheduler.scheduleExams();
					//scheduler.printScheduledExams();
					CSVExporter csvExporter = new CSVExporter();
					csvExporter.exportCSV(scheduler, i);
				} catch (Exception e) {
					System.out.print("Cannot schedule exams!");
				}
				System.out.println();
			}
		}	*/	
	}
}
