package com.example.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.example.entity.Room;

@Repository
@RepositoryRestResource
public interface RoomRepository extends JpaRepository<Room, Serializable> {

	@Query(value = "SELECT DISTINCT(r) FROM Room r")
	public List<Room> findRoomsByAllFields();

	@Query(value = "SELECT DISTINCT(r) FROM Room r where r.price>=minPrice and r.price<=maxPrice")
	public List<Room> findRoomsByPrice(int minPrice, int maxPrice);

}
