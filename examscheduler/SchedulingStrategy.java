package examscheduler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SchedulingStrategy {
	
	protected Map<Exam, List<Domain>> examDomains;
	protected List<Exam> exams;
	
	public void removeInvalidDomains() {
		for (Exam exam : examDomains.keySet()) {
			List<Domain> domains = examDomains.get(exam);
			List<Domain> validDomains = new ArrayList<>();
			for (Domain domain : domains) {
				if (exam.canFitInDomain(domain)) {
					validDomains.add(domain);
				}
			}
			examDomains.put(exam, validDomains);
		}
	}
	
	public void sortDomains() {
		for (Exam exam : examDomains.keySet()) {
			Comparator<Domain> comp;
			if (exam.isRequiresComputers()) {
				comp = (Domain a, Domain b) -> {
				    return Integer.compare(a.calculateCapacityWithComputers(), b.calculateCapacityWithComputers());
				};
			} else {
				comp = (Domain a, Domain b) -> {
				    return Integer.compare(a.calculateCapacity(), b.calculateCapacity());
				};
			}
			
			List<Domain> domains = examDomains.get(exam);
			domains.sort(comp);
		}
	}
	
	public void schedule(Scheduler scheduler) throws Exception {
		exams = new ArrayList<>(scheduler.getExams());
		sortExams();
		List<Domain> allDomains = Domain.generateAllDomains(scheduler.getDuration(), scheduler.getAuditoriums());
		examDomains = new HashMap<>();	// always create new examDomain map
		for (Exam exam : exams) {
			examDomains.put(exam, new ArrayList<>(allDomains));
		}
		removeInvalidDomains();			// remove domains with capacity less than desired
		sortDomains();
		boolean scheduled = scheduleUtil(0);
		
		if (!scheduled) {
			throw new Exception("Cannot schedule!");
		} else {
			System.out.println("desibrabo");
			System.out.println("ok");
		}
		
		scheduler.setExamDomains(examDomains);
	}
	
	private boolean scheduleUtil(int examNumber) throws Exception {
		long start = System.currentTimeMillis();

		Map<Exam, List<Domain>> oldDomains = new HashMap<>(examDomains);	// a way to remember old domains, for backtrack
		
		Exam currentExam = exams.get(examNumber);
		List<Domain> possibleDomains = examDomains.get(currentExam);
		
		while (!possibleDomains.isEmpty()) {
			for (int nextExamIndex = examNumber + 1; nextExamIndex < exams.size(); nextExamIndex++) {
				Exam nextExam = exams.get(nextExamIndex);
				examDomains.put(nextExam, oldDomains.get(nextExam));
			}
			if (currentExam.canFitInDomain(possibleDomains.get(0))) {
				removeByDomainWithException(possibleDomains.get(0), currentExam);	// removes overlaping domains from other exams
				if (examNumber == exams.size() - 1) {
					Domain actualDomain = possibleDomains.get(0);
					possibleDomains.clear();
					possibleDomains.add(actualDomain);
					return true;
				}
				boolean ret = scheduleUtil(examNumber + 1);
				if (ret) {
					return true;
				}
			}
			possibleDomains.remove(0);
			
			long finish = System.currentTimeMillis();
			long timeElapsed = finish - start;
			//if (timeElapsed > 3000) {
				//throw new Exception("Cannot schedule!");
			//}
		}

		//System.out.println(examNumber);
		examDomains = new HashMap<>(oldDomains);
		return false;
	}
	
	void removeByDomainWithException(Domain toExclude, Exam exceptionExam) {
		for (Exam exam : examDomains.keySet()) {
			if (exam == exceptionExam) {
				continue;
			}
			List<Domain> newDomains = new ArrayList<>();
			for (Domain domain : examDomains.get(exam)) {
				if (!Domain.domainsOverlap(domain, toExclude) && !(Exam.sameDepartmentAndYear(exceptionExam, exam) && domain.getDay() == toExclude.getDay())) {
					newDomains.add(domain);
				}
			}
			examDomains.put(exam, newDomains);
		}
	}
	
	public abstract void sortExams();
}

class MostAttendenceFirst extends SchedulingStrategy {
	@Override
	public void sortExams() {
		Comparator<Exam> comp = (Exam a, Exam b) -> {
		    return Integer.compare(b.getStudentsAttendingExam(), a.getStudentsAttendingExam());
		};
		exams.sort(comp);
	}
}

