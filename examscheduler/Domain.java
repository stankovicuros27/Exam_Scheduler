package examscheduler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Domain {
	private int day;
	private String time;
	private ArrayList<Auditorium> selectedAuditoriums;
	private static Set<List<Auditorium>> uniqueAuditoriumCombinations = null;
	
	public static List<Domain> generateAllDomains(int numberOfDays, final List<Auditorium> list) {
		List<Domain> allDomains = new ArrayList<>();
		List<List<Auditorium>> auditoriumCombinations = createAuditoriumCombinations(list);
		for (int day = 1; day <= numberOfDays; day++) {
			for (String time : Scheduler.examTimes) {
				for (List<Auditorium> selectedAuditoriums : auditoriumCombinations) {
					Domain domain = new Domain(day, time, new ArrayList<>(selectedAuditoriums));
					allDomains.add(domain);
				}
			}
		}
		
		List<Domain> allDomainsWithoutEmptyOnes = new ArrayList<>();
		for (Domain domain : allDomains) {
			if (!domain.getSelectedAuditoriums().isEmpty()) {
				allDomainsWithoutEmptyOnes.add(domain);
			}
		}
		allDomains = allDomainsWithoutEmptyOnes;
		
		Comparator<Domain> comp = (Domain a, Domain b) -> {
		    return Double.compare(a.calculateScore(), b.calculateScore());
		};
		allDomains.sort(comp);
		return allDomains;
	}
	
	private static List<List<Auditorium>> createAuditoriumCombinations(List<Auditorium> allAuditoriums) {
		uniqueAuditoriumCombinations = new HashSet<>();
		uniqueAuditoriumCombinations.add(new ArrayList<>());
		createAuditoriumCombinationsUtil(allAuditoriums, 0);
		List<List<Auditorium>> allCombinations = new ArrayList<>();
		for (List<Auditorium> list : uniqueAuditoriumCombinations) {
			allCombinations.add(list);
		}
		uniqueAuditoriumCombinations = null;
		return allCombinations;
	}
	
	private static void createAuditoriumCombinationsUtil(List<Auditorium> allAuditoriums, int position) {
		Set<List<Auditorium>> newCombinations = new HashSet<>();
		for (List<Auditorium> previousCombinations : uniqueAuditoriumCombinations) {
			List<Auditorium> newList = new ArrayList<>(previousCombinations);
			newList.add(allAuditoriums.get(position));
			newCombinations.add(newList);
		}
		uniqueAuditoriumCombinations.addAll(newCombinations);
		if (++position < allAuditoriums.size()) {
			createAuditoriumCombinationsUtil(allAuditoriums, position);
		}
	}
	
	public static boolean domainsOverlap(Domain d1, Domain d2) {
		if (d1.getDay() == d2.getDay() && d1.getTime() == d2.getTime()) {
			for (Auditorium aud : d1.selectedAuditoriums) {
				if (d2.selectedAuditoriums.contains(aud)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public int calculateCapacity() {
		int capacity = 0;
		for (Auditorium auditorium : selectedAuditoriums) {
			capacity += auditorium.getCapacity();
		}
		return capacity;
	}
	
	public int calculateRequiredStaff() {
		int requiredStaff = 0;
		for (Auditorium auditorium : selectedAuditoriums) {
			requiredStaff += auditorium.getRequiredStaff();
		}
		return requiredStaff;
	}
	
	public int calculateNumberOfAuditoriumsOutsideETF() {
		int outsideETF = 0;
		for (Auditorium auditorium : selectedAuditoriums) {
			outsideETF += auditorium.isBelongsToETF() == true ? 0 : 1;
		}
		return outsideETF;
	}
	
	public boolean hasComputers() {
		boolean ret = true;
		for (Auditorium auditorium : selectedAuditoriums) {
			if (!auditorium.isHasComputers()) {
				ret = false;
				break;
			}
		}
		return ret;
	}
	
	public double calculateScore() {
		return calculateRequiredStaff() + 1.2 * calculateNumberOfAuditoriumsOutsideETF();
	}

	public Domain(int day, String time, ArrayList<Auditorium> selectedAuditoriums) {
		super();
		this.day = day;
		this.time = time;
		this.selectedAuditoriums = selectedAuditoriums;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("~Domain~");
		sb.append(": Day(" + day + ")");
		sb.append(", Time(" + time + ")");
		sb.append("\nAuditoriums: ");
		for (Auditorium auditorium : selectedAuditoriums) {
			sb.append("\n" + auditorium);
		}
		return sb.toString();
	}
	
	public static void printAllDomains(List<Domain> domains) {
		for (Domain domain : domains) {
			System.out.println("\n" + domain);
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + day;
		result = prime * result + ((selectedAuditoriums == null) ? 0 : selectedAuditoriums.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Domain other = (Domain) obj;
		if (day != other.day)
			return false;
		if (selectedAuditoriums == null) {
			if (other.selectedAuditoriums != null)
				return false;
		} else if (!selectedAuditoriums.equals(other.selectedAuditoriums))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		return true;
	}

	public int getDay() {
		return day;
	}
	public String getTime() {
		return time;
	}
	public ArrayList<Auditorium> getSelectedAuditoriums() {
		return selectedAuditoriums;
	}
}
