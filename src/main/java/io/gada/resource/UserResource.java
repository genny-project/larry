//package io.gada.resource;
//
//import javax.enterprise.context.ApplicationScoped;
//
//import io.gada.TopologyProducer;
//import org.eclipse.microprofile.reactive.messaging.Channel;
//import org.eclipse.microprofile.reactive.messaging.Emitter;
//import org.eclipse.microprofile.reactive.messaging.Incoming;
//import org.jboss.logging.Logger;
//
//import java.lang.invoke.MethodHandles;
//
//@ApplicationScoped
//public class UserResource {
//    private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass());
//
////    @Channel("channel_users_agg")
////    Emitter<String> events;
//
//    @Incoming(TopologyProducer.TOPIC_OUTPUT)
//    public void process(String event) {
//        LOGGER.info("process");
//        LOGGER.info(event);
//    }
//
//}
