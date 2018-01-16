package com.example.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Candidate extends UserInformation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userLineId;
	private String phone;
	private Date birthday;
	private String jLPT;
	private String durationInJapan;
	private Date preferedDate;
	private String preferedTime;
	

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "idBotInformation", referencedColumnName = "idBotInformation")
	@JsonIgnoreProperties("candidate")
	private BotInformation botInformation;

	@OneToMany(mappedBy = "candidate")
	@JsonIgnoreProperties({ "candidate", "room" })
	private List<CandidateRoomRelation> candidateRoomRelations;
	
	public String getUserLineId() {
		return userLineId;
	}

	public void setUserLineId(String userLineId) {
		this.userLineId = userLineId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getjLPT() {
		return jLPT;
	}

	public void setjLPT(String jLPT) {
		this.jLPT = jLPT;
	}

	public String getDurationInJapan() {
		return durationInJapan;
	}

	public void setDurationInJapan(String durationInJapan) {
		this.durationInJapan = durationInJapan;
	}

	public BotInformation getBotInformation() {
		return botInformation;
	}

	public void setBotInformation(BotInformation botInformation) {
		this.botInformation = botInformation;
	}

	public List<CandidateRoomRelation> getCandidateRoomRelations() {
		return candidateRoomRelations;
	}

	public void setCandidateRoomRelations(List<CandidateRoomRelation> candidateRoomRelations) {
		this.candidateRoomRelations = candidateRoomRelations;
	}

	public Date getPreferedDate() {
		return preferedDate;
	}

	public void setPreferedDate(Date preferedDate) {
		this.preferedDate = preferedDate;
	}

	public String getPreferedTime() {
		return preferedTime;
	}

	public void setPreferedTime(String preferedTime) {
		this.preferedTime = preferedTime;
	}

}
