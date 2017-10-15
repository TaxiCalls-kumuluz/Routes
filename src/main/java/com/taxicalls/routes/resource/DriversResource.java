package com.taxicalls.routes.resource;

import com.taxicalls.routes.model.Coordinate;
import com.taxicalls.routes.model.Driver;
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
import javax.ws.rs.core.Response;

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
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("routes", configOverrides);
        this.em = emf.createEntityManager();
    }

    @POST
    public Response createDriver(Driver driver) {
        if (driver.getId() == null) {
            return Response.status(Response.Status.PARTIAL_CONTENT).entity(driver).build();
        }
        em.getTransaction().begin();
        em.persist(driver);
        em.getTransaction().commit();
        return Response.status(Response.Status.CREATED).entity(driver).build();
    }

    @GET
    public Response getDrivers() {
        List<Driver> drivers = em.createNamedQuery("Driver.findAll", Driver.class).getResultList();
        return Response.ok(drivers).build();
    }

    @GET
    @Path("/{id}")
    public Response getDriver(@PathParam("id") Integer id) {
        Driver driver = em.find(Driver.class, id);
        if (driver == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(driver).build();
    }

    @POST
    @Path("/available")
    public Response getAvailableDrivers(AvailableDriversRequest availableDriversRequest) {
        Coordinate coordinate = availableDriversRequest.getCoordinate();
        int ratio = availableDriversRequest.getRatio();
        List<Driver> drivers = em.createNamedQuery("Driver.findAll", Driver.class).getResultList();
        List<Driver> availableDrivers = new ArrayList<>();
        for (Driver driver : drivers) {
            if (driver.getAtualCoordinate().getEuclidienDistance(coordinate) <= ratio) {
                availableDrivers.add(driver);
            }
        }
        return Response.ok(availableDrivers).build();
    }
}
