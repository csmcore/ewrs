package app.ewarehouse.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import app.ewarehouse.master.dto.Greeting;
import app.ewarehouse.master.dto.HelloMessage;

@RestController
public class GreetingController {

	
	    @MessageMapping("/hello")
	    @SendTo("/topic/greetings")
	    public Greeting greeting(HelloMessage message) throws Exception {
	    	
	    	message.setName("chinmaya");
	        Thread.sleep(1000); // simulated delay
	        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
	    }
}
