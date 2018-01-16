package com.example.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.example.entity.LineStation;

@Repository
@RepositoryRestResource
public interface LineStationRepository extends JpaRepository<LineStation, Serializable> {


	@Query(value = "select l from LineStation l where l.lineCD=:idLineCD")
	public LineStation getLineStationByIdLineCD(@Param("idLineCD") int idLineCD);

}
