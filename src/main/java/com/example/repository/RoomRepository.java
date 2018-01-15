package com.example.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.example.entity.Candidate;
import com.example.entity.Room;

@Repository
@RepositoryRestResource
public interface RoomRepository extends JpaRepository<Room, Serializable> {

	@Query(value = "SELECT DISTINCT(r) FROM Room r")
	public List<Room> findRoomsByAllFields();

}
