//package edu.technopolis.advanced.boatswain;
//
//import java.io.IOException;
//
//import org.junit.AfterClass;
//import org.junit.Assert;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import edu.technopolis.advanced.weather.incoming.request.Message;
//import edu.technopolis.advanced.weather.request.GetSubscriptionsRequest;
//import edu.technopolis.advanced.weather.request.SendMessagePayload;
//import edu.technopolis.advanced.weather.request.SendMessageRequest;
//import edu.technopolis.advanced.weather.request.SendRecipient;
//import edu.technopolis.advanced.weather.request.SubscribePayload;
//import edu.technopolis.advanced.weather.request.SubscribeRequest;
////import edu.technopolis.advanced.weather.response.CurrencyResponse;
//import edu.technopolis.advanced.weather.response.GetSubscriptionsResponse;
//import edu.technopolis.advanced.weather.response.SendMessageResponse;
//import edu.technopolis.advanced.weather.response.SubscribeResponse;
//import junit.framework.TestCase;
//
//public class ApiClientTest {
//    private static ApiClient client;
//
//    @BeforeClass
//    public static void createClient() throws IOException {
//        client = new ApiClient("https", "api.ok.ru",
//                "tkn1wOarQY5ZN01PPgK4xW2tg51XhUahBpvTt3bxKMJCrpdHrYsRoYOamNux7uKBcQudQ:CBACJMBMEBABABABA");
//    }
//
//    @Test
//    public void testSubscriptions() throws Exception {
//        GetSubscriptionsResponse subscriptions = client.get(new GetSubscriptionsRequest("/graph/me/subscriptions"), GetSubscriptionsResponse.class);
//        subscriptions.getSubscriptions().forEach(subscription -> System.out.println(subscription.getUrl()));
//        TestCase.assertNotNull(subscriptions);
//    }
//
//    @Test
//    public void testSubscribe() throws Exception {
//        SubscribeRequest req = new SubscribeRequest("/graph/me/subscribe");
//        SubscribePayload payload = new SubscribePayload();
//        payload.setUrl("https://boatswain.jtechnopolis.pw/onmessage");
//        req.setPayload(payload);
//        SubscribeResponse status = client.post(req, SubscribeResponse.class);
//        TestCase.assertNotNull(status);
//    }
//
//    @Test
//    public void testPostMessage() throws Exception {
//        SendMessageRequest req = new SendMessageRequest("/graph/me/messages", "chat:C3e7a6b21f900");
//        Message message = new Message();
//        message.setText("text");
//        SendMessagePayload payload = new SendMessagePayload(new SendRecipient("user:541569460261"), message);
//        req.setPayload(payload);
//        SendMessageResponse status = client.post(req, SendMessageResponse.class);
//        TestCase.assertNotNull(status);
//    }
//
////    @Test
////    public void testCurrency() throws IOException {
////        ApiClient currencyClient = new ApiClient("http", "api.fixer.io", null);
////        CurrencyResponse usd = currencyClient.get(new CurrencyRequest("USD"), CurrencyResponse.class);
////        Assert.assertNotNull(usd.getRates());
////        Assert.assertNotNull(usd.getRates().get("RUB"));
////    }
//
//    @AfterClass
//    public static void closeClient() throws IOException {
//        client.close();
//    }
//}
