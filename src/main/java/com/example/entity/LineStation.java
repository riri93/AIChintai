package com.example.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class LineStation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idLineStation;
	private int lineCD;
	private String lineNameJapanese;
	private String lineNameKatakana;
	private String lineNameHiragana;
	private String lineColorCode;
	private String lineColor;
	private int lineType;
	@Column(precision = 10, scale = 2)
	private Double longitude;
	@Column(precision = 10, scale = 2)
	private Double latitude;
	private int lineZoom;

	public int getIdLineStation() {
		return idLineStation;
	}

	public void setIdLineStation(int idLineStation) {
		this.idLineStation = idLineStation;
	}

	public String getLineNameJapanese() {
		return lineNameJapanese;
	}

	public void setLineNameJapanese(String lineNameJapanese) {
		this.lineNameJapanese = lineNameJapanese;
	}

	public String getLineNameKatakana() {
		return lineNameKatakana;
	}

	public void setLineNameKatakana(String lineNameKatakana) {
		this.lineNameKatakana = lineNameKatakana;
	}

	public String getLineNameHiragana() {
		return lineNameHiragana;
	}

	public void setLineNameHiragana(String lineNameHiragana) {
		this.lineNameHiragana = lineNameHiragana;
	}

	public String getLineColorCode() {
		return lineColorCode;
	}

	public void setLineColorCode(String lineColorCode) {
		this.lineColorCode = lineColorCode;
	}

	public String getLineColor() {
		return lineColor;
	}

	public void setLineColor(String lineColor) {
		this.lineColor = lineColor;
	}

	public int getLineType() {
		return lineType;
	}

	public void setLineType(int lineType) {
		this.lineType = lineType;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public int getLineZoom() {
		return lineZoom;
	}

	public void setLineZoom(int lineZoom) {
		this.lineZoom = lineZoom;
	}

	public int getLineCD() {
		return lineCD;
	}

	public void setLineCD(int lineCD) {
		this.lineCD = lineCD;
	}

}
