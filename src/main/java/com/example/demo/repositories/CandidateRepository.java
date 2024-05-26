package com.example.demo.repositories;

import com.example.demo.models.Candidate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidateRepository extends MongoRepository<Candidate, String> {
    Optional<Candidate> findByUsername(String username);
}
