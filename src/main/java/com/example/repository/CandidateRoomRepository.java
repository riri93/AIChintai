package com.example.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.example.entity.CandidateRoomRelation;

@Repository
@RepositoryRestResource

public interface CandidateRoomRepository extends JpaRepository<CandidateRoomRelation, Serializable> {
}
