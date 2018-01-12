package com.example.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.example.entity.UserInformation;

@Repository
@RepositoryRestResource
public interface UserRepository extends JpaRepository<UserInformation, Serializable> {

}