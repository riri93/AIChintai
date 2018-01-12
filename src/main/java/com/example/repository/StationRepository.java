package com.example.repository;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entity.Station;

@Repository

public interface StationRepository extends JpaRepository<Station, Serializable> {
	@Query(value = "SELECT s.* from  Station s where lower(s.stationName) like lower(CONCAT('%',:station,'%')) or abs(cast( GREATEST(length(:station),length(s.stationName)) -levenshtein( lower(:station),lower(s.stationName)) as float))/length(:station)*100 >40 desc , ?#{#pageable}", nativeQuery = true, countQuery = "SELECT count(s.*) from  station s where lower(s.stationName) like lower(CONCAT('%',:station,'%')) or abs(cast( GREATEST(length(:station),length(s.station_name)) -levenshtein( lower(:station),lower(s.stationName)) as float))/length(:station)*100 >40 desc , ?#{#pageable}")
	public Page findStationss(@Param("station") String station, Pageable pageable);
	
	@Query("SELECT a FROM Station a where a.idStation = :id")
	public Station findStationById(@Param("id") int id);

}
