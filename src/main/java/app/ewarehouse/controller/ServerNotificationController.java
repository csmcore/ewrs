package app.ewarehouse.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
@RestController
public class ServerNotificationController {
	 @GetMapping("/notifications")
	    public SseEmitter streamNotifications() {
	        SseEmitter emitter = new SseEmitter();
	        new Thread(() -> {
	            try {
	                // Simulate sending notifications
	                emitter.send("New notification!");
	                Thread.sleep(1000);
	                emitter.send("Another notification!");
	                emitter.complete();
	            } catch (IOException e) {
	                emitter.completeWithError(e);
	            }catch(InterruptedException e) {
	            	Thread.currentThread().interrupt();
	            	emitter.completeWithError(e);
	            }
	        }).start();
	        return emitter;
	    }
}
