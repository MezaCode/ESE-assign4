package gateways;

import myexceptions.UnauthorizedException;
import myexceptions.UnknownException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import utils.HashUtils;

public class SessionGateway {
    private static final Logger LOGGER = LogManager.getLogger();

    public static String login(String userName, String password) throws UnauthorizedException {
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {
            httpclient = HttpClients.createDefault();
            HttpPost postRequest = new HttpPost("http://localhost:8080/login");

            JSONObject formData = new JSONObject();
            formData.put("username", userName);
            formData.put("password", HashUtils.getCryptoHash(password, "SHA-256"));
            String formDataString = formData.toString();

            StringEntity reqEntity = new StringEntity(formDataString);
            postRequest.setEntity(reqEntity);
            postRequest.setHeader("Content-Type", "application/json");
            response = httpclient.execute(postRequest);
            System.out.println("--------------------------------- output ---------------------------------");
            System.out.println(response.getStatusLine().getStatusCode());
            System.out.println(formDataString);
            System.out.println();
            System.out.println("--------------------------------------------------------------------------");
            switch(response.getStatusLine().getStatusCode()) {
                case 200:
                    HttpEntity entity = response.getEntity();
                    // use org.apache.http.util.EntityUtils to read json as string
                    String strResponse = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                    EntityUtils.consume(entity);
                    JSONObject jsonObject = new JSONObject(strResponse);
                    String session = jsonObject.getString("session_id");
                    System.out.println(session);
                    return session;
                case 401:
                    throw new UnauthorizedException("login failed");
                default:
                    throw new UnknownException(response.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new UnauthorizedException(e);

        } finally {
            try {
                if(response != null) {
                    response.close();
                }
                if(httpclient != null) {
                    httpclient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new UnauthorizedException(e);
            }
        }
    }
}
