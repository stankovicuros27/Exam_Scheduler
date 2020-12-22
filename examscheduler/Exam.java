package examscheduler;

import java.util.ArrayList;

public class Exam {
	private String code;
	private int studentsAttendingExam;
	private boolean requiresComputers;
	private ArrayList<String> departments = new ArrayList<>();
		
	public Exam(String code, int studentsAttendingExam, boolean requiresComputers, ArrayList<String> departments) {
		super();
		this.code = code;
		this.studentsAttendingExam = studentsAttendingExam;
		this.requiresComputers = requiresComputers;
		this.departments = departments;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Code (" + code + "), Attendence (" + studentsAttendingExam + "), Requires Computers (" + requiresComputers + 
				"), Departments (");
		for (String department : departments) {
			sb.append(department + " ");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");
		return sb.toString();
	}
	
	public static boolean sameDepartment(Exam e1, Exam e2) {
		for (String department : e1.getDepartments()) {
			if (e2.getDepartments().contains(department)) {
				return true;
			}
		}
		return false;
	}

	public String getCode() {
		return code;
	}

	public int getStudentsAttendingExam() {
		return studentsAttendingExam;
	}

	public boolean isRequiresComputers() {
		return requiresComputers;
	}

	public ArrayList<String> getDepartments() {
		return departments;
	}
}
