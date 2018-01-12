package com.example.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Station implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idStation;
	private String stationName;
	private String lineName;
	private String addressStation;

	@Column(precision = 10, scale = 2)
	private Double longitudeStation;
	@Column(precision = 10, scale = 2)
	private Double latitudeStation;

	public int getIdStation() {
		return idStation;
	}

	public void setIdStation(int idStation) {
		this.idStation = idStation;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getAddressStation() {
		return addressStation;
	}

	public void setAddressStation(String addressStation) {
		this.addressStation = addressStation;
	}

	public Double getLongitudeStation() {
		return longitudeStation;
	}

	public void setLongitudeStation(Double longitudeStation) {
		this.longitudeStation = longitudeStation;
	}

	public Double getLatitudeStation() {
		return latitudeStation;
	}

	public void setLatitudeStation(Double latitudeStation) {
		this.latitudeStation = latitudeStation;
	}

}
