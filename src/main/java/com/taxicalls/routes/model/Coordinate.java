/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.taxicalls.routes.model;

import java.io.Serializable;

/**
 *
 * @author romulo
 */
public class Coordinate implements Serializable, Comparable<Coordinate> {

    private final Long longitude;
    private final Long latitude;

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

    @Override
    public int compareTo(Coordinate coordinate) {
        return 1;
    }

}
