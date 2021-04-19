package it.polito.astrid.service;


import it.polito.astrid.models.InterceptionRequest;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.conf.EventProcessingOption;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import it.polito.verefoo.astrid.jaxb.Components.*;
import it.polito.astrid.models.KafkaCounter;

@Service
public class DroolsService {
	private  static  final String drlFile = "astrid-config.drl";
	private  static  final String DecisionTableFile = "interception.xls";
    
    private final KieContainer kieContainer;
    private final KafkaCounter numberKafka;
    
    @Autowired
	public DroolsService() {
		this.kieContainer = kieContainer();
		numberKafka = new KafkaCounter();
	}
    
    /*@Bean
	public KieContainer kieContainer() {
		return KieServices.Factory.get().getKieClasspathContainer();
	}*/
    
    @Bean
    private KieContainer kieContainer(){
        KieServices kieServices = KieServices.Factory.get();

        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(ResourceFactory.newClassPathResource(drlFile, getClass()));
        
        /*Cristian modify*/
        KieModuleModel kieModuleModel = kieServices.newKieModuleModel();
        KieBaseModel baseModel = kieModuleModel.newKieBaseModel("defaultKBase")
            .setDefault(true)
            .setEventProcessingMode(EventProcessingOption.STREAM);
        baseModel.newKieSessionModel("defaultKSession")
             .setDefault(true);
        kieFileSystem.writeKModuleXML(kieModuleModel.toXML());
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
    	kieBuilder.buildAll();
    	KieModule kieModule = kieBuilder.getKieModule();
        /*End Cristian modify*/
        
        /*KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        KieModule kieModule = kieBuilder.getKieModule();*/

        return kieServices.newKieContainer(kieModule.getReleaseId());
    }

    public void sendCommunication(String command) {
        KieSession kieSession = kieContainer.newKieSession();
        //kieSession.setGlobal("kafkaSms", kafkaSms);
        //kieSession.setGlobal("kafkaEmail", kafkaEmail);
        kieSession.insert(command);
        kieSession.fireAllRules();
        kieSession.dispose();
    }
    
    public void sendInterceptionRequest(InterceptionRequest IR, Component COM, Component COM1, Component COM2) {
    	KieSession kieSession = kieContainer.newKieSession();
        //kieSession.setGlobal("kafkaSms", kafkaSms);
        //kieSession.setGlobal("kafkaEmail", kafkaEmail);
        if (IR.getCommand().equals("kafka")){
        	kieSession.insert(IR.getMess());
        	kieSession.insert(COM);
        	kieSession.insert(numberKafka);
        }else{
        	kieSession.insert(IR);
    		kieSession.insert(COM);
    		if (COM1 != null)
    			kieSession.insert(COM1);
    		if (COM2 != null)
    			kieSession.insert(COM2);
        }
        kieSession.fireAllRules();
        kieSession.dispose();
    }
}