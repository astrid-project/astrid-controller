package it.polito.astrid.beans;

import java.util.ArrayList;
import java.util.List;

public class StudentRegistration {
	private List<Element> studentRecords;
	private static StudentRegistration stdregd = null;

	private StudentRegistration() {
		studentRecords = new ArrayList<Element>();
	}

	public static StudentRegistration getInstance() {
		if (stdregd == null) {
			stdregd = new StudentRegistration();
			return stdregd;
		} else {
			return stdregd;
		}
	}

	public void add(Element std) {
		studentRecords.add(std);
	}

	public String upDateStudent(Element std) {
		for (int i = 0; i < studentRecords.size(); i++) {
			Element stdn = studentRecords.get(i);
			if (stdn.getRegistrationNumber().equals(std.getRegistrationNumber())) {
				studentRecords.set(i, std);// update the new record
				return "Update successful";
			}
		}
		return "Update un-successful";
	}

	public String deleteStudent(String registrationNumber) {
		for (int i = 0; i < studentRecords.size(); i++) {
			Element stdn = studentRecords.get(i);
			if (stdn.getRegistrationNumber().equals(registrationNumber)) {
				studentRecords.remove(i);// update the new record
				return "Delete successful";
			}
		}
		return "Delete un-successful";
	}

	public List<Element> getStudentRecords() {
		return studentRecords;
	}
}