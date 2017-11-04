/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.taxicalls.trip.model;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author romulo
 */
public class Coordinate implements Serializable {

    private final Long longitude;
    private final Long latitude;

    public Coordinate() {
        this.longitude = null;
        this.latitude = null;
    }

    public Coordinate(int longitude, int latitude) {
        this.longitude = (long) longitude;
        this.latitude = (long) latitude;
    }

    public Coordinate(Long longitude, Long latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Long getLatitude() {
        return latitude;
    }

    public Long getLongitude() {
        return longitude;
    }

    public double getEuclidienDistance(Coordinate coordinate) {
        return Math.sqrt(
                Math.pow(this.getLatitude() - coordinate.getLatitude(), 2)
                + Math.pow(this.getLongitude() - coordinate.getLongitude(), 2)
        );
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.longitude);
        hash = 29 * hash + Objects.hashCode(this.latitude);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Coordinate)) {
            return false;
        }
        Coordinate other = (Coordinate) obj;
        return Objects.equals(this.getLatitude(), other.getLatitude()) && Objects.equals(this.getLongitude(), other.getLongitude());
    }

}
