package com.taxicalls.trip.resources;

import com.taxicalls.protocol.Response;
import com.taxicalls.trip.model.Coordinate;
import com.taxicalls.trip.model.Driver;
import com.taxicalls.trip.model.Progress;
import com.taxicalls.trip.model.Status;
import com.taxicalls.trip.model.Trip;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
        Trip merge = em.merge(trip);
        em.getTransaction().commit();
        return Response.successful(merge);
    }

    @POST
    @Path("/update")
    public Response updateTrip(Trip trip) {
        if (trip == null) {
            return Response.error("trip incomplete");
        }
        if (trip.getDriver() == null) {
            return Response.error("driver incomplete");
        }
        List<Trip> trips = em.createNamedQuery("Trip.findAll", Trip.class).getResultList();
        em.getTransaction().begin();
        for (Trip stored : trips) {
            if (stored.getDriver() == null) {
                continue;
            }
            if (stored.getDriver().equals(trip.getDriver())) {
                stored.setProgress(Progress.CONCLUDED);
                em.merge(stored);
            }
        }
        em.getTransaction().commit();
        return Response.successful();
    }

    @POST
    @Path("/request")
    public Response requestTrip(Trip trip) {
        if (trip == null) {
            return Response.error("trip incomplete");
        }
        if (trip.getAuthor() == null) {
            return Response.error("author incomplete");
        }
        if (trip.getAddressFrom() == null) {
            return Response.error("addressFrom incomplete");
        }
        if (trip.getAddressTo() == null) {
            return Response.error("addressTo incomplete");
        }
        if (trip.getAddressTo().getCoordinate() == null) {
            return Response.error("coordinateTo incomplete");
        }
        Coordinate coordinate = trip.getAddressFrom().getCoordinate();
        if (coordinate == null) {
            return Response.error("coordinate incomplete");
        }
        if (coordinate.getLatitude() == null) {
            return Response.error("latitude incomplete");
        }
        if (coordinate.getLongitude() == null) {
            return Response.error("longitude incomplete");
        }
        trip.setProgress(Progress.REQUESTED);
        em.getTransaction().begin();
        em.merge(trip);
        em.getTransaction().commit();
        Collection<Trip> trips = em.createNamedQuery("Trip.findAll", Trip.class).getResultList();
        Collection<Driver> drivers = em.createNamedQuery("Driver.findAll", Driver.class).getResultList();
        Collection<Driver> busyDrivers = new ArrayList<>();
        for (Trip stored : trips) {
            if (stored.getProgress().equals(Progress.IN_PROGRESS)) {
                continue;
            } else if (stored.getProgress().equals(Progress.MOVING_TO)) {
                continue;
            }
            busyDrivers.add(stored.getDriver());
        }
        Collection<Driver> availableDrivers = new ArrayList<>();
        for (Driver driver : drivers) {
            if (driver.getAtualCoordinate() == null) {
                continue;
            }
            if (driver.getAtualCoordinate().getLatitude() == null) {
                continue;
            }
            if (driver.getAtualCoordinate().getLongitude() == null) {
                continue;
            }
            if (driver.getStatus().equals(Status.NOT_WORKING)) {
                continue;
            }
            availableDrivers.add(driver);
        }
        availableDrivers.removeAll(busyDrivers);
        List<DriverIsDistant> driverIsDistant = new ArrayList<>();
        availableDrivers.forEach((availableDriver) -> {
            driverIsDistant.add(new DriverIsDistant(availableDriver, coordinate));
        });
        Collections.sort(driverIsDistant);
        return Response.successful(driverIsDistant);
    }

    @GET
    public Response getTrips() {
        List<Trip> trips = em.createNamedQuery("Trip.findAll", Trip.class).getResultList();
        return Response.successful(trips);
    }

    @GET
    @Path("/{id}")
    public Response getTrip(@PathParam("id") Long id) {
        Trip trip = em.find(Trip.class, id);
        if (trip == null) {
            return Response.notFound();
        }
        return Response.successful(trip);
    }
}
