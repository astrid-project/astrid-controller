package it.polito.astrid.controllers.test;

import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import it.polito.verefoo.astrid.jaxb.InfrastructureEvent;
import it.polito.verefoo.astrid.jaxb.InfrastructureInfo;
import it.polito.verefoo.astrid.jaxb.InfrastructureEvent.EventData;
import it.polito.verefoo.astrid.jaxb.InfrastructureInfo.Metadata;
import it.polito.verefoo.astrid.jaxb.InfrastructureInfo.Spec;
import it.polito.verefoo.astrid.jaxb.InfrastructureInfo.Spec.Node;

public class ServicesMethods {
	
	protected String getInfrastructureInfoXMLFormat() {
		InfrastructureInfo info = new InfrastructureInfo();
		Metadata meta = new Metadata();
		Spec spec = new Spec();
		Node node1 = new Node();
		Node node2 = new Node();
		meta.setName("my-graph");
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date;
		try {
			date = format.parse("2019-05-06 13:13:37.37");
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);
			XMLGregorianCalendar xmlGregCal =  DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
			meta.setLastUpdate(xmlGregCal);
		} catch (ParseException | DatatypeConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		info.setMetadata(meta);
		
		// Create one node of Spec
		node1.setIp("192.168.122.171");
		
		// Create second node of Spec
		node2.setIp("192.168.122.93");
		
		//insert this node in Spec
		spec.getNode().add(node1);
		spec.getNode().add(node2);
		
		info.setSpec(spec);
		
		Marshaller marshaller=null;
		JAXBContext jaxbContext;
		StringWriter sw = new StringWriter();
		try {
			jaxbContext = JAXBContext.newInstance(InfrastructureInfo.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(info, sw);
			return sw.toString();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	protected String getInfrastructureEventXMLFormat() {
		InfrastructureEvent event = new InfrastructureEvent();
		EventData ed = new EventData();
		
		event.setType("delete");
		
		ed.setResourceType("pod");
		ed.setName("apache");
		ed.setIp("172.20.1.241");
		ed.setUid("apache-c7d97955-2dtf6");
		
		event.setEventData(ed);
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date;
		try {
			date = format.parse("2019-06-28 13:55:01.55");
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);
			XMLGregorianCalendar xmlGregCal =  DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
			event.setEventTime(xmlGregCal);
		} catch (ParseException | DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Marshaller marshaller=null;
		JAXBContext jaxbContext;
		StringWriter sw = new StringWriter();
		try {
			jaxbContext = JAXBContext.newInstance(InfrastructureEvent.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(event, sw);
			return sw.toString();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	protected String getAttackEventXMLFormat() {
		InfrastructureEvent event = new InfrastructureEvent();
		EventData ed = new EventData();
		
		event.setType("attack");
		
		ed.setResourceType("pod");
		ed.setName("apache");
		ed.setIp("172.20.1.241");
		ed.setUid("apache-c7d97955-2dtf6");
		
		event.setEventData(ed);
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date;
		try {
			date = format.parse("2019-06-28 13:55:01.55");
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);
			XMLGregorianCalendar xmlGregCal =  DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
			event.setEventTime(xmlGregCal);
		} catch (ParseException | DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Marshaller marshaller=null;
		JAXBContext jaxbContext;
		StringWriter sw = new StringWriter();
		try {
			jaxbContext = JAXBContext.newInstance(InfrastructureEvent.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(event, sw);
			return sw.toString();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
