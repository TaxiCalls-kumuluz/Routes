/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.taxicalls.trip.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

/**
 *
 * @author romulo
 */
@Entity
@NamedQuery(name = "Driver.findAll", query = "SELECT d FROM Driver d")
public class Driver implements Serializable {

    @Id
    private Long id;

    private Long atualLatitude;
    private Long atualLongitude;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String car;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
    }

    public void setPassword(String password) {
    }

    public void setAtualLatitude(Long atualLatitude) {
        this.atualLatitude = atualLatitude;
    }

    public void setAtualLongitude(Long atualLongitude) {
        this.atualLongitude = atualLongitude;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public Coordinate getAtualCoordinate() {
        return new Coordinate(atualLongitude, atualLatitude);
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }
    
    public void setAccount(String account){
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Driver)) {
            return false;
        }
        Driver other = (Driver) obj;
        return getId().equals(other.getId());
    }

}
