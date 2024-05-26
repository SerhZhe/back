package com.example.demo.controllers;

import com.example.demo.models.Candidate;
import com.example.demo.repositories.CandidateRepository;
import com.example.demo.models.JwtUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class CandidatesController {
    private final CandidateRepository candidateRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder;

    public CandidatesController(CandidateRepository candidateRepository, JwtUtil jwtUtil, BCryptPasswordEncoder encoder) {
        this.candidateRepository = candidateRepository;
        this.jwtUtil = jwtUtil;
        this.encoder = encoder;
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerCandidate(@RequestBody Candidate candidate) {
        Candidate savedCandidate = candidateRepository.save(candidate);
        final String token = jwtUtil.generateToken(savedCandidate.getUsername());
        Map<String, Object> response = new HashMap<>();
        response.put("user", savedCandidate);
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCandidateByToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Authorization token is missing or invalid");
        }

        String token = authHeader.substring(7);
        try {
            String username = jwtUtil.extractUsername(token);
            Optional<Candidate> candidate = candidateRepository.findByUsername(username);

            if (candidate.isPresent()) {
                candidateRepository.delete(candidate.get());
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login login) {
        Optional<Candidate> candidate = candidateRepository.findByUsername(login.getUsername());
        if (candidate.isPresent() && encoder.matches(login.getPassword(), candidate.get().getPassword())) {
            final String token = jwtUtil.generateToken(login.getUsername());
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Candidate>> getAllCandidates() {
        List<Candidate> candidates = candidateRepository.findAll();
        return ResponseEntity.ok(candidates);
    }

    @DeleteMapping("/{candidateID}")
    public ResponseEntity<?> deleteCandidate(@PathVariable String candidateID) {
        candidateRepository.deleteById(candidateID);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/candidate")
    public ResponseEntity<?> getCandidate(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Authorization token is missing or invalid");
        }
        String token = authHeader.substring(7);
        try {
            String username = jwtUtil.extractUsername(token);

            Optional<Candidate> candidate = candidateRepository.findByUsername(username);
            if (candidate.isPresent()) {
                return ResponseEntity.ok(candidate.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}
@Getter
@Setter
class Login{
    private String username;
    private String password;
}