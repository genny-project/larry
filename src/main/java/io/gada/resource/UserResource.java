package io.gada.resource;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import java.lang.invoke.MethodHandles;

@ApplicationScoped
public class UserResource {
    private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass());

//    @Channel("events")
//    Emitter<String> events;

    @Incoming("events")
    public void process(String event) throws InterruptedException {
//        Thread.sleep(200);
//        return new Quote(quoteRequest, random.nextInt(100));
        LOGGER.info(event);
    }

}
