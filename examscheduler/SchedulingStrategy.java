package examscheduler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SchedulingStrategy {
	
	protected Map<Exam, List<Domain>> examDomains;
	
	/*private void prepareDomains(Exam exam) {
		List<Domain> domains = examDomains.get(exam);
		List<Domain> newDomains = new ArrayList<>();
		
		if (exam.isRequiresComputers()) {
			for (Domain domain : domains) {
				boolean add = true;
				for (Auditorium aud : domain.getSelectedAuditoriums()) {
					
				}
			}
		}
	}*/
	
	void removeByDomainExcept(Domain toExclude, Exam exceptionExam) {
		for (Exam exam : examDomains.keySet()) {
			if (exam == exceptionExam) {
				continue;
			}
			List<Domain> newDomains = new ArrayList<>();
			for (Domain domain : examDomains.get(exam)) {
				if (!Domain.domainsOverlap(domain, toExclude) /*&& !(Exam.sameDepartment(exceptionExam, exam) && domain.getDay() == toExclude.getDay())*/) {
					newDomains.add(domain);
				}
			}
			examDomains.put(exam, newDomains);
		}
	}
	
	boolean pickDomain(Exam exam, Domain domain) {
		if (domain.calculateCapacity() >= exam.getStudentsAttendingExam() && (!exam.isRequiresComputers() || (exam.isRequiresComputers() && domain.hasComputers()))) {
			examDomains.get(exam).clear();
			examDomains.get(exam).add(domain);
			return true;
		}
		return false;
	}
	
	public abstract void schedule(Scheduler scheduler);
}



class MostAttendenceFirst extends SchedulingStrategy {
	
	private List<Exam> exams;
	
	public void schedule(Scheduler scheduler) {
		Comparator<Exam> comp = (Exam a, Exam b) -> {
		    return Double.compare(b.getStudentsAttendingExam(), a.getStudentsAttendingExam());
		};
		exams = new ArrayList<>(scheduler.getExams());
		exams.sort(comp);
		
		List<Domain> allDomains = Domain.generateAllDomains(scheduler.getDuration(), scheduler.getAuditoriums());	
		examDomains = new HashMap<>();
		
		for (Exam exam : exams) {
			examDomains.put(exam, new ArrayList<>(allDomains));
		}
		
		boolean scheduled = scheduleUtil(0);
		
		if (scheduled) {
			boolean ok = true;
			for (Exam exam : examDomains.keySet()) {
				if (examDomains.get(exam).size() != 1) {
					System.out.println("NOT OK, " + examDomains.get(exam).size());
					ok = false;
					break;
				}
			}
			if (ok) {
				System.out.println(" OK ");
			}
		}
	}

	private boolean scheduleUtil(int examNumber) {
		
		Map<Exam, List<Domain>> oldDomains = new HashMap<>(examDomains);
		Exam currentExam = exams.get(examNumber);
		List<Domain> possibleDomainsOld = new ArrayList<>(examDomains.get(currentExam));
		
		for (int i = 0; i < possibleDomainsOld.size(); i++) {
			examDomains = new HashMap<>(oldDomains);
			boolean ok = pickDomain(currentExam, possibleDomainsOld.get(i));
			if (ok) {
				removeByDomainExcept(possibleDomainsOld.get(i), currentExam);
				if (examNumber == exams.size() - 1) {
					return true;
				}
				boolean ret = scheduleUtil(examNumber + 1);
				if (ret) {
					return true;
				}
			}
		}
		
		examDomains = new HashMap<>(oldDomains);
		return false;
	}
}

