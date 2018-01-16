package com.example.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.LineStation;
import com.example.repository.LineStationRepository;

@Service
public class LineStationService {

	@Autowired
	private LineStationRepository lineStationRepository;

	public void setLineStationRepository(LineStationRepository lineStationRepository) {
		this.lineStationRepository = lineStationRepository;
	}

	public Iterable<LineStation> listAllLineStations() {
		return lineStationRepository.findAll();
	}

	public LineStation getLineStationById(int id) {
		return lineStationRepository.findOne(id);
	}

	public LineStation getLineStationByIdLineCD(int idCD) {
		return lineStationRepository.getLineStationByIdLineCD(idCD);
	}

	public LineStation saveLineStation(LineStation lineStation) {
		return lineStationRepository.save(lineStation);
	}

}
