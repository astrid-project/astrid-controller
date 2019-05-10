package it.polito.astrid.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import it.polito.astrid.beans.StudentRegistration;
import it.polito.astrid.beans.Element;

@Controller
public class RetrieveController {
  @RequestMapping(method = RequestMethod.GET, value="/student/allstudent")
  @ResponseBody
  public List<Element> getAllStudents() {
	  return StudentRegistration.getInstance().getStudentRecords();
  }
}