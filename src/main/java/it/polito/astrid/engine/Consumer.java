package it.polito.astrid.engine;


import it.polito.astrid.models.InterceptionRequest;
import it.polito.astrid.service.DroolsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class Consumer {
    @Autowired
    private DroolsService droolsService;

    private final Logger logger = LoggerFactory.getLogger(Consumer.class);

    @KafkaListener(topics = "interception", groupId = "group_id")
    public void consume(InterceptionRequest message) throws IOException {
        logger.info(String.format("#### -> Consumed message -> %s", message));
        /** Send data to Drools to invoke communication channels
         * **/
        droolsService.sendCommunication(message.getCommand());

    }
}