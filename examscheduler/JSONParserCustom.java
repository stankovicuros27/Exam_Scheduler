package examscheduler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import json.simple.JSONArray;
import json.simple.JSONObject;
import json.simple.parser.JSONParser;

public class JSONParserCustom {
	
	private BufferedReader openFile(String path) {
		try {
			File inputFile = new File(path);
			return new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), Charset.forName("UTF-8")));
		} catch (FileNotFoundException e) {
			System.out.println("Invalid file");
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Auditorium> parseAuditoriums(String fileName) {
		List<Auditorium> auditoriums = new ArrayList<>();
		JSONParser parser = new JSONParser();
		try {
			JSONArray array = (JSONArray)parser.parse(openFile(fileName));
			for(Object o : array) {
				JSONObject auditorium = (JSONObject)o;
				String name = (String)auditorium.get("naziv");
				long capacity = (long)auditorium.get("kapacitet");
				long hasComputers = (long)auditorium.get("racunari");
				long requiredStaff = (long)auditorium.get("dezurni");
				long belongsToETF = (long)auditorium.get("etf");
				auditoriums.add(new Auditorium(name, Math.toIntExact(capacity), hasComputers == 1, Math.toIntExact(requiredStaff), belongsToETF == 1));
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return auditoriums;
	}
	
	@SuppressWarnings("unchecked")
	public List<Exam> parseExams(String path, Scheduler scheduler) {
		List<Exam> exams = new ArrayList<>();
		JSONParser parser = new JSONParser();
		try {
			JSONObject obj = (JSONObject)parser.parse(openFile(path));
			
			long duration = (long)obj.get("trajanje_u_danima");
			scheduler.setDuration(Math.toIntExact(duration));
			
			JSONArray array = (JSONArray)obj.get("ispiti");
			for(Object o : array) {
				JSONObject exam = (JSONObject)o;
				String code = (String)exam.get("sifra");
				long studentsAttendingExam = (long)exam.get("prijavljeni");
				long requiresComputers = (long)exam.get("racunari");
				
				List<String> departmentsList = new ArrayList<>();
				JSONArray departments = (JSONArray)exam.get("odseci");
				for(Object o1 : departments) {
					departmentsList.add((String)o1);
				}
				exams.add(new Exam(code, Math.toIntExact(studentsAttendingExam), requiresComputers == 1, departments));
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return exams;
	}
}
