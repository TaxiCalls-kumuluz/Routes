package com.taxicalls.trip.resources;

import com.taxicalls.trip.model.Trip;
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

@Path("/trips")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class TripsResource {

    private final EntityManager em;

    public TripsResource() {
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
    public Response createTrip(Trip trip) {
        em.getTransaction().begin();
        em.persist(trip);
        em.getTransaction().commit();
        return Response.status(Response.Status.CREATED).entity(trip).build();
    }

    @GET
    public Response getTrips() {
        List<Trip> trips = em.createNamedQuery("Trip.findAll", Trip.class).getResultList();
        return Response.ok(trips).build();
    }

    @GET
    @Path("/{id}")
    public Response getTrip(@PathParam("id") Integer id) {
        Trip trip = em.find(Trip.class, id);
        if (trip == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(trip).build();
    }
}
