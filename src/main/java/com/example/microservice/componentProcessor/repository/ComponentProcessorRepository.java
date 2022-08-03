package com.example.microservice.componentProcessor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.microservice.componentProcessor.entity.ComponentProcessorEntity;

@Transactional
@Repository
public interface ComponentProcessorRepository extends JpaRepository<ComponentProcessorEntity, String> {

}
