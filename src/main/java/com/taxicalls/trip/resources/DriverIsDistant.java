/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.taxicalls.trip.resources;

import com.taxicalls.trip.model.Coordinate;
import com.taxicalls.trip.model.Driver;

/**
 *
 * @author romulo
 */
public class DriverIsDistant implements Comparable<DriverIsDistant> {

    private Driver driver;
    private double distance;

    public DriverIsDistant() {
    }

    public DriverIsDistant(Driver driver, Coordinate coordinate) {
        this.driver = driver;
        this.distance = driver.getAtualCoordinate().getEuclidienDistance(coordinate);
    }

    public DriverIsDistant(Driver driver, double distance) {
        this.driver = driver;
        this.distance = distance;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(DriverIsDistant o) {
        return Double.compare(this.getDistance(), o.getDistance());
    }
}
