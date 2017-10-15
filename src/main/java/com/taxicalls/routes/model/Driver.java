/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.taxicalls.routes.model;

import java.io.Serializable;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Coordinate getAtualCoordinate() {
        return new Coordinate(atualLongitude, atualLatitude);
    }

}
