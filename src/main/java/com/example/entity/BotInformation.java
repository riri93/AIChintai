package com.example.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BotInformation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5964532135862641283L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idBotInformation;
	@Column(columnDefinition = "int default 0") /** 0 if first time**1 if second time */
	private int pageMoreRooms;
	@Column(columnDefinition = "int default 0")
	private int status;
	private String intentName;
	private String languageBot;
	private String stationToSearch;
	private String distanceToSearch;
	private String priceToSearch;

	public int getIdBotInformation() {
		return idBotInformation;
	}

	public void setIdBotInformation(int idBotInformation) {
		this.idBotInformation = idBotInformation;
	}

	public int getPageMoreRooms() {
		return pageMoreRooms;
	}

	public void setPageMoreRooms(int pageMoreRooms) {
		this.pageMoreRooms = pageMoreRooms;
	}

	public String getIntentName() {
		return intentName;
	}

	public void setIntentName(String intentName) {
		this.intentName = intentName;
	}

	public String getLanguageBot() {
		return languageBot;
	}

	public void setLanguageBot(String languageBot) {
		this.languageBot = languageBot;
	}

	public String getStationToSearch() {
		return stationToSearch;
	}

	public void setStationToSearch(String stationToSearch) {
		this.stationToSearch = stationToSearch;
	}

	public String getDistanceToSearch() {
		return distanceToSearch;
	}

	public void setDistanceToSearch(String distanceToSearch) {
		this.distanceToSearch = distanceToSearch;
	}

	public String getPriceToSearch() {
		return priceToSearch;
	}

	public void setPriceToSearch(String priceToSearch) {
		this.priceToSearch = priceToSearch;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
