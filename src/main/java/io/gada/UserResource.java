//package io.gada;
//
//import io.gada.message.UserCountData;
//
//import java.util.Optional;
//
//import javax.inject.Inject;
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.Response.Status;
//
//
//@Path("/users")
//public class UserResource {
//
////    @Inject
////    InteractiveQueries interactiveQueries;
////
////    @GET
////    @Path("/data/{id}")
////    public Response countUser(@PathParam("id") int id) {
////        Optional<UserCountData> userCountData = interactiveQueries.getUserAccessCountData(id);
////
////        if (userCountData.isPresent()) {
////            return Response.ok(userCountData.get()).build();
////        } else {
////            return Response.status(Status.NOT_FOUND.getStatusCode(),
////                    "No data found for movie " + id).build();
////        }
////    }
////
////
////    @GET
////    @Path("/active")
////    public Response listUser(@PathParam("id") int id) {
////        Optional<UserCountData> userCountData = interactiveQueries.getUserAccessCountData(id);
////
////        if (userCountData.isPresent()) {
////            return Response.ok(userCountData.get()).build();
////        } else {
////            return Response.status(Status.NOT_FOUND.getStatusCode(),
////                    "No data found for movie " + id).build();
////        }
////    }
//
//}