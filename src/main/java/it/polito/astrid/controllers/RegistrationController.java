package it.polito.astrid.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import it.polito.astrid.beans.Element;
import it.polito.astrid.beans.StudentRegistration;
import it.polito.astrid.jaxb.InfrastructureInfo;
import it.polito.astrid.beans.RegistrationReply;

@Controller
public class RegistrationController {
  @RequestMapping(method = RequestMethod.POST, value="/register/insfrastructure")
  @ResponseBody
  public RegistrationReply registerStudent(@RequestBody InfrastructureInfo info) {
  System.out.println("In registerStudent");
        RegistrationReply stdregreply = new RegistrationReply();           
		/*
		 * StudentRegistration.getInstance().add(student); //We are setting the below
		 * value just to reply a message back to the caller
		 * stdregreply.setName(student.getName()); stdregreply.setAge(student.getAge());
		 * stdregreply.setRegistrationNumber(student.getRegistrationNumber());
		 */
        stdregreply.setRegistrationStatus("Successful");
        return stdregreply;
}
}