package net.mestwin.fcmpushnotifications.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import net.mestwin.fcmpushnotifications.firebase.FCMService;
import net.mestwin.fcmpushnotifications.model.PushNotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class PushNotificationService {

    @Value("#{${app.notifications.defaults}}")
    private Map<String, String> defaults;

    private Logger logger = LoggerFactory.getLogger(PushNotificationService.class);
    private FCMService fcmService;

    public PushNotificationService(FCMService fcmService) {
        this.fcmService = fcmService;
    }


    public void sendPushNotificationWeb(PushNotificationRequest request) {
        try {
            fcmService.sendToToken(request);
        } catch ( FirebaseMessagingException e) {
            logger.error(e.getMessage());
        }
    }

    public void sendPushNotification(PushNotificationRequest request) {
        try {
            fcmService.sendMessage(getSamplePayloadData(), request);
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
        }
    }
    @Scheduled(initialDelay = 60000, fixedDelay = 60000)
    public void sendPushNotificationWeb() {
        try {
            Map<String, String> data= new HashMap<>();
            String datetime= LocalDateTime.now().toString();
            data.put("hello","world"+datetime);
            data.put("tile","chào mừng mày đến với thế giới của tao");
            // This registration token comes from the client FCM SDKs.
            String registrationToken = "cXYKuZ3AYqaD4LGbne6uET:APA91bHrKNotWBn68VmBWa07DpCv1x8fY6lgB6Bl84jntHjXG1Q3uhoE9eO5EKY-yfxCw7R0Bl-33TZc-uQ13fDRRb5FiCGCo07S-LehKDSN3XFqURywh5RwcVesAWkEbc2jFBzs1a91";
            PushNotificationRequest pushNotificationRequest = new PushNotificationRequest();
            pushNotificationRequest.setTitle("Thông báo hệ thống");
            pushNotificationRequest.setToken(registrationToken);
            fcmService.sendMessage(data,getPushNotificationRequest());
            fcmService.sendMessageWithoutData(getPushNotificationRequest());
            fcmService.sendToToken(pushNotificationRequest);
        } catch (InterruptedException | ExecutionException | FirebaseMessagingException e) {
            logger.error(e.getMessage());
        }
    }

    public void sendPushNotificationWithoutData(PushNotificationRequest request) {
        try {
            fcmService.sendMessageWithoutData(request);
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
        }
    }


    public void sendPushNotificationToToken(PushNotificationRequest request) {
        try {
            fcmService.sendMessageToAndroid(request);
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
        }
    }


    private Map<String, String> getSamplePayloadData() {
        Map<String, String> pushData = new HashMap<>();
        pushData.put("messageId", defaults.get("payloadMessageId"));
        pushData.put("text", defaults.get("payloadData") + " " + LocalDateTime.now());
        return pushData;
    }


    private PushNotificationRequest getPushNotificationRequest() {
        PushNotificationRequest request = new PushNotificationRequest(defaults.get("title"),
                defaults.get("message"),
                defaults.get("topic"));
        return request;
    }


}
