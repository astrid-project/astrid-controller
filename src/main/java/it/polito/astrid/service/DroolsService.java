package it.polito.astrid.service;



import it.polito.astrid.models.InterceptionRequest;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DroolsService {

    @Autowired
    private KieContainer kieContainer;

    public void sendCommunication(String command) {
        KieSession kieSession = kieContainer.newKieSession();
        //kieSession.setGlobal("kafkaSms", kafkaSms);
        //kieSession.setGlobal("kafkaEmail", kafkaEmail);
        kieSession.insert(command);
        kieSession.fireAllRules();
        kieSession.dispose();
    }
}