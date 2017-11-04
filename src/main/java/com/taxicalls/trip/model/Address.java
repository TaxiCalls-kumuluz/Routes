/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.taxicalls.trip.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author romulo
 */
@Entity
public class Address implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private Long longitude;
    private Long latitude;

    public Address() {
    }

    public Coordinate getCoordinate() {
        return new Coordinate(longitude, latitude);
    }

    public void setCoordinate(Coordinate coordinate) {
        this.latitude = coordinate.getLatitude();
        this.longitude = coordinate.getLongitude();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Address)) {
            return false;
        }
        Address other = (Address) obj;
        return Objects.equals(this.getCoordinate(), other.getCoordinate());
    }
}
