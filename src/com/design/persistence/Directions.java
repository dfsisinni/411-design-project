/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.design.persistence;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author dane
 */
@Entity
@Table(name = "directions")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Directions.findAll", query = "SELECT d FROM Directions d"),
    @NamedQuery(name = "Directions.findById", query = "SELECT d FROM Directions d WHERE d.id = :id"),
    @NamedQuery(name = "Directions.findByOrigin", query = "SELECT d FROM Directions d WHERE d.origin = :origin"),
    @NamedQuery(name = "Directions.findByDestination", query = "SELECT d FROM Directions d WHERE d.destination = :destination"),
    @NamedQuery(name = "Directions.findByDistance", query = "SELECT d FROM Directions d WHERE d.distance = :distance"),
    @NamedQuery(name = "Directions.findByTime", query = "SELECT d FROM Directions d WHERE d.time = :time")})
public class Directions implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "origin")
    private String origin;
    @Column(name = "destination")
    private String destination;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "distance")
    private Double distance;
    @Column(name = "time")
    private Double time;
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Queries queries;

    public Directions() {
    }

    public Directions(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    public Queries getQueries() {
        return queries;
    }

    public void setQueries(Queries queries) {
        this.queries = queries;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Directions)) {
            return false;
        }
        Directions other = (Directions) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpabuilder.Directions[ id=" + id + " ]";
    }
    
}
