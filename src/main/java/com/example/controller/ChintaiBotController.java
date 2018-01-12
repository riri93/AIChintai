package com.example.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.CancelablePrintJob;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.BotInformation;
import com.example.entity.Candidate;
import com.example.entity.Room;
import com.example.repository.BotInformationRepository;
import com.example.repository.CandidateRepository;
import com.example.tool.AsynchronousService;
import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.profile.UserProfileResponse;

import retrofit2.Response;

@RestController
public class ChintaiBotController {

	@Autowired
	CandidateRepository candidateRepository;

	@Autowired
	BotInformationRepository botInformationRepository;

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
	@RequestMapping(value = "/webhookNewBot", method = RequestMethod.POST)
	private void webhook(@RequestBody Map<String, Object> obj) throws JSONException, IOException {

		String CHANNEL_ACCESS_TOKEN = "Zc6e1XlR7/4a6uNGB5mQAF21zDgCct5C43eL4DrKdfdXX73vGvFtvVPQbTcZNwbHulPADQCtLMKjwI34KA1aZh9hSiFej9tf8UOE/4x8N5gdTcpef1jMVe2ly8ZWhmyH+LKlNOGJFlZeiCjBSo35AgdB04t89/1O/w1cDnyilFU=";

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

		Response<UserProfileResponse> lineResponse = LineMessagingServiceBuilder.create(CHANNEL_ACCESS_TOKEN).build()
				.getProfile(userId).execute();
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
			BotInformation botInformation = new BotInformation();
			botInformation.setIntentName("search room");
			botInformationRepository.saveAndFlush(botInformation);

			candidate.setBotInformation(botInformation);
			candidateRepository.saveAndFlush(candidate);
		}

		// search for station
		if (intentName.equals("station")) {
			BotInformation botInformation = new BotInformation();
			botInformation = candidate.getBotInformation();

			String stationToSearch = "";

			if (customerMessage.contains("の近くで探しています。")) {
				stationToSearch = customerMessage.replace("の近くで探しています。", "");
			} else if (customerMessage.contains("I want to find a job near ")) {
				stationToSearch = customerMessage.replace("I want to find a job near ", "");
			}

			if (customerMessage.contains("駅")) {
				stationToSearch = stationToSearch.replace(" 駅 ", "");
			} else if (customerMessage.contains("Station")) {
				stationToSearch = stationToSearch.replace(" Station ", "");
			} else if (customerMessage.contains("station")) {
				stationToSearch = stationToSearch.replace(" station ", "");
			}

			botInformation.setStationToSearch(stationToSearch);
			botInformationRepository.saveAndFlush(botInformation);

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
		java.util.List<CarouselColumn> columns = new ArrayList<CarouselColumn>();

		for (Room room : rooms) {

			String title = "";

			String img = null;

			title = room.getNameBuilding();

			if (title.length() > 39) {
				title = title.substring(0, 39);
			}
			byte[] titleByte = title.getBytes(StandardCharsets.UTF_8); // Explicit,
			title = new String(titleByte, StandardCharsets.UTF_8);

			/*************************/
			String textToSend = room.getNameBuilding() + " | " + room.getPostCode();

			if (textToSend.length() > 59) {
				textToSend = textToSend.substring(0, 59);
			}
			byte[] labelToByte = textToSend.getBytes(StandardCharsets.UTF_8); // Explicit,
			textToSend = new String(labelToByte, StandardCharsets.UTF_8);
			/**************************/

			String detail = "Detail";

			String option = "Option";

			// String link = "月" + room.getPrice() + "円";

			// URIAction uriAction1 = new URIAction(detail, link);
			MessageAction messageAction1 = new MessageAction(detail, "月" + room.getPrice() + "円");
			MessageAction messageAction2 = new MessageAction(option, "詳細をみる");

			CarouselColumn column = new CarouselColumn(img, title, textToSend,
					Arrays.asList(messageAction1, messageAction2));
			columns.add(column);
		}

		CarouselTemplate carouselTemplate = new CarouselTemplate(columns);
		String templateText = "";
		templateText = "Which building?";

		TemplateMessage templateMessage = new TemplateMessage(templateText, carouselTemplate);
		PushMessage pushMessage = new PushMessage(userId, templateMessage);

		try {
			LineMessagingServiceBuilder.create(CHANNEL_ACCESS_TOKEN).build().pushMessage(pushMessage).execute();
			/******** THREAD ***********/
			anAsynchronousService.executeAsynchronously(userId, "japanese", CHANNEL_ACCESS_TOKEN);
			/****************/
		} catch (IOException e) {
			System.out.println("Exception is raised ");
			e.printStackTrace();
		}
	}

}
