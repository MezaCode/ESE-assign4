package gateways;

import mvc.model.Person;
import myexceptions.UnauthorizedException;
import myexceptions.UnknownException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.HashUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;

public class PersonGateway {
    private static final Logger LOGGER = LogManager.getLogger();

    public static ArrayList<Person> fetchPeople(String session) throws UnauthorizedException {
        ArrayList<Person> peopleList = new ArrayList<Person>();

        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {
            httpclient = HttpClients.createDefault();
            HttpGet getRequest = new HttpGet("http://localhost:8080/people");

            JSONObject formData = new JSONObject();
            formData.put("session_id", session);
            String formDataString = formData.toString();

            StringEntity reqEntity = new StringEntity(formDataString);
            getRequest.setHeader("Authorization",session);//setEntity(reqEntity);
            response = httpclient.execute(getRequest);

            switch(response.getStatusLine().getStatusCode()) {
                case 200:
                    HttpEntity entity = response.getEntity();
                    // use org.apache.http.util.EntityUtils to read json as string
                    String strResponse = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                    for ( Object obj : new JSONArray(strResponse)){
                        JSONObject jsonObject = (JSONObject) obj;
                        String dob = jsonObject.getString("dob");
                        dob = dob.substring(0,10);
                        Person person = new Person(jsonObject.getInt("id"),jsonObject.getString("first_name"), jsonObject.getString("last_name"), LocalDate.parse(dob) );
                        peopleList.add(person);
                                //Person(int inputID, String first, String last, LocalDate dob)
                    }
                    System.out.println(peopleList);
                    return peopleList;
                case 401:
                    throw new UnauthorizedException("Unauthorized session");
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
    public static void addAuditTrail(String session, String val, String field, String oldVal, int id)throws UnauthorizedException {
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {
            httpclient = HttpClients.createDefault();
            HttpPost postRequest = new HttpPost("http://localhost:8080/people/"+id+"/audittrail");
            String msg = "";
            if (field.equalsIgnoreCase("added")){
                msg = field;
            }
            else{
                msg = field+" changed from "+oldVal+" to "+val;
            }
            JSONObject formData = new JSONObject();
            formData.put("changed_msg", msg);
            String formDataString = formData.toString();
// todo: finish this for adding an audit (need to user id)
            StringEntity reqEntity = new StringEntity(formDataString);
            postRequest.setEntity(reqEntity);
            postRequest.setHeader("Authorization",session);//setEntity(reqEntity);
            postRequest.setHeader("Content-Type", "application/json");
            response = httpclient.execute(postRequest);

            switch(response.getStatusLine().getStatusCode()) {
                case 200:
                    break;
                case 401:
                    throw new UnauthorizedException("Unauthorized session");
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
    public static void updatePerson(String session, String name ,String field, String oldVal, int id) throws UnauthorizedException {
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {
            httpclient = HttpClients.createDefault();
            HttpPut putRequest = new HttpPut("http://localhost:8080/people/"+id);

            JSONObject formData = new JSONObject();
            formData.put(field, name);
            String formDataString = formData.toString();

            StringEntity reqEntity = new StringEntity(formDataString);
            putRequest.setEntity(reqEntity);
            putRequest.setHeader("Authorization",session);//setEntity(reqEntity);
            putRequest.setHeader("Content-Type", "application/json");
            response = httpclient.execute(putRequest);

            switch(response.getStatusLine().getStatusCode()) {
                case 200:
                    break;
                case 401:
                    throw new UnauthorizedException("Unauthorized session");
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

    public static int insertPerson(String session, String firstName, String lastName, String dob) throws UnauthorizedException {
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {
            httpclient = HttpClients.createDefault();
            HttpPost postRequest = new HttpPost("http://localhost:8080/people");

            JSONObject formData = new JSONObject();
            formData.put("first_name", firstName);
            formData.put("last_name",lastName);
            formData.put("dob",dob);

            String formDataString = formData.toString();

            StringEntity reqEntity = new StringEntity(formDataString);
            postRequest.setEntity(reqEntity);
            postRequest.setHeader("Authorization",session);//setEntity(reqEntity);
            postRequest.setHeader("Content-Type", "application/json");
            response = httpclient.execute(postRequest);

            switch(response.getStatusLine().getStatusCode()) {
                case 200:
                    HttpEntity entity = response.getEntity();
                    // use org.apache.http.util.EntityUtils to read json as string
                    String strResponse = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                    JSONObject jsonObject = new JSONObject(strResponse);
                    int id = jsonObject.getInt("id");
                    return id;
                case 401:
                    throw new UnauthorizedException("Unauthorized session");
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

    public static void deletePerson(String session, int id) throws UnauthorizedException {
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {
            httpclient = HttpClients.createDefault();
            HttpDelete deleteRequest = new HttpDelete("http://localhost:8080/people/"+id);
            deleteRequest.setHeader("Authorization",session);//setEntity(reqEntity);
            response = httpclient.execute(deleteRequest);

            switch(response.getStatusLine().getStatusCode()) {
                case 200:
                    break;
                case 401:
                    throw new UnauthorizedException("Unauthorized session");
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
