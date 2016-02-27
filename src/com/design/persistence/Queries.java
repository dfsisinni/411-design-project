/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.design.persistence;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author dane
 */
@Entity
@Table(name = "queries")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Queries.findAll", query = "SELECT q FROM Queries q"),
    @NamedQuery(name = "Queries.findById", query = "SELECT q FROM Queries q WHERE q.id = :id"),
    @NamedQuery(name = "Queries.findByClass1", query = "SELECT q FROM Queries q WHERE q.class1 = :class1"),
    @NamedQuery(name = "Queries.findByQuery", query = "SELECT q FROM Queries q WHERE q.query = :query"),
    @NamedQuery(name = "Queries.findByTime", query = "SELECT q FROM Queries q WHERE q.time = :time"),
    @NamedQuery(name = "Queries.findByType", query = "SELECT q FROM Queries q WHERE q.type = :type"),
    @NamedQuery(name = "Queries.findBySuccessful", query = "SELECT q FROM Queries q WHERE q.successful = :successful"),
    @NamedQuery(name = "Queries.findByConfidence", query = "SELECT q FROM Queries q WHERE q.confidence = :confidence"),
    @NamedQuery(name = "Queries.findByResponseTime", query = "SELECT q FROM Queries q WHERE q.responseTime = :responseTime")})
public class Queries implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "class")
    private String class1;
    @Column(name = "query")
    private String query;
    @Column(name = "time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
    @Column(name = "type")
    private String type;
    @Column(name = "successful")
    private Boolean successful;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "confidence")
    private Double confidence;
    @Column(name = "responseTime")
    private Double responseTime;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "queries")
    private News news;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "queries")
    private Directions directions;
    @JoinColumn(name = "phone", referencedColumnName = "phone")
    @ManyToOne
    private Users phone;

    public Queries() {
    }

    public Queries(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClass1() {
        return class1;
    }

    public void setClass1(String class1) {
        this.class1 = class1;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getSuccessful() {
        return successful;
    }

    public void setSuccessful(Boolean successful) {
        this.successful = successful;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public Double getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Double responseTime) {
        this.responseTime = responseTime;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public Directions getDirections() {
        return directions;
    }

    public void setDirections(Directions directions) {
        this.directions = directions;
    }

    public Users getPhone() {
        return phone;
    }

    public void setPhone(Users phone) {
        this.phone = phone;
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
        if (!(object instanceof Queries)) {
            return false;
        }
        Queries other = (Queries) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpabuilder.Queries[ id=" + id + " ]";
    }
    
}
