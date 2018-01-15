package com.example.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class CandidateRoomRelationPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int idRoom;
	private int idCandidate;

	public int getIdRoom() {
		return idRoom;
	}

	public void setIdRoom(int idRoom) {
		this.idRoom = idRoom;
	}

	public int getIdCandidate() {
		return idCandidate;
	}

	public void setIdCandidate(int idCandidate) {
		this.idCandidate = idCandidate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idCandidate;
		result = prime * result + idRoom;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CandidateRoomRelationPK other = (CandidateRoomRelationPK) obj;
		if (idCandidate != other.idCandidate)
			return false;
		if (idRoom != other.idRoom)
			return false;
		return true;
	}

}
