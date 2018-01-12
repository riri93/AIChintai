package com.example.repository;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.example.entity.Candidate;
import com.example.entity.Station;

@Repository

public interface StationRepository extends JpaRepository<Station, Serializable> {
	@Query(value = "SELECT s.* from  station s where lower(s.stationName) like lower(CONCAT('%',:station,'%')) or abs(cast( GREATEST(length(:station),length(n.japanese_station)) -levenshtein( lower(:station),lower(s.stationName)) as float))/length(:station)*100 >40  desc ,    ?#{#pageable}", nativeQuery = true, countQuery = "SELECT count(s.*) from  station s where lower(s.stationName) like lower(CONCAT('%',:station,'%')) or abs(cast( GREATEST(length(:station),length(n.japanese_station)) -levenshtein( lower(:station),lower(s.stationName)) as float))/length(:station)*100 >40  desc ,  ?#{#pageable}  ")
	public Page findStations(@Param("station") String station, Pageable pageable);

}
