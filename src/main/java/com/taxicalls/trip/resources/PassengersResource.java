package com.taxicalls.trip.resources;

import com.taxicalls.trip.model.Passenger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/passengers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class PassengersResource {

    private final EntityManager em;

    public PassengersResource() {
        Map<String, String> env = System.getenv();
        Map<String, Object> configOverrides = new HashMap<>();
        env.keySet().forEach((envName) -> {
            if (envName.contains("DATABASE_USER")) {
                configOverrides.put("javax.persistence.jdbc.user", env.get(envName));
            } else if (envName.contains("DATABASE_PASS")) {
                configOverrides.put("javax.persistence.jdbc.password", env.get(envName));
            }
        });
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("trip", configOverrides);
        this.em = emf.createEntityManager();
    }

    @POST
    public Response createPassenger(Passenger passenger) {
        em.getTransaction().begin();
        em.persist(passenger);
        em.getTransaction().commit();
        return Response.status(Response.Status.CREATED).entity(passenger).build();
    }

    @GET
    public Response getPassengers() {
        List<Passenger> passengers = em.createNamedQuery("Passenger.findAll", Passenger.class).getResultList();
        return Response.ok(passengers).build();
    }

    @GET
    @Path("/{id}")
    public Response getPassenger(@PathParam("id") Long id) {
        Passenger passenger = em.find(Passenger.class, id);
        if (passenger == null) {
            return Response.ok("{}").build();
        }
        return Response.ok(passenger).build();
    }
}
