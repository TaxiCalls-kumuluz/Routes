package com.taxicalls.trip.service;

import com.taxicalls.utils.Eager;
import com.taxicalls.utils.ServiceRegistry;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Eager
@ApplicationScoped
public class TripService {

    @Inject
    private ServiceRegistry services;

    private final String serviceName;
    private final String endpointURI;

    public TripService() {
        this.serviceName = getClass().getSimpleName();
        this.endpointURI = "http://" + serviceName.replace("Service", "").toLowerCase() + ":8080/";
    }

    @PostConstruct
    public void registerService() {
        services.registerService(serviceName, endpointURI);
    }

    @PreDestroy
    public void unregisterService() {
        services.unregisterService(serviceName, endpointURI);
    }
}
