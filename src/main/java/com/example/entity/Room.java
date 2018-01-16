package com.example.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Room implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idRoom;
	private String roomID;
	private String nameBuilding;
	private String postCode;
	private String detail;
	private String pictureRoom;
	private int price;
	private int managementFee;
	private int keyMoney;
	private int deposit;
	private int insurance;
	
	@Column(precision = 10, scale = 2)
	private Double longitudeRoom;
	@Column(precision = 10, scale = 2)
	private Double latitudeRoom;

	@OneToMany(mappedBy = "room")
	@JsonIgnoreProperties({ "candidate", "room" })
	private List<CandidateRoomRelation> candidateRoomRelations;

	public int getIdRoom() {
		return idRoom;
	}

	public void setIdRoom(int idRoom) {
		this.idRoom = idRoom;
	}

	public String getRoomID() {
		return roomID;
	}

	public void setRoomID(String roomID) {
		this.roomID = roomID;
	}

	public String getNameBuilding() {
		return nameBuilding;
	}

	public void setNameBuilding(String nameBuilding) {
		this.nameBuilding = nameBuilding;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getManagementFee() {
		return managementFee;
	}

	public void setManagementFee(int managementFee) {
		this.managementFee = managementFee;
	}

	public int getKeyMoney() {
		return keyMoney;
	}

	public void setKeyMoney(int keyMoney) {
		this.keyMoney = keyMoney;
	}

	public int getDeposit() {
		return deposit;
	}

	public void setDeposit(int deposit) {
		this.deposit = deposit;
	}

	public int getInsurance() {
		return insurance;
	}

	public void setInsurance(int insurance) {
		this.insurance = insurance;
	}

	public List<CandidateRoomRelation> getCandidateRoomRelations() {
		return candidateRoomRelations;
	}

	public void setCandidateRoomRelations(List<CandidateRoomRelation> candidateRoomRelations) {
		this.candidateRoomRelations = candidateRoomRelations;
	}

	public Double getLongitudeRoom() {
		return longitudeRoom;
	}

	public void setLongitudeRoom(Double longitudeRoom) {
		this.longitudeRoom = longitudeRoom;
	}

	public Double getLatitudeRoom() {
		return latitudeRoom;
	}

	public void setLatitudeRoom(Double latitudeRoom) {
		this.latitudeRoom = latitudeRoom;
	}

	public String getPictureRoom() {
		return pictureRoom;
	}

	public void setPictureRoom(String pictureRoom) {
		this.pictureRoom = pictureRoom;
	}

}
