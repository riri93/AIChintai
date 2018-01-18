package com.example.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Candidate;
import com.example.repository.CandidateRepository;

@RestController
public class CandidateController {
	@Autowired
	private CandidateRepository candidateRepository;

	@RequestMapping(value = "/saveCandidate", method = RequestMethod.POST)
	public void saveCandidate(@RequestBody @Valid Candidate candidate) {
		candidateRepository.save(candidate);
	}

	@RequestMapping(value = "/getCandidate", method = RequestMethod.GET)
	public Candidate getCandidate(@RequestParam(name = "idCandidate") int idCandidate) {
		return candidateRepository.findCandidateById(idCandidate);
	}
}
