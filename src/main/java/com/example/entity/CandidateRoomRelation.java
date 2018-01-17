package com.example.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class CandidateRoomRelation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private CandidateRoomRelationPK candidateRoomRelationPK;
	
	

	@ManyToOne
	@JoinColumn(name = "idCandidate", referencedColumnName = "idUserInformation", insertable = false, updatable = false)
	@JsonIgnoreProperties({"candidateRoomRelations"})
	private Candidate candidate;

	@ManyToOne
	@JoinColumn(name = "idRoom", referencedColumnName = "idRoom", insertable = false, updatable = false)
	@JsonIgnoreProperties({"candidateRoomRelations"})
	private Room room;
	
	
	private boolean applied = false;
	private Date appliedDate;
	private Date preferedDate;
	private String preferedTime;

	public CandidateRoomRelationPK getCandidateRoomRelationPK() {
		return candidateRoomRelationPK;
	}

	public void setCandidateRoomRelationPK(CandidateRoomRelationPK candidateRoomRelationPK) {
		this.candidateRoomRelationPK = candidateRoomRelationPK;
	}

	public Candidate getCandidate() {
		return candidate;
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public boolean isApplied() {
		return applied;
	}

	public void setApplied(boolean applied) {
		this.applied = applied;
	}

	public Date getAppliedDate() {
		return appliedDate;
	}

	public void setAppliedDate(Date appliedDate) {
		this.appliedDate = appliedDate;
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
