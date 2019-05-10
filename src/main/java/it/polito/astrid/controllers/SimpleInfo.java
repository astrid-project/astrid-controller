package it.polito.astrid.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import it.polito.astrid.Client.ClientToVerifoo;
import it.polito.astrid.beans.Element;
import it.polito.astrid.beans.StudentRegistration;
import it.polito.astrid.jaxb.InfrastructureInfo;
import it.polito.astrid.beans.RegistrationReply;

@Controller
public class SimpleInfo {
	
	//ClientToVerifoo clientVerifoo = new ClientToVerifoo(convertFileToString("graph.yml"));
  @RequestMapping(method = RequestMethod.GET, value="/register/infoVerifoo")
  @ResponseBody
  public String infoVerifoo() {
	  System.out.println("Info from Verifoot");
	  String all = StudentRegistration.getInstance().simpleGet();
	  System.out.println(all);
	  return all;
  }
  
  
}