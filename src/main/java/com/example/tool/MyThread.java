package com.example.tool;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.template.ButtonsTemplate;

/**
 * Created by gkatzioura on 10/18/17.
 */
@Component
@Scope("prototype")
public class MyThread implements Runnable {
	private String userId;
	private String language;
	private String channelToken;

	public void setUserId(String userId, String language, String channelToken) {
		this.userId = userId;
		this.language = language;
		this.channelToken = channelToken;
	}

	@Override
	public void run() {

		System.out.println("----------- start treaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaad");
		System.out.println("userId" + userId);
		System.out.println("language" + language);
		System.out.println("channelToken" + channelToken);
		try {
			Thread.sleep(15000);
			TemplateMessage templateMessage;
			System.out.println("thread  english");
			ButtonsTemplate buttonsTemplate = new ButtonsTemplate(null, null, "こんなお部屋はどうですか？",
					Arrays.asList(new MessageAction("More Rooms", "もっと欲しい"), new MessageAction("Other Rooms", "他を探したい"),
							new MessageAction("It's good", "素晴らしい")));
			templateMessage = new TemplateMessage("こんなお部屋はどうですか？", buttonsTemplate);
			PushMessage pushMessage = new PushMessage(userId, templateMessage);
			LineMessagingServiceBuilder.create(channelToken).build().pushMessage(pushMessage).execute();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("-----------end threaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaad");
	}
}