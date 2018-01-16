package com.example.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.example.entity.LineStation;
import com.example.entity.NearestStation;
import com.example.entity.Room;
import com.example.repository.NearestStationRepository;
import com.example.repository.RoomRepository;
import com.example.service.LineStationService;
import com.example.service.NearestStationService;

@RestController
public class ServiceController {
	
	@Autowired
	RoomRepository roomRepository;
	
	@Autowired
	NearestStationRepository nearestStationRepository;
	
	@Autowired
	NearestStationService nearestStationService;
	
	@Autowired
	LineStationService lineStationService;
	
	public JSONObject stationService(String postcode) throws JSONException {

		String baseURL = "https://maps.googleapis.com/maps/api/geocode/json?address="+postcode+"&key=AIzaSyBMuLYCug0drD2cEY0MuutYszCGnac3nLQ";

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
		return jsonarr.getJSONObject(0).getJSONObject("geometry").getJSONObject("location");

	}
	/****************************************************************/
	/**
	 * @author Wala Ben Amor
	 * 
	 *         Method to upload the room csv file
	 * @param request
	 */
	@RequestMapping(value = "/api/uploadRoomsFile", method = RequestMethod.GET)
	public void uploadRoomsFile() {
		try {

			String line = "";


			String csvFile = "/home/djo/Téléchargements/aichintai-Rooms.csv";
			BufferedReader br = null;
			FileReader fr = null;
			fr = new FileReader(csvFile);
			br = new BufferedReader(fr);

			br.readLine();
			System.out.println("----------START---Room File--------");
			int i = 1;

			while ((line = br.readLine()) != null) {
				System.out.println("----------COMULM NUM-----------" + i);
				i = i + 1;
				// room_id name_building post_code detail price management_fee keymoney deposit
				// insurance

				String room_id = null; // 0
				String name_building = null; // 1
				String post_code = null; // 2
				String detail = null; // 3
				String price = null; // 4
				String management_fee = null; // 5
				/***********/
				String keymoney = null; // 6
				String deposit = null; // 7
				String insurance = null; // 8

				String[] column = line.split(",");

				if (column[0] != null && !column[0].equals("")) {
					room_id = column[0];
					System.out.println("/******** 0 *******room_id***");

				}
				if (column[1] != null && !column[1].equals("")) {
					name_building = column[1];
					System.out.println("/********1 *******name_building*******");

				}
				if (column[2] != null && !column[2].equals("")) {
					post_code = column[2];
					System.out.println("/******** 2 ***post_code*******" + post_code);

				}
				if (column[3] != null && !column[3].equals("")) {
					detail = column[3];
					System.out.println("/******** 3 *******detail*****" + detail);

				}
				if (column[4] != null && !column[4].equals("")) {
					price = column[4];
					System.out.println("/******** 4 *******price*****" + price);

				}
				if (column[5] != null) {
					management_fee = column[5];
					System.out.println("/******** 5*******management_fee*****" + management_fee);
					if (!column[5].equals("null")) {

					}

				}
				/************/
				if (column[6] != null && !column[6].equals("")) {
					keymoney = column[6];
					System.out.println("/******** 6 *******keymoney*****" + keymoney);

				}
				if (column[7] != null && !column[7].equals("")) {
					deposit = column[7];
					System.out.println("/******** 7 *******deposit*****" + deposit);

				}
				if (column[8] != null && !column[8].equals("")) {
					insurance = column[8];
					System.out.println("/******** 8 *******insurance*****" + insurance);

				}

				System.out.println("/*********************");
				Room room = new Room();
				if (name_building != "" && name_building != null) {
					room.setNameBuilding(name_building);
					System.out.println("/***set*****name_building**************");

				}
				if (post_code != "" && post_code != null) {
					JSONObject latLong=stationService(post_code);
					room.setLatitudeRoom(latLong.getDouble("lat"));
					room.setLongitudeRoom(latLong.getDouble("lng"));
					room.setPostCode(post_code);
					System.out.println("/****set****post_code**************");

				}

				if (detail != "" && detail != null) {
					room.setDetail(detail);
					System.out.println("/***set*****detail**************");

				}
				if (price != "" && price != null) {
					room.setPrice(Integer.parseInt(price));
				}

				if (management_fee != "" && management_fee != null) {
					room.setManagementFee(Integer.parseInt(management_fee));
					System.out.println("/****set****management_fee**************");

				}

				if (keymoney != "" && keymoney != null) {
					room.setKeyMoney(Integer.parseInt(keymoney));
					System.out.println("/****set****keyMoney**************");

				}
				/**************/
				if (deposit != "" && deposit != null) {
					room.setDeposit(Integer.parseInt(deposit));
					System.out.println("/****set****deposit**************");

				}
				/***************/
				if (insurance != "" && insurance != null) {
					room.setInsurance(Integer.parseInt(insurance));
					System.out.println("/****set****management_fee**************");

				}

				roomRepository.saveAndFlush(room);

			}
			br.close();
			System.out.println("---ROOM------CLOSE--FINISH---UPLOAD------");

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("----ROOM-----CLOSE-----Exception------");
		}
	}

	
	/***********************************************************/
	/**
	 * @author Wala Ben Amor
	 * 
	 *         Method to upload the near station csv file
	 * @param request
	 */
	@RequestMapping(value = "/uploadstation", method = RequestMethod.GET)
	public void uploadNearStationsFile() {
		try {
	
			/*******************************************/
			String line = "";

	
			
			String csvFile = "/home/djo/Téléchargements/mt_databasestation.csv";
			BufferedReader br = null;
			FileReader fr = null;
			fr = new FileReader(csvFile);
			br = new BufferedReader(fr);
			br.readLine();
			System.out.println("----------START---NearStationsFile--------");
			int i = 1;

			while ((line = br.readLine()) != null) {
				System.out.println("----------COMULM NUM-----------" + i);
				i = i + 1;
				String idNearestStation = null;
				String idLineStation = null;
				String japaneseStation = null;
				String japaneseKatakanaStation = null;
				String japaneseHiraganaStation = null;
				String japaneseRomajiStation = null;
				String addressStation = null;
				String longitudeStation = null;
				String latitudeStation = null;
				String openYmd = null;
				String closeYmd = null;

				String[] column = line.split(",");

				System.out.println("--column[0]-nameShop--");
				if (column[0] != null && !column[0].equals("")) {
					idNearestStation = column[0];
					System.out.println("/******** 0 ***idNearestStation*******" + idNearestStation);

				}

				if (column[2] != null && !column[2].equals("")) {
					japaneseStation = column[2];
					System.out.println("/******** 2 ***japaneseStation***********" + japaneseStation);

				}
				if (column[3] != null && !column[3].equals("")) {
					japaneseKatakanaStation = column[3];
					System.out.println("/******** 3 *******japaneseKatakanaStation*******" + japaneseKatakanaStation);

				}
				if (column[4] != null && !column[4].equals("")) {
					japaneseHiraganaStation = column[4];
					System.out.println("/******** 4 *******japaneseHiraganaStation*******" + japaneseHiraganaStation);

				}
				if (column[5] != null && !column[5].equals("")) {
					japaneseRomajiStation = column[5];
					System.out.println("/******** 5 *******japaneseRomajiStation*******" + japaneseRomajiStation);

				}
				if (column[6] != null && !column[6].equals("")) {
					idLineStation = column[6];

				}
				if (column[10] != null && !column[10].equals("")) {
					addressStation = column[10];
					System.out.println("/******** 10 *******addressStation*******" + addressStation);

				}
				if (column[11] != null && !column[11].equals("")) {
					longitudeStation = column[11];
					System.out.println("/********11 *******longitudeStation*******" + longitudeStation);

				}
				if (column[12] != null && !column[12].equals("")) {
					latitudeStation = column[12];
					System.out.println("/******** 12 *******latitudeStation*******" + latitudeStation);

				}
				if (column[13] != null && !column[13].equals("")) {
					openYmd = column[13];
					System.out.println("/******** 13 *******openYmd*******" + openYmd);

				}
				if (column[14] != null && !column[14].equals("")) {
					closeYmd = column[14];
					System.out.println("/******** 14 *******closeYmd*******" + closeYmd);

				}

				System.out.println("/*********************");
				NearestStation nearestStation = new NearestStation();

				if (idNearestStation != "" && idNearestStation != null) {
					nearestStation.setIdNearestStationCD(Integer.parseInt(idNearestStation));

				}

				if (japaneseStation != "" && japaneseStation != null) {
					nearestStation.setJapaneseStation(japaneseStation);
					System.out.println("/********japaneseStation**************");

				}
				if (japaneseKatakanaStation != "" && japaneseKatakanaStation != null) {
					nearestStation.setJapaneseKatakanaStation(japaneseKatakanaStation);
					System.out.println("/********japaneseKatakanaStation**************");

				}
				if (japaneseHiraganaStation != "" && japaneseHiraganaStation != null) {
					nearestStation.setJapaneseHiraganaStation(japaneseHiraganaStation);
					System.out.println("/********japaneseHiraganaStation**************");

				}
				if (japaneseRomajiStation != "" && japaneseRomajiStation != null) {
					nearestStation.setJapaneseRomajiStation(japaneseRomajiStation);
					System.out.println("/********japaneseKanjiStation**************");

				}

//				if (idLineStation != "" && idLineStation != null) {
//
//					int idRelatedLine = Integer.parseInt(idLineStation);
//					// idRelatedLine = idRelatedLine - 1001;
//					LineStation lineStation = new LineStation();
//					lineStation = lineStationService.getLineStationByIdLineCD(idRelatedLine);
//					if (lineStation != null) {
//						nearestStation.setLineStation(lineStation);
//					}
//
//				}

				if (addressStation != "" && addressStation != null) {
					nearestStation.setAddressStation(addressStation);
					System.out.println("/********addressStation**************");

				}
				if (longitudeStation != "" && longitudeStation != null) {
					System.out.println("-----------longitudeStation--------------------" + longitudeStation);
					if (longitudeStation.contains("\"")) {
						longitudeStation = longitudeStation.replaceAll("\"", "");
					}
					nearestStation.setLongitudeStation(Double.parseDouble(longitudeStation));
					System.out.println("/********longitudeStation**************");

				}
				if (latitudeStation != "" && latitudeStation != null) {
					System.out.println("-----------latitudeStation--------------------" + latitudeStation);

					if (latitudeStation.contains("\"")) {
						latitudeStation = latitudeStation.replaceAll("\"", "");
					}
					nearestStation.setLatitudeStation(Double.parseDouble(latitudeStation));
					System.out.println("/********latitudeStation**************");

				}
				if (openYmd != "" && openYmd != null) {
					nearestStation.setOpenYmd(openYmd);
					System.out.println("/********openYmd**************");

				}
				if (closeYmd != "" && closeYmd != null) {
					nearestStation.setCloseYmd(closeYmd);
					System.out.println("/********closeYmd**************");

				}

				nearestStationService.saveNearestStation(nearestStation);

			}
			br.close();
			System.out.println("---NEAREST STATION-------CLOSE--FINISH---UPLOAD------");

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("----NEAREST STATION------CLOSE-----Exception------");
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	/**********************************************************************/
	/**
	 * @author Wala Ben Amor
	 * 
	 *         Method to upload the Line station csv file
	 * @param request
	 */
	@RequestMapping(value = "/api/uploadLineStationFile", method = RequestMethod.GET)
	public void uploadLineStationFile() {
		try {
			

			/*******************************************/
			String line = "";

			String csvFile = "/home/djo/Téléchargements/mt_databasestation.csv";
			BufferedReader br = null;
			FileReader fr = null;
			fr = new FileReader(csvFile);
			br = new BufferedReader(fr);
			br.readLine();
			
			br.readLine();
			System.out.println("----------START---LINE--------");
			int i = 1;
			List<String> notFoundJobs = new ArrayList<String>();
			while ((line = br.readLine()) != null) {
				System.out.println("---LINE-------COMULM NUM-----------" + i);
				i = i + 1;

				String line_cd = null;
				String lineNameJapanese = null; // lineNameJapanese
				String lineNameKatakana = null;// lineNameKatakana
				String lineNameHiragana = null;// lineNameHiragana
				String lineColorCode = null;
				String lineColor = null;
				String lineType = null;
				String longitude = null;
				String latitude = null;
				String lineZoom = null;

				String[] column = line.split(",");
				if (column[0] != null && !column[0].equals("")) {
					line_cd = column[0];
				}
				if (column[2] != null && !column[2].equals("")) {
					lineNameJapanese = column[2];
				}
				if (column[3] != null && !column[3].equals("")) {
					lineNameKatakana = column[3];
				}
				if (column[4] != null && !column[4].equals("")) {
					lineNameHiragana = column[4];
				}
				if (column[5] != null && !column[5].equals("")) {
					lineColorCode = column[5];
				}
				if (column[6] != null && !column[6].equals("")) {
					lineColor = column[6];
				}
				/********************************/
				if (column[7] != null && !column[7].equals("")) {
					lineType = column[7];
				}
				if (column[8] != null && !column[8].equals("")) {
					longitude = column[8];
				}
				if (column[9] != null && !column[9].equals("")) {
					latitude = column[9];
				}
				if (column[10] != null && !column[10].equals("")) {
					lineZoom = column[10];
				}

				System.out.println("/*********************");
				LineStation lineStation = new LineStation();
				System.out.println("/******** 0 **************");
				if (line_cd != "" && line_cd != null) {
					lineStation.setLineCD(Integer.parseInt(line_cd));
				}

				System.out.println("/******** 2 **************");
				if (lineNameJapanese != "" && lineNameJapanese != null) {
					lineStation.setLineNameJapanese(lineNameJapanese);
				}
				System.out.println("/******** 3 *****************");
				if (lineNameKatakana != "" && lineNameKatakana != null) {
					lineStation.setLineNameKatakana(lineNameKatakana);
				}
				System.out.println("/******** 4 ***************");

				if (lineNameHiragana != "" && lineNameHiragana != null) {
					lineStation.setLineNameHiragana(lineNameHiragana);
				}
				System.out.println("/******** 5 **************");
				if (lineColorCode != "" && lineColorCode != null) {
					lineStation.setLineColorCode(lineColorCode);
				}
				System.out.println("/******** 6 **************");
				if (lineColor != "" && lineColor != null) {
					lineStation.setLineColor(lineColor);
				}
				System.out.println("/******** 7 **************");
				if (lineType != "" && lineType != null) {
					lineStation.setLineType(Integer.parseInt(lineType));
				}
				System.out.println("/******** 8 ************/");
				if (longitude != "" && longitude != null) {
					System.out.println("-----------longitude-----------------------" + longitude);
					System.out.println("-----------Double.parseDouble(longitude)-----------------------"
							+ Double.parseDouble(longitude));
					lineStation.setLongitude(Double.parseDouble(longitude));
				}
				System.out.println("/******** 9 ************/");
				if (latitude != "" && latitude != null) {
					lineStation.setLatitude(Double.parseDouble(latitude));
				}
				System.out.println("/******** 10 ************/");
				if (lineZoom != "" && lineZoom != null) {
					lineStation.setLineZoom(Integer.parseInt(lineZoom));
				}
				lineStationService.saveLineStation(lineStation);
				System.out.println("--save LineStation---");

			}
			br.close();
			System.out.println("---LineStation-------CLOSE--FINISH---UPLOAD------");

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("----LineStation------CLOSE-----Exception------");
		}
	}
	
}
