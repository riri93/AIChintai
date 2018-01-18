package com.example.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.BotInformation;
import com.example.entity.Candidate;
import com.example.entity.Room;
import com.example.entity.NearestStation;
import com.example.repository.BotInformationRepository;
import com.example.repository.CandidateRepository;
import com.example.repository.NearestStationRepository;
import com.example.tool.AsynchronousService;
import com.example.repository.RoomRepository;
import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.TextMessage;

import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.profile.UserProfileResponse;

import retrofit2.Response;

@RestController
public class ChintaiBotController {

	private String CHANNEL_ACCESS_TOKEN = "Zc6e1XlR7/4a6uNGB5mQAF21zDgCct5C43eL4DrKdfdXX73vGvFtvVPQbTcZNwbHulPADQCtLMKjwI34KA1aZh9hSiFej9tf8UOE/4x8N5gdTcpef1jMVe2ly8ZWhmyH+LKlNOGJFlZeiCjBSo35AgdB04t89/1O/w1cDnyilFU=";

	private String urlDomain = "http://ec2-52-23-16-23.compute-1.amazonaws.com";

	@Autowired
	CandidateRepository candidateRepository;

	@Autowired
	BotInformationRepository botInformationRepository;

	@Autowired
	NearestStationRepository nearestStationRepository;

	@Autowired
	RoomRepository roomRepository;

	@Autowired
	AsynchronousService anAsynchronousService;

	/**
	 * @author Nour
	 * @author Rihab Kallel
	 * @author Wala
	 * @author Rihab Mabrouk
	 * @author Djo
	 * 
	 *         method to exchange messages between candidate and bot using intents
	 *         from Dialog follow api
	 * 
	 * @param obj
	 *            json object that illustrates the user's message sent via LINE bot
	 * 
	 * @return json object that illustrates the bot's reply
	 * 
	 * @throws JSONException
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(value = "/webhookAIChintai", method = RequestMethod.POST)
	private void webhook(@RequestBody Map<String, Object> obj) throws JSONException, IOException {

		JSONObject jsonResult = new JSONObject(obj);

		System.out.println("******************JSON RESULT**************");
		System.out.println(jsonResult);

		JSONObject rsl = jsonResult.getJSONObject("originalRequest");
		JSONObject data = rsl.getJSONObject("data");
		JSONObject source = data.getJSONObject("source");
		JSONObject message = data.getJSONObject("message");
		String userId = source.getString("userId");
		String customerMessage = message.getString("text");
		String timestamp = jsonResult.getString("timestamp");
		String language = jsonResult.getString("lang");
		String sessionId = jsonResult.getString("sessionId");
		JSONObject result = jsonResult.getJSONObject("result");
		JSONObject metadata = result.getJSONObject("metadata");
		String intentName = metadata.getString("intentName");
		JSONObject parameters = result.getJSONObject("parameters");
		JSONObject fulfillment = result.getJSONObject("fulfillment");
		String speech = fulfillment.getString("speech");

		System.out.println("********************LANGUAGE : " + language);
		System.out.println("*******************INTENT NAME : " + intentName);
		System.out.println("********************CUSTOMER MESSAGE : " + customerMessage);

		Candidate candidate = new Candidate();

		try {

			Response<UserProfileResponse> lineResponse = LineMessagingServiceBuilder.create(CHANNEL_ACCESS_TOKEN)
					.build().getProfile(userId).execute();
			String nameUser = "";
			String pictureUrl = "";
			if (lineResponse.isSuccessful()) {
				UserProfileResponse profile = lineResponse.body();
				nameUser = profile.getDisplayName();
				pictureUrl = profile.getPictureUrl();
			} else {
				System.out.println(lineResponse.code() + " " + lineResponse.message());
			}

			// create candidate if not registered
			if (candidateRepository.findByUserLineId(userId) == null) {
				Candidate candidateToRegister = new Candidate();

				if (nameUser != null && !nameUser.equals("")) {
					candidateToRegister.setUserName(nameUser);
				}
				if (nameUser != null && !nameUser.equals("")) {
					candidateToRegister.setProfilePicture(pictureUrl);
				}
				candidateToRegister.setUserLineId(userId);
				candidateRepository.saveAndFlush(candidateToRegister);
			}

			candidate = candidateRepository.findByUserLineId(userId);

			// create bot information of null
			if (candidate.getBotInformation() == null) {
				System.out.println("*****create botInformation********");
				BotInformation botInformation = new BotInformation();
				botInformation.setIntentName("search room");
				botInformationRepository.saveAndFlush(botInformation);

				candidate.setBotInformation(botInformation);
				candidateRepository.saveAndFlush(candidate);
			}

			// searching station intent
			if (intentName.equals("Default Fallback Intent")) {

				BotInformation botInformation = candidate.getBotInformation();
				botInformation.setIntentName("Default Fallback Intent");
				botInformation.setStationToSearch(customerMessage);
				botInformationRepository.saveAndFlush(botInformation);

				Page nearestStations;

				System.out.println("********************customerMessage********************************"
						+ candidate.getBotInformation().getStationToSearch());
				nearestStations = nearestStationRepository
						.findStations(candidate.getBotInformation().getStationToSearch(), new PageRequest(0, 3));
				System.out.println("********************nearestStations********************************"
						+ nearestStations.getContent().size());
				System.out.println("********** TOTAL PAGES *********** " + nearestStations.getTotalPages());
				if (nearestStations.getContent().size() > 0) {

					List<Action> messageActions = new ArrayList<Action>();
					List<Object[]> nearStationObj = nearestStations.getContent();

					for (Object[] ObjNearestStation : nearStationObj) {
						NearestStation station = nearestStationRepository
								.findNearStationById((int) ObjNearestStation[0]);
						String lineName = station.getLineStation().getLineNameJapanese();
						if (lineName.length() > 6) {
							lineName = lineName.substring(0, 6);
						}
						byte[] lineNameStation = lineName.getBytes(StandardCharsets.UTF_8);
						String lineNameJapanese = new String(lineNameStation, StandardCharsets.UTF_8);

						String stationName = station.getJapaneseStation();
						if (station.getJapaneseStation().length() > 6) {
							stationName = station.getJapaneseStation().substring(0, 6);
						}

						byte[] b = stationName.getBytes(StandardCharsets.UTF_8); // Explicit,
						String japanesString = new String(b, StandardCharsets.UTF_8);
						MessageAction ma = new MessageAction(japanesString + "駅 | " + lineNameJapanese,
								station.getJapaneseStation() + " 駅 | " + station.getLineStation().getLineNameJapanese()
										+ " の近くで探しています。");
						messageActions.add(ma);
					}

					String neareststationNA = "いいえ。私が探している駅ではありません。";
					messageActions.add(new MessageAction("違う", neareststationNA));
					ButtonsTemplate buttonsTemplate = new ButtonsTemplate(null, null,
							botInformation.getStationToSearch() + "はこの駅のことですか？", messageActions);
					TemplateMessage templateMessage = new TemplateMessage("駅", buttonsTemplate);

					PushMessage pushMessage = new PushMessage(userId, templateMessage);
					LineMessagingServiceBuilder.create(CHANNEL_ACCESS_TOKEN).build().pushMessage(pushMessage).execute();

				} else {
					TextMessage textMessage = new TextMessage("ごめんなさい。駅が見つかりませんでした。勉強不足です。。。");
					PushMessage pushMessage = new PushMessage(userId, textMessage);
					LineMessagingServiceBuilder.create(CHANNEL_ACCESS_TOKEN).build().pushMessage(pushMessage).execute();
				}

			}

			// search for station and display distance
			if (intentName.equals("station")) {

				BotInformation botInformation = new BotInformation();
				botInformation = candidate.getBotInformation();

				String stationToSearch = "";

				if (customerMessage.contains("の近くで探しています。")) {
					stationToSearch = customerMessage.replace("の近くで探しています。", "");
				}

				if (customerMessage.contains("駅")) {
					stationToSearch = stationToSearch.replace(" 駅 ", "");
				}

				botInformation.setStatus(2);
				botInformation.setIntentName("station");
				botInformation.setStationToSearch(stationToSearch);
				botInformationRepository.saveAndFlush(botInformation);

				ButtonsTemplate buttonsTemplate = new ButtonsTemplate(null, null, "どれぐらい近いほうがいいですか？", Arrays.asList(
						new MessageAction("徒歩5分以内", "徒歩5分以内 がいいです。"), new MessageAction("徒歩10分以内", "徒歩10分以内 がいいです。"),
						new MessageAction("徒歩20分以内", "徒歩20分以内 がいいです。"), new MessageAction("気にしない", "駅からの距離は気にしません。")));
				TemplateMessage templateMessage = new TemplateMessage("言語を選択してください。", buttonsTemplate);

				PushMessage pushMessage = new PushMessage(userId, templateMessage);
				LineMessagingServiceBuilder.create(CHANNEL_ACCESS_TOKEN).build().pushMessage(pushMessage).execute();

			}

			// station not available first time intent
			if (intentName.equals("station not available")) {
				BotInformation botInformation = new BotInformation();
				botInformation = candidate.getBotInformation();
				botInformation.setIntentName("station not available");
				botInformationRepository.saveAndFlush(botInformation);

				Page nearestStations;

				nearestStations = nearestStationRepository
						.findStations(candidate.getBotInformation().getStationToSearch(), new PageRequest(1, 3));

				if (nearestStations.getContent().size() > 0) {

					botInformationRepository.saveAndFlush(botInformation);
					List<Action> messageActions = new ArrayList<Action>();
					List<Object[]> nearStationObj = nearestStations.getContent();

					for (Object[] ObjNearestStation : nearStationObj) {
						NearestStation station = nearestStationRepository
								.findNearStationById((int) ObjNearestStation[0]);
						String lineName = station.getLineStation().getLineNameJapanese();
						if (lineName.length() > 6) {
							lineName = lineName.substring(0, 6);
						}
						byte[] lineNameStation = lineName.getBytes(StandardCharsets.UTF_8);
						String lineNameJapanese = new String(lineNameStation, StandardCharsets.UTF_8);

						String stationName = station.getJapaneseStation();
						if (station.getJapaneseStation().length() > 6) {
							stationName = station.getJapaneseStation().substring(0, 6);
						}

						byte[] b = stationName.getBytes(StandardCharsets.UTF_8); // Explicit,
						String japanesString = new String(b, StandardCharsets.UTF_8);
						MessageAction ma = new MessageAction(japanesString + "駅 | " + lineNameJapanese,
								station.getJapaneseStation() + " 駅 | " + station.getLineStation().getLineNameJapanese()
										+ " の近くで探しています。");
						messageActions.add(ma);
					}

					String neareststationNA = "いいえ。これらも私が探している駅ではありません。";
					messageActions.add(new MessageAction("違う", neareststationNA));
					ButtonsTemplate buttonsTemplate = new ButtonsTemplate(null, null,
							botInformation.getStationToSearch() + "はこの駅のことですか？", messageActions);
					TemplateMessage templateMessage = new TemplateMessage("駅", buttonsTemplate);

					PushMessage pushMessage = new PushMessage(userId, templateMessage);
					LineMessagingServiceBuilder.create(CHANNEL_ACCESS_TOKEN).build().pushMessage(pushMessage).execute();

				} else {
					TextMessage textMessage = new TextMessage("ごめんなさい。駅が見つかりませんでした。勉強不足です。。。");
					PushMessage pushMessage = new PushMessage(userId, textMessage);
					LineMessagingServiceBuilder.create(CHANNEL_ACCESS_TOKEN).build().pushMessage(pushMessage).execute();
				}
			}

			// if the user choose other rooms
			if (intentName.equals("other rooms")) {
				BotInformation botInformation = candidate.getBotInformation();
				TextMessage textMessage = new TextMessage("どの駅の近くでお部屋を探していますか？");
				PushMessage pushMessage = new PushMessage(userId, textMessage);
				LineMessagingServiceBuilder.create(CHANNEL_ACCESS_TOKEN).build().pushMessage(pushMessage).execute();
				botInformation.setIntentName("other rooms");
				botInformationRepository.saveAndFlush(botInformation);
			}

			// if the user choose more rooms
			if (intentName.equals("more rooms")) {
				BotInformation botInformation = candidate.getBotInformation();
				botInformation.setIntentName("more rooms");
				botInformation.setPageMoreRooms(botInformation.getPageMoreRooms() + 5);
				botInformationRepository.saveAndFlush(botInformation);

				TextMessage textMessage = new TextMessage("分かりました。今探してみますね。");
				PushMessage pushMessage = new PushMessage(userId, textMessage);
				LineMessagingServiceBuilder.create(CHANNEL_ACCESS_TOKEN).build().pushMessage(pushMessage).execute();

				/********** Search for Rooms ************/
				searchMoreRooms(candidate, userId, CHANNEL_ACCESS_TOKEN, timestamp);
				/**********************/
			}

			// when user clicks search room in the menu
			if (intentName.equals("search room")) {

				BotInformation botInformation = candidate.getBotInformation();
				TextMessage textMessage = new TextMessage("どの駅の近くでお部屋を探していますか？");
				PushMessage pushMessage = new PushMessage(userId, textMessage);
				LineMessagingServiceBuilder.create(CHANNEL_ACCESS_TOKEN).build().pushMessage(pushMessage).execute();
				botInformation.setIntentName("search room");
				botInformationRepository.saveAndFlush(botInformation);

			}

			// if user answers it's good for how was it question
			if (intentName.equals("its good rooms")) {
				BotInformation botInformation = new BotInformation();
				botInformation = candidate.getBotInformation();
				botInformation.setIntentName("its good rooms");
				botInformationRepository.saveAndFlush(botInformation);

				TextMessage textMessage = new TextMessage("ありがとうございます！");
				PushMessage pushMessage = new PushMessage(userId, textMessage);
				LineMessagingServiceBuilder.create(CHANNEL_ACCESS_TOKEN).build().pushMessage(pushMessage).execute();

			}

			// if user select the distance
			if (intentName.equals("distance")) {
				String distanceToSearch = "";

				if (parameters != null && parameters.getString("distance") != null) {
					distanceToSearch = parameters.getString("distance");
				} else {
					if (!customerMessage.equals("駅からの距離は気にしません。")) {
						if (customerMessage.contains("がいいです。")) {
							distanceToSearch = customerMessage.replace(" がいいです。", "");
						}
					} else {
						distanceToSearch = "駅からの距離は気にしません。";
					}
				}

				BotInformation botInformation = new BotInformation();
				botInformation = candidate.getBotInformation();
				botInformation.setDistanceToSearch(distanceToSearch);
				botInformation.setIntentName("distance");
				botInformationRepository.saveAndFlush(botInformation);

				ButtonsTemplate buttonsTemplate = new ButtonsTemplate(null, null, "家賃はいくらがいいですか？", Arrays.asList(
						new MessageAction("5万円未満", "5万円未満 がいいです。"), new MessageAction("7万円未満", "7万円未満 がいいです。"),
						new MessageAction("10万円未満", "10万円未満 がいいです。"), new MessageAction("気にしない", "家賃は気にしません。")));
				TemplateMessage templateMessage = new TemplateMessage("家賃はいくらがいいですか？", buttonsTemplate);

				PushMessage pushMessage = new PushMessage(userId, templateMessage);
				LineMessagingServiceBuilder.create(CHANNEL_ACCESS_TOKEN).build().pushMessage(pushMessage).execute();

			}

			// if nearest station is not available again
			if (intentName.equals("station not available again")) {
				BotInformation botInformation = new BotInformation();
				botInformation = candidate.getBotInformation();
				botInformation.setIntentName("station not available again");
				botInformationRepository.saveAndFlush(botInformation);

				TextMessage textMessage = new TextMessage("ごめんなさい。駅が見つかりませんでした。勉強不足です。。。");
				PushMessage pushMessage = new PushMessage(userId, textMessage);
				LineMessagingServiceBuilder.create(CHANNEL_ACCESS_TOKEN).build().pushMessage(pushMessage).execute();
			}

			// if the user choose a price
			if (intentName.equals("price")) {

				String priceToSearch = "";

				if (parameters != null && parameters.getString("price") != null) {
					priceToSearch = parameters.getString("price");
				} else if (!customerMessage.equals("家賃は気にしません。")) {
					if (customerMessage.contains("がいいです。")) {
						priceToSearch = customerMessage.replace(" がいいです。", "");
					} else {
						priceToSearch = "家賃は気にしません。";
					}
				}

				BotInformation botInformation = new BotInformation();
				botInformation = candidate.getBotInformation();
				botInformation.setIntentName("price");
				botInformation.setPriceToSearch(priceToSearch);
				botInformationRepository.saveAndFlush(botInformation);

				TextMessage textMessage = new TextMessage("分かりました。今探してみますね。");
				PushMessage pushMessage = new PushMessage(userId, textMessage);
				LineMessagingServiceBuilder.create(CHANNEL_ACCESS_TOKEN).build().pushMessage(pushMessage).execute();

				/********** Search for Rooms ************/
				searchRooms(candidate, userId, CHANNEL_ACCESS_TOKEN, timestamp);
				/**********************/

			}

			// if user doesn't want to search again
			if (intentName.equals("No, I don't want to search with another condition.")) {
				BotInformation botInformation = new BotInformation();
				botInformation = candidate.getBotInformation();
				botInformation.setIntentName("No, I don't want to search with another condition.");
				botInformationRepository.saveAndFlush(botInformation);

				TextMessage textMessage = new TextMessage("分かりました。また、お部屋を探すときは言ってくださいね。");
				PushMessage pushMessage = new PushMessage(userId, textMessage);
				LineMessagingServiceBuilder.create(CHANNEL_ACCESS_TOKEN).build().pushMessage(pushMessage).execute();
			}

			// if user clicks on help button in the menu
			if (intentName.equals("help")) {
				BotInformation botInformation = new BotInformation();
				botInformation = candidate.getBotInformation();
				botInformation.setIntentName("help");
				botInformationRepository.saveAndFlush(botInformation);

				TextMessage textMessage = new TextMessage("お問い合わせ先はこちらです。");
				PushMessage pushMessage = new PushMessage(userId, textMessage);
				LineMessagingServiceBuilder.create(CHANNEL_ACCESS_TOKEN).build().pushMessage(pushMessage).execute();
			}

			// if user clicks on recomment room in the menu
			if (intentName.equals("recommend room")) {
				BotInformation botInformation = new BotInformation();
				botInformation = candidate.getBotInformation();
				botInformation.setIntentName("recommend room");
				botInformationRepository.saveAndFlush(botInformation);

				/********** Search for recommended Rooms ************/
				searchRecommendedRooms(candidate, userId, CHANNEL_ACCESS_TOKEN, timestamp);
				/**********************/
			}

		} catch (Exception e) {

			ButtonsTemplate buttonsTemplate = new ButtonsTemplate(null, null, "ごめんなさい。どういう意味か分かりません。もしかして、", Arrays
					.asList(new MessageAction("お部屋を探したい", "お部屋を探したいです。"), new MessageAction("質問がある", "質問があります。")));
			TemplateMessage templateMessage = new TemplateMessage("ごめんなさい。どういう意味か分かりません。もしかして、", buttonsTemplate);

			PushMessage pushMessage = new PushMessage(userId, templateMessage);
			LineMessagingServiceBuilder.create(CHANNEL_ACCESS_TOKEN).build().pushMessage(pushMessage).execute();

			e.printStackTrace();

		}

	}

	/**
	 * @author Wala Ben Amor
	 * 
	 *         Method to send the list of rooms
	 * @param candidate
	 * @param userId
	 * @param CHANNEL_ACCESS_TOKEN
	 * @param timestamp
	 * @param rooms
	 * @throws IOException
	 * @throws JSONException
	 */
	public void sendCarouselRooms(Candidate candidate, String userId, String CHANNEL_ACCESS_TOKEN, String timestamp,
			List<Room> rooms) throws IOException, JSONException {

		System.out.println("*******************CAROUSEL*********************");
		System.out.println("*****************ROOM SIZE: " + rooms.size());
		java.util.List<CarouselColumn> columns = new ArrayList<CarouselColumn>();

		for (Room room : rooms) {

			System.out.println("************ROOM: " + room.getIdRoom());

			String title = "";

			String img = "https://cdn4.iconfinder.com/data/icons/real-estate-circular-2/90/78-256.png";

			title = room.getNameBuilding();

			if (title.length() > 39) {
				title = title.substring(0, 39);
			}
			byte[] titleByte = title.getBytes(StandardCharsets.UTF_8); // Explicit,
			title = new String(titleByte, StandardCharsets.UTF_8);

			String textToSend = "月" + room.getPrice() / 10000 + "円" + " | " + room.getBuildingType() + room.getFloor()
					+ "階 \n" + room.getRoomID() + room.getRoomType();

			if (textToSend.length() > 59) {
				textToSend = textToSend.substring(0, 59);
			}
			byte[] labelToByte = textToSend.getBytes(StandardCharsets.UTF_8); // Explicit,
			textToSend = new String(labelToByte, StandardCharsets.UTF_8);

			/**************************/

			System.out.println("title : " + title);
			System.out.println("textToSend : " + textToSend);
			System.out.println("img : " + img);

			String option = "詳細をみる";
			String link = urlDomain + "/#!/apply/" + room.getIdRoom() + "/" + candidate.getIdUserInformation();

			URIAction uriAction = new URIAction(option, link);

			CarouselColumn column = new CarouselColumn(img, title, textToSend, Arrays.asList(uriAction));
			columns.add(column);
		}

		CarouselTemplate carouselTemplate = new CarouselTemplate(columns);
		String templateText = "";
		templateText = "Which building?";

		TemplateMessage templateMessage = new TemplateMessage(templateText, carouselTemplate);
		PushMessage pushMessage = new PushMessage(userId, templateMessage);

		try {
			LineMessagingServiceBuilder.create(CHANNEL_ACCESS_TOKEN).build().pushMessage(pushMessage).execute();
		} catch (IOException e) {
			System.out.println("Exception is raised ");
			e.printStackTrace();
		}
	}

	/**
	 * Method to search rooms by price and distance
	 * 
	 * @param candidate
	 * @param userId
	 * @param CHANNEL_ACCESS_TOKEN
	 * @param timestamp
	 */
	public void searchRooms(Candidate candidate, String userId, String CHANNEL_ACCESS_TOKEN, String timestamp) {
		List<Room> rooms = new ArrayList<Room>();
		List<Room> roomsDistance = new ArrayList<Room>();

		BotInformation botInformation = new BotInformation();
		botInformation = candidate.getBotInformation();

		String stationToSearch = botInformation.getStationToSearch();
		if (stationToSearch.endsWith(" ")) {
			stationToSearch = stationToSearch.replaceAll("\\s+$", "");
		}
		String stationName = "";
		String lineName = "";

		if (stationToSearch.indexOf("|") != -1) {
			stationName = stationToSearch.substring(0, stationToSearch.indexOf("|"));
			lineName = stationToSearch.substring(stationToSearch.indexOf("|") + 2, stationToSearch.length());
		}

		System.out.println("***********SEARCH ROOM STATION***********");
		System.out.println("***************stationToSearch: " + stationToSearch);
		System.out.println("***************stationName : " + stationName);
		System.out.println("***************lineName : " + lineName);

		NearestStation station = new NearestStation();
		station = nearestStationRepository.findStationByNameAndLine(stationName, lineName);

		if (station != null && station.getIdNearestStation() != 0) {

			int minPrice = 0;
			int maxPrice = 0;

			if (botInformation.getPriceToSearch().equals("5万円未満")) {
				minPrice = 0;
				maxPrice = 49999;
			} else if (botInformation.getPriceToSearch().equals("7万円未満")) {
				minPrice = 50000;
				maxPrice = 69999;
			} else if (botInformation.getPriceToSearch().equals("10万円未満")) {
				minPrice = 70000;
				maxPrice = 99999;
			}

			System.out.println("minPrice : " + minPrice);
			System.out.println("maxPrice : " + maxPrice);

			if (!botInformation.getPriceToSearch().equals("家賃は気にしません。")) {
				roomsDistance = roomRepository.findRoomsByPrice(minPrice, maxPrice);
			} else if (botInformation.getPriceToSearch().equals("家賃は気にしません。")) {
				roomsDistance = roomRepository.findAll();
			}

			double minDistance = 0;
			double maxDistance = 0;

			if (botInformation.getDistanceToSearch().equals("徒歩5分以内")) {
				minDistance = 0;
				maxDistance = 1;
			} else if (botInformation.getDistanceToSearch().equals("徒歩10分以内")) {
				minDistance = 1;
				maxDistance = 2;
			} else if (botInformation.getDistanceToSearch().equals("徒歩20分以内")) {
				minDistance = 2;
				maxDistance = 3;
			} else if (botInformation.getDistanceToSearch().equals("駅からの距離は気にしません。")) {
				minDistance = 3;
				maxDistance = 10;
			}

			System.out.println("minDistance : " + minDistance);
			System.out.println("maxDistance : " + maxDistance);

			System.out.println("************rooms: " + roomsDistance.size());

			HashMap roomsHashMap = new HashMap();
			List<Integer> roomsToDisplay = new ArrayList<>();

			roomsHashMap = getRoomStationsDistanceMatrix(roomsDistance, station, minDistance, maxDistance);

			System.out.println("*********roomsHashMap : " + roomsHashMap.size());

			Iterator<Map.Entry<Integer, Double>> entriesSorted = roomsHashMap.entrySet().iterator();
			while (entriesSorted.hasNext()) {
				Map.Entry<Integer, Double> entry = entriesSorted.next();
				roomsToDisplay.add(entry.getKey());
				System.out.println("ID:  " + entry.getKey() + " DISTANCE: " + entry.getValue());
			}

			Collections.shuffle(roomsToDisplay);

			if (roomsToDisplay.size() <= 5) {
				for (int i = 0; i < roomsToDisplay.size(); i++) {
					rooms.add(roomRepository.findOne(roomsToDisplay.get(i)));
				}
			} else {
				for (int i = 0; i < 5; i++) {
					rooms.add(roomRepository.findOne(roomsToDisplay.get(i)));
				}
			}

			if (rooms != null && rooms.size() != 0) {
				try {
					sendCarouselRooms(candidate, userId, CHANNEL_ACCESS_TOKEN, timestamp, rooms);
				} catch (IOException | JSONException e) {
					e.printStackTrace();
				}

				/******** THREAD ***********/
				anAsynchronousService.executeAsynchronously(userId, "japanese", CHANNEL_ACCESS_TOKEN);
				/****************/

			} else {

				ConfirmTemplate confirmTemplate = new ConfirmTemplate("条件に該当するお部屋が見つかりませんでした。別の条件でもう一度探してみますか？",
						new MessageAction("はい", "はい。別の条件でもう一度探してみます。"),
						new MessageAction("いいえ", "いいえ。別の条件で探さなくても大丈夫です。"));

				TemplateMessage templateMessage = new TemplateMessage("条件に該当するお部屋が見つかりませんでした。別の条件でもう一度探してみますか？",
						confirmTemplate);

				PushMessage pushMessage = new PushMessage(userId, templateMessage);

				try {
					LineMessagingServiceBuilder.create(CHANNEL_ACCESS_TOKEN).build().pushMessage(pushMessage).execute();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			TextMessage textMessage = new TextMessage("ごめんなさい。駅が見つかりませんでした。勉強不足です。。。");
			PushMessage pushMessage = new PushMessage(userId, textMessage);
			try {
				LineMessagingServiceBuilder.create(CHANNEL_ACCESS_TOKEN).build().pushMessage(pushMessage).execute();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Method to search rooms by price and distance
	 * 
	 * @param candidate
	 * @param userId
	 * @param CHANNEL_ACCESS_TOKEN
	 * @param timestamp
	 */
	public void searchMoreRooms(Candidate candidate, String userId, String CHANNEL_ACCESS_TOKEN, String timestamp) {
		List<Room> rooms = new ArrayList<Room>();
		List<Room> roomsDistance = new ArrayList<Room>();

		BotInformation botInformation = new BotInformation();
		botInformation = candidate.getBotInformation();

		String stationToSearch = botInformation.getStationToSearch();
		if (stationToSearch.endsWith(" ")) {
			stationToSearch = stationToSearch.replaceAll("\\s+$", "");
		}

		String stationName = "";
		String lineName = "";

		if (stationToSearch.indexOf("|") != -1) {
			stationName = stationToSearch.substring(0, stationToSearch.indexOf("|"));
			lineName = stationToSearch.substring(stationToSearch.indexOf("|") + 2, stationToSearch.length());
		}

		System.out.println("***********SEARCH ROOM STATION***********");
		System.out.println("***************stationToSearch: " + stationToSearch);
		System.out.println("***************stationName : " + stationName);
		System.out.println("***************lineName : " + lineName);

		NearestStation station = new NearestStation();
		station = nearestStationRepository.findStationByNameAndLine(stationName, lineName);

		if (station != null && station.getIdNearestStation() != 0) {

			int minPrice = 0;
			int maxPrice = 0;

			if (botInformation.getPriceToSearch().equals("5万円未満")) {
				minPrice = 0;
				maxPrice = 49999;
			} else if (botInformation.getPriceToSearch().equals("7万円未満")) {
				minPrice = 50000;
				maxPrice = 69999;
			} else if (botInformation.getPriceToSearch().equals("10万円未満")) {
				minPrice = 70000;
				maxPrice = 99999;
			}

			System.out.println("minPrice : " + minPrice);
			System.out.println("maxPrice : " + maxPrice);

			if (!botInformation.getPriceToSearch().equals("家賃は気にしません。")) {
				roomsDistance = roomRepository.findRoomsByPrice(minPrice, maxPrice);
			} else if (botInformation.getPriceToSearch().equals("家賃は気にしません。")) {
				roomsDistance = roomRepository.findAll();
			}

			double minDistance = 0;
			double maxDistance = 0;

			if (botInformation.getDistanceToSearch().equals("徒歩5分以内")) {
				minDistance = 0;
				maxDistance = 1;
			} else if (botInformation.getDistanceToSearch().equals("徒歩10分以内")) {
				minDistance = 1;
				maxDistance = 2;
			} else if (botInformation.getDistanceToSearch().equals("徒歩20分以内")) {
				minDistance = 2;
				maxDistance = 3;
			} else if (botInformation.getDistanceToSearch().equals("駅からの距離は気にしません。")) {
				minDistance = 3;
				maxDistance = 10;
			}

			System.out.println("minDistance : " + minDistance);
			System.out.println("maxDistance : " + maxDistance);

			roomsDistance = roomRepository.findRoomsByPrice(minPrice, maxPrice);

			System.out.println("************rooms: " + roomsDistance.size());

			HashMap roomsHashMap = new HashMap();
			List<Integer> roomsToDisplay = new ArrayList<>();

			roomsHashMap = getRoomStationsDistanceMatrix(roomsDistance, station, minDistance, maxDistance);

			Iterator<Map.Entry<Integer, Double>> entriesSorted = roomsHashMap.entrySet().iterator();
			while (entriesSorted.hasNext()) {
				Map.Entry<Integer, Double> entry = entriesSorted.next();
				roomsToDisplay.add(entry.getKey());
				System.out.println("ID:  " + entry.getKey() + " DISTANCE: " + entry.getValue());
			}

			Collections.shuffle(roomsToDisplay);

			if (roomsToDisplay.size() > 5) {
				if (roomsToDisplay.size() <= botInformation.getPageMoreRooms() + 5) {
					for (int i = botInformation.getPageMoreRooms(); i < roomsToDisplay.size(); i++) {
						rooms.add(roomRepository.findOne(roomsToDisplay.get(i)));
					}
				} else {

					for (int i = botInformation.getPageMoreRooms(); i < botInformation.getPageMoreRooms() + 5; i++) {
						rooms.add(roomRepository.findOne(roomsToDisplay.get(i)));
					}
				}
			}

			if (rooms != null && rooms.size() != 0) {
				try {
					sendCarouselRooms(candidate, userId, CHANNEL_ACCESS_TOKEN, timestamp, rooms);
				} catch (IOException | JSONException e) {
					e.printStackTrace();
				}

				/******** THREAD ***********/
				anAsynchronousService.executeAsynchronously(userId, "japanese", CHANNEL_ACCESS_TOKEN);
				/****************/
			} else {
				ConfirmTemplate confirmTemplate = new ConfirmTemplate("条件に該当するお部屋が見つかりませんでした。別の条件でもう一度探してみますか？",
						new MessageAction("はい", "はい。別の条件でもう一度探してみます。"),
						new MessageAction("いいえ", "いいえ。別の条件で探さなくても大丈夫です。"));

				TemplateMessage templateMessage = new TemplateMessage("条件に該当するお部屋が見つかりませんでした。別の条件でもう一度探してみますか？",
						confirmTemplate);

				PushMessage pushMessage = new PushMessage(userId, templateMessage);

				try {
					LineMessagingServiceBuilder.create(CHANNEL_ACCESS_TOKEN).build().pushMessage(pushMessage).execute();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			TextMessage textMessage = new TextMessage("ごめんなさい。駅が見つかりませんでした。勉強不足です。。。");
			PushMessage pushMessage = new PushMessage(userId, textMessage);
			try {
				LineMessagingServiceBuilder.create(CHANNEL_ACCESS_TOKEN).build().pushMessage(pushMessage).execute();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Method to search recommended rooms
	 * 
	 * @param candidate
	 * @param userId
	 * @param CHANNEL_ACCESS_TOKEN
	 * @param timestamp
	 */
	public void searchRecommendedRooms(Candidate candidate, String userId, String CHANNEL_ACCESS_TOKEN,
			String timestamp) {
		List<Room> rooms = new ArrayList<Room>();
		List<Room> roomsToDisplay = new ArrayList<Room>();

		rooms = roomRepository.findAll();

		Collections.shuffle(rooms);

		if (rooms.size() <= 5) {
			for (int i = 0; i < rooms.size(); i++) {
				roomsToDisplay.add(rooms.get(i));
			}
		} else {
			for (int i = 0; i < 5; i++) {
				roomsToDisplay.add(rooms.get(i));
			}
		}

		if (roomsToDisplay != null && roomsToDisplay.size() != 0) {
			try {
				sendCarouselRooms(candidate, userId, CHANNEL_ACCESS_TOKEN, timestamp, roomsToDisplay);
			} catch (IOException | JSONException e) {
				e.printStackTrace();
			}

			TextMessage textMessage = new TextMessage("どうぞ！");
			PushMessage pushMessage = new PushMessage(userId, textMessage);
			try {
				LineMessagingServiceBuilder.create(CHANNEL_ACCESS_TOKEN).build().pushMessage(pushMessage).execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			ConfirmTemplate confirmTemplate = new ConfirmTemplate("条件に該当するお部屋が見つかりませんでした。別の条件でもう一度探してみますか？",
					new MessageAction("はい", "はい。別の条件でもう一度探してみます。"), new MessageAction("いいえ", "いいえ。別の条件で探さなくても大丈夫です。"));

			TemplateMessage templateMessage = new TemplateMessage("条件に該当するお部屋が見つかりませんでした。別の条件でもう一度探してみますか？",
					confirmTemplate);

			PushMessage pushMessage = new PushMessage(userId, templateMessage);

			try {
				LineMessagingServiceBuilder.create(CHANNEL_ACCESS_TOKEN).build().pushMessage(pushMessage).execute();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 *
	 * @author Rihab Kallel
	 * 
	 *         method to get the distance between destination and origin addresses
	 * 
	 * 
	 * @param jobsDistance
	 *            : list of jobs ; as in destinations
	 * @param nearestStation
	 *            station chosen by user; as in origin
	 */
	public HashMap getRoomStationsDistanceMatrix(List<Room> roomsDistance, NearestStation station, double minDistance,
			double maxDistance) {
		HashMap<Integer, Double> jobsHashMap = new HashMap<>();

		if (station.getLatitudeStation() != null && station.getLongitudeStation() != null) {
			for (Room room : roomsDistance) {
				final int R = 6371; // Radius of the earth
				if (room.getLatitudeRoom() != null && room.getLongitudeRoom() != null) {
					double latDistance = Math.toRadians(room.getLatitudeRoom() - station.getLatitudeStation());
					double lonDistance = Math.toRadians(room.getLongitudeRoom() - station.getLongitudeStation());
					double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
							+ Math.cos(Math.toRadians(station.getLatitudeStation()))
									* Math.cos(Math.toRadians(room.getLatitudeRoom())) * Math.sin(lonDistance / 2)
									* Math.sin(lonDistance / 2);
					double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
					double d = R * c;

					double height = 0;

					d = Math.pow(d, 2) + Math.pow(height, 2);

					double distance = Math.sqrt(d);

					System.out.println("****************DISTANCESSSSSSSSSSS************** : " + distance);

					if (distance >= minDistance && distance <= maxDistance) {
						jobsHashMap.put(room.getIdRoom(), distance);
					}
				}
			}
		}
		return jobsHashMap;
	}

}
