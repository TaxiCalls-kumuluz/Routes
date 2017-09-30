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
public class Address implements Serializable, Comparable<Address> {

    private final Coordinate coordinate;

    public Address(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public boolean isGreaterThan(Address address) {
        return compareTo(address) > 0;
    }

    public boolean isLesserThan(Address address) {
        return compareTo(address) < 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Address)) {
            return false;
        }
        Address other = (Address) obj;
        return compareTo(other) == 0;
    }

    @Override
    public int compareTo(Address address) {
        return Integer.compare(0, 0);
    }

}
