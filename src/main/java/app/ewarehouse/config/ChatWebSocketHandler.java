package app.ewarehouse.config;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.master.dto.Greeting;

public class ChatWebSocketHandler extends TextWebSocketHandler {

	private List<WebSocketSession> sessions = new ArrayList<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();
		Greeting greeting = new Greeting("Hello Welcome to Web Socket i am from after connection");

		for (int i = 0; i < 10; i++) {
			TextMessage message = new TextMessage(objectMapper.writeValueAsString(greeting));
			session.sendMessage(message);
			Thread.sleep(1000);
		}

		sessions.add(session);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

		
		  String payload = message.getPayload();
		  
		  
		  JSONObject jsonObj=new JSONObject(payload);
		  
		  String payloadData=jsonObj.getString("data");
		  System.out.println(payloadData);
		 

		ObjectMapper objectMapper = new ObjectMapper();
		Greeting greeting = new Greeting("Hello Welcome to Web Socket"+payloadData);
		for (WebSocketSession webSocketSession : sessions) {

			message = new TextMessage(objectMapper.writeValueAsString(greeting));
			webSocketSession.sendMessage(message);
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status)
			throws Exception {
		sessions.remove(session);
	}
}
