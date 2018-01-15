package com.example.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.entity.Station;
import com.example.repository.StationRepository;

@RestController
public class ServiceController {

	@Autowired
	StationRepository stationRepository;

	@RequestMapping(value = "/station", method = RequestMethod.GET)
	public void stationService() throws JSONException {

		String baseURL = "https://maps.googleapis.com/maps/api/geocode/json?address=563-0056&key=AIzaSyBMuLYCug0drD2cEY0MuutYszCGnac3nLQ";

		System.out.println("***********URL************* " + baseURL);

		RestTemplate rest = new RestTemplate();
		rest.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

		ResponseEntity<String> response = rest.exchange(baseURL, HttpMethod.GET, null, String.class);

		// System.out.println("******************RESPONSE********** : " +
		// response.getBody());
		JSONObject jsonObj = new JSONObject(response.getBody());
		JSONArray jsonarr = jsonObj.getJSONArray("results");

		System.out.println("******************RESPONSE********** : "
				+ jsonarr.getJSONObject(0).getJSONObject("geometry").getJSONObject("location"));

		// Page nearestStations;
		//
		// nearestStations = stationRepository.findStations("aa" , new PageRequest(0,
		// 3));

		// if (nearestStations.getContent().size() > 0) {
		//
		// }

	}

}
