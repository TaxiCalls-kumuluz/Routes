package com.taxicalls.trip.resources;

import com.taxicalls.protocol.Response;
import com.taxicalls.trip.model.Coordinate;
import com.taxicalls.trip.model.Driver;
import com.taxicalls.trip.model.Trip;
import java.util.ArrayList;
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

@Path("/drivers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class DriversResource {

    private final EntityManager em;

    public DriversResource() {
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
    public Response createDriver(Driver driver) {
        if (driver.getId() == null) {
            return Response.error("missing id");
        }
        em.getTransaction().begin();
        em.persist(driver);
        em.getTransaction().commit();
        return Response.successful(driver);
    }

    @GET
    public Response getDrivers() {
        List<Driver> drivers = em.createNamedQuery("Driver.findAll", Driver.class).getResultList();
        return Response.successful(drivers);
    }

    @GET
    @Path("/{id}")
    public Response getDriver(@PathParam("id") Long id) {
        Driver driver = em.find(Driver.class, id);
        if (driver == null) {
            return Response.notFound();
        }
        return Response.successful(driver);
    }

    @GET
    @Path("/available")
    public Response getAvailableDriversInfo() {
        AvailableDriversRequest availableDriversRequest = new AvailableDriversRequest();
        availableDriversRequest.setRatio(0);
        availableDriversRequest.setCoordinate(new Coordinate(0, 0));
        return Response.successful(availableDriversRequest);
    }

    @POST
    @Path("/available")
    public Response getAvailableDrivers(AvailableDriversRequest availableDriversRequest) {
        Coordinate coordinate = availableDriversRequest.getCoordinate();
        int ratio = availableDriversRequest.getRatio();
        List<Trip> trips = em.createNamedQuery("Trip.findAll", Trip.class).getResultList();
        List<Driver> drivers = em.createNamedQuery("Driver.findAll", Driver.class).getResultList();
        List<Driver> busyDrivers = new ArrayList<>();
        trips.forEach((trip) -> {
            busyDrivers.add(trip.getDriver());
        });
        List<Driver> availableDrivers = new ArrayList<>();
        for (Driver driver : drivers) {
            if (driver.getAtualCoordinate().getEuclidienDistance(coordinate) <= ratio) {
                availableDrivers.add(driver);
            }
        }
        availableDrivers.removeAll(busyDrivers);
        return Response.successful(availableDrivers);
    }
}