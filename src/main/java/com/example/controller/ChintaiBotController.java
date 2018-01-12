package com.example.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChintaiBotController {

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

	}

}
