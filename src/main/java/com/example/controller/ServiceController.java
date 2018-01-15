package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.repository.StationRepository;

@RestController
public class ServiceController {

	@Autowired
	StationRepository stationRepository;

	@RequestMapping(value = "/station", method = RequestMethod.GET)
	public void stationService() {

		Page nearestStations;

		nearestStations = stationRepository.findStations("aa",
				new PageRequest(0, 3));

		if (nearestStations.getContent().size() > 0) {

		}

	}

}
