package com.example.tool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;

import org.springframework.stereotype.Service;

/**
 * Created by gkatzioura on 4/26/17.
 */
@Service
public class AsynchronousService {

	@Autowired
	TaskExecutor taskExecutor;

	@Autowired
	ApplicationContext applicationContext;

	public void executeAsynchronously(String userId, String language, String channelToken) {

		MyThread myThread = applicationContext.getBean(MyThread.class);
		myThread.setUserId(userId, language, channelToken);
		taskExecutor.execute(myThread);
	}

}