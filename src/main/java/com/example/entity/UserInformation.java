package com.example.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserInformation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6156085840452149840L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idUserInformation;
	private String userName;
	private String email;
	private String password;
	private String profilePicture;

	public int getIdUserInformation() {
		return idUserInformation;
	}

	public void setIdUserInformation(int idUserInformation) {
		this.idUserInformation = idUserInformation;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

}
