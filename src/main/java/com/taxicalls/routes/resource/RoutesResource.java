package com.taxicalls.routes.resource;

import com.taxicalls.routes.model.Route;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

@Path("/routes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class RoutesResource {

    private final EntityManager em;

    public RoutesResource() {
        EntityManagerFactory createEntityManagerFactory = Persistence.createEntityManagerFactory("routes");
        this.em = createEntityManagerFactory.createEntityManager(new HashMap());
    }

    @GET
    public Response getRoutes() {
        List<Route> routes = em.createNamedQuery("Route.findAll", Route.class).getResultList();
        return Response.ok(routes).build();
    }

    @GET
    @Path("/{id}")
    public Response getRoute(@PathParam("id") Integer id) {
        Route route = em.find(Route.class, id);
        if (route == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(route).build();
    }

    @POST
    @Path("/available")
    public Response getAvailableRoutes(Route request) {
        List<Route> routes = em.createNamedQuery("Route.findAll", Route.class).getResultList();
        List<Route> availableRoutes = new ArrayList<>();
        for (Route route : routes) {
            if (route.includes(request.getAddressFrom())) {
                availableRoutes.add(route);
            }
        }
        return Response.ok(availableRoutes).build();
    }

    @POST
    public Response createRoute(Route route) {
        em.getTransaction().begin();
        em.persist(route);
        em.getTransaction().commit();
        return Response.status(Response.Status.CREATED).entity(route).build();
    }
}
