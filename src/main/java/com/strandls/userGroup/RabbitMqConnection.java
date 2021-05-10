/**
 * 
 */
package com.strandls.userGroup;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author Abhishek Rudra
 *
 */
public class RabbitMqConnection {

	public final static String EXCHANGE_BIODIV = "biodiv";
	private final static String OBSERVATION_QUEUE = "observationQueue";
	private final static String ROUTING_OBSERVATION = "observation";

	public final static String MAILING_QUEUE;
	public final static String MAILING_ROUTINGKEY;

	private final static String RABBITMQ_HOST;
	private final static Integer RABBITMQ_PORT;
	private final static String RABBITMQ_USERNAME;
	private final static String RABBITMQ_PASSWORD;

	static {
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");

		Properties properties = new Properties();
		try {
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}

		RABBITMQ_HOST = properties.getProperty("rabbitmq_host");
		RABBITMQ_PORT = Integer.parseInt(properties.getProperty("rabbitmq_port"));
		RABBITMQ_USERNAME = properties.getProperty("rabbitmq_username");
		RABBITMQ_PASSWORD = properties.getProperty("rabbitmq_password");

		MAILING_QUEUE = properties.getProperty("rabbitmq_queue");
		MAILING_ROUTINGKEY = properties.getProperty("rabbitmq_routingKey");
		try {
			in.close();
		} catch (IOException e) {

		}
	}

	public Channel setRabbitMQConnetion() throws IOException, TimeoutException {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(RABBITMQ_HOST);
		factory.setPort(RABBITMQ_PORT);
		factory.setUsername(RABBITMQ_USERNAME);
		factory.setPassword(RABBITMQ_PASSWORD);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_BIODIV, "direct");
		channel.queueDeclare(OBSERVATION_QUEUE, false, false, false, null);
		channel.queueBind(OBSERVATION_QUEUE, EXCHANGE_BIODIV, ROUTING_OBSERVATION);
		channel.queueDeclare(MAILING_QUEUE, false, false, false, null);
		channel.queueBind(MAILING_QUEUE, EXCHANGE_BIODIV, MAILING_ROUTINGKEY);

		return channel;

	}
}
