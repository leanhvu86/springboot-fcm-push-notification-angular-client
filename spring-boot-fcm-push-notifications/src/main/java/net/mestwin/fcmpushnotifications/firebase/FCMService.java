package net.mestwin.fcmpushnotifications.firebase;

import com.google.firebase.messaging.*;
import net.mestwin.fcmpushnotifications.model.PushNotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class FCMService {

    private Logger logger = LoggerFactory.getLogger(FCMService.class);

    public void sendMessage(Map<String, String> data, PushNotificationRequest request)
            throws InterruptedException, ExecutionException {
        Message message = getPreconfiguredMessageWithData(data, request);
        String response = sendAndGetResponse(message);
        logger.info("Sent message with data. Topic: " + request.getTopic() + ", " + response);
    }

    public void sendMessageWithoutData(PushNotificationRequest request)
            throws InterruptedException, ExecutionException {
        Message message = getPreconfiguredMessageWithoutData(request);
        String response = sendAndGetResponse(message);
        logger.info("Sent message without data. Topic: " + request.getTopic() + ", " + response);
    }

    public void sendMessageToAndroid(PushNotificationRequest request)
            throws InterruptedException, ExecutionException {
        Message message = getPreconfiguredMessageToAndroid(request);
        String response = sendAndGetResponse(message);
        logger.info("Sent message to token. Device token: " + request.getToken() + ", " + response);
    }

    private String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }

    private AndroidConfig getAndroidConfig(String topic) {
        return AndroidConfig.builder()
                .setTtl(Duration.ofMinutes(2).toMillis()).setCollapseKey(topic)
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(AndroidNotification.builder().setSound(NotificationParameter.SOUND.getValue())
                        .setColor(NotificationParameter.COLOR.getValue()).setTag(topic).build()).build();
    }

    private ApnsConfig getApnsConfig(String topic) {
        return ApnsConfig.builder()
                .setAps(Aps.builder().setCategory(topic).setThreadId(topic).build()).build();
    }

    private Message getPreconfiguredMessageToAndroid(PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request).setToken(request.getToken())
                .build();
    }

    private Message getPreconfiguredMessageWithoutData(PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request).setTopic(request.getTopic())
                .build();
    }

    private Message getPreconfiguredMessageWithData(Map<String, String> data, PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request).putAllData(data).setTopic(request.getTopic())
                .build();
    }

    private Message.Builder getPreconfiguredMessageBuilder(PushNotificationRequest request) {
        AndroidConfig androidConfig = getAndroidConfig(request.getTopic());
        ApnsConfig apnsConfig = getApnsConfig(request.getTopic());
        return Message.builder()
                .setApnsConfig(apnsConfig).setAndroidConfig(androidConfig).setNotification(
                        new Notification(request.getTitle(), request.getMessage()));
    }
    public Message webpushMessage() {
        // [START webpush_message]
        Message message = Message.builder()
                .setWebpushConfig(WebpushConfig.builder()
                        .setNotification(new WebpushNotification(
                                "$GOOG up 1.43% on the day",
                                "$GOOG gained 11.80 points to close at 835.67, up 1.43% on the day.",
                                "https://my-server/icon.png"))
                        .setFcmOptions(WebpushFcmOptions.withLink("https://my-server/page-to-open-on-click"))
                        .build())
                .setTopic("industry-tech")
                .build();
        // [END webpush_message]
        return message;
    }
    public void sendToToken(PushNotificationRequest pushNotificationRequest) throws FirebaseMessagingException {
        // [START send_to_token]
        // See documentation on defining a message payload.
        Map<String,String> data= new HashMap<>();
        data.put("string1","test");
        data.put("string2","test2");
        Message message = Message.builder()
                .putAllData(data)
                .setToken(pushNotificationRequest.getToken())
                .build();

        // Send a message to the device corresponding to the provided
        // registration token.
        String response = FirebaseMessaging.getInstance().send(message);
        // Response is a message ID string.
        System.out.println("Successfully sent message: " + response);
        // [END send_to_token]
    }

}
