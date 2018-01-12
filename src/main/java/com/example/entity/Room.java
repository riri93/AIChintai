package com.example.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
	private int price;
	private int managementFee;
	private int keyMoney;
	private int deposit;
	private int insurance;

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

}
