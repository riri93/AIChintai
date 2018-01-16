package com.example.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class NearestStation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idNearestStation;

	private int idNearestStationCD;
	private String japaneseStation;
	private String japaneseKatakanaStation;
	private String japaneseHiraganaStation;
	private String japaneseRomajiStation;

	private String addressStation;

	@Column(precision = 10, scale = 2)
	private Double longitudeStation;
	@Column(precision = 10, scale = 2)
	private Double latitudeStation;

	private String openYmd;
	private String closeYmd;
	
	private String nearestStationDetail;

	@OneToOne
	@JoinColumn(name = "idLineStation", referencedColumnName = "idLineStation")
	private LineStation lineStation;

	public int getIdNearestStation() {
		return idNearestStation;
	}

	public void setIdNearestStation(int idNearestStation) {
		this.idNearestStation = idNearestStation;
	}

	public LineStation getLineStation() {
		return lineStation;
	}

	public void setLineStation(LineStation lineStation) {
		this.lineStation = lineStation;
	}

	public String getJapaneseHiraganaStation() {
		return japaneseHiraganaStation;
	}

	public void setJapaneseHiraganaStation(String japaneseHiraganaStation) {
		this.japaneseHiraganaStation = japaneseHiraganaStation;
	}

	public String getJapaneseStation() {
		return japaneseStation;
	}

	public void setJapaneseStation(String japaneseStation) {
		this.japaneseStation = japaneseStation;
	}

	public String getJapaneseKatakanaStation() {
		return japaneseKatakanaStation;
	}

	public void setJapaneseKatakanaStation(String japaneseKatakanaStation) {
		this.japaneseKatakanaStation = japaneseKatakanaStation;
	}

	public String getJapaneseRomajiStation() {
		return japaneseRomajiStation;
	}

	public void setJapaneseRomajiStation(String japaneseRomajiStation) {
		this.japaneseRomajiStation = japaneseRomajiStation;
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

	public String getOpenYmd() {
		return openYmd;
	}

	public void setOpenYmd(String openYmd) {
		this.openYmd = openYmd;
	}

	public String getCloseYmd() {
		return closeYmd;
	}

	public void setCloseYmd(String closeYmd) {
		this.closeYmd = closeYmd;
	}

	public int getIdNearestStationCD() {
		return idNearestStationCD;
	}

	public void setIdNearestStationCD(int idNearestStationCD) {
		this.idNearestStationCD = idNearestStationCD;
	}


	public String getNearestStationDetail() {
		return nearestStationDetail;
	}

	public void setNearestStationDetail(String nearestStationDetail) {
		this.nearestStationDetail = nearestStationDetail;
	}

}
