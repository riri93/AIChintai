package com.example.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.example.entity.Candidate;

@Repository
@RepositoryRestResource
public interface CandidateRepository extends JpaRepository<Candidate, Serializable> {

	@Query(value = "select s from Candidate s where s.userLineId =:lineID")
	public Candidate findByUserLineId(@Param("lineID") String lineID);

	@Query(value = "select c from Candidate c where c.botInformation.idBotInformation =:idBotInformation")
	public Candidate findCandidateByIdBotInformation(@Param("idBotInformation") int idBotInformation);

}
