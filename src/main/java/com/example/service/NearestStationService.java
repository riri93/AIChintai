package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.NearestStation;
import com.example.repository.NearestStationRepository;

@Service
public class NearestStationService {

	@Autowired
	private NearestStationRepository nearestStationRepository;

	public void setNearestStationRepository(NearestStationRepository nearestStationRepository) {
		this.nearestStationRepository = nearestStationRepository;
	}

	public Iterable<NearestStation> listAllNearestStations() {
		return nearestStationRepository.findAll();
	}

	public NearestStation getNearestStationById(int id) {
		return nearestStationRepository.findOne(id);
	}

	public NearestStation saveNearestStation(NearestStation nearestStation) {
		return nearestStationRepository.save(nearestStation);
	}

	public List<NearestStation> getAllNearestStations() {
		return nearestStationRepository.findAll();
	}

}
