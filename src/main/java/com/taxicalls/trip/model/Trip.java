/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.taxicalls.trip.model;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

/**
 *
 * @author romulo
 */
@Entity
@NamedQuery(name = "Trip.findAll", query = "SELECT t FROM Trip t")
public class Trip implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Address addressFrom;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Address addressTo;
    
    @ManyToOne(cascade = CascadeType.MERGE)
    private Driver driver;

    @ManyToMany(cascade = CascadeType.MERGE)
    private Set<Passenger> passengers;
    
    @Enumerated(EnumType.STRING)
    private Progress progress;

    protected Trip() {
    }

    public Trip(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Address getAddressFrom() {
        return addressFrom;
    }

    public Address getAddressTo() {
        return addressTo;
    }

    public Progress getProgress() {
        return progress;
    }

    public Driver getDriver() {
        return driver;
    }

    public Set<Passenger> getPassengers() {
        return passengers;
    }

}
