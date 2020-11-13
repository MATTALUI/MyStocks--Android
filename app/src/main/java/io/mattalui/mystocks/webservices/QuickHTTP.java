package io.mattalui.mystocks.webservices;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

//import io.mattalui.autologs.BuildConfig;

public class QuickHTTP {

  String usertoken;

  public QuickHTTP(){
    this.usertoken = null;
  }

  public QuickHTTP(String _token) {
    this.usertoken = _token;
  }

  private HttpURLConnection getBaseConnection(String url) throws MalformedURLException, IOException {
    System.out.println(url);
    HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();
    connection.setRequestProperty("Authorization", authorization);
    connection.setRequestProperty("Content-Type", "application/json; utf-8");
    connection.setRequestProperty("X-Identity", token);

    return connection;
  }

  private String buildQueryString(HashMap<String, String> paramHash) {
    String params = "?";
    Set<String> keys = paramHash.keySet();
    boolean firstKey = true;
    for (String key : keys) {
      if(!firstKey) {
        params = params + "&";
      }

      String value = URLEncoder.encode(paramHash.get(key));
      params = params +  URLEncoder.encode(key) + "=" + value;
      firstKey = false;
    }

    return params;
  }

  private String readResponse(HttpURLConnection connection) throws IOException {
    InputStream response = null;
    if(connection.getResponseCode() >= 400){
      response = connection.getErrorStream();
    }else{
      response = connection.getInputStream();
    }

    Scanner scanner = new Scanner(response);

    String resp = scanner.useDelimiter("\\A").next();
    System.out.println("QUICKHTTP RESPONSE: " + resp);
    return resp;
  }

  private void writeData(HttpURLConnection connection, String data) throws IOException {
    OutputStream output = connection.getOutputStream();
    byte[] body = data.getBytes("utf-8");
    output.write(body, 0, body.length);
  }

  public String get(String url){
    try {
      HttpURLConnection connection = getBaseConnection(url);
      return readResponse(connection);
    }catch(Exception e) {
      System.out.println(e);
      return "ERROR";
    }
  }

  public String get(String url, HashMap<String, String> params){
    String requestUrl = url + buildQueryString(params);

    return get(requestUrl);
  }

  public String post(String url, String data){
    try {
      HttpURLConnection connection = getBaseConnection(url);
      connection.setRequestMethod("POST");
      connection.setDoOutput(true);
      writeData(connection, data);

      return readResponse(connection);
    }catch(Exception e) {
      System.out.println(e);
      return "ERROR";
    }
  }

  public String put(String url, String data){
    try {
      HttpURLConnection connection = getBaseConnection(url);
      connection.setRequestMethod("PUT");
      connection.setDoOutput(true);
      writeData(connection, data);

      return readResponse(connection);
    }catch(Exception e) {
      System.out.println(e);
      return "ERROR";
    }
  }

  public String delete(String url){
    try {
      HttpURLConnection connection = getBaseConnection(url);
      connection.setRequestMethod("DELETE");

      return readResponse(connection);
    }catch(Exception e) {
      System.out.println(e);
      return "ERROR";
    }
  }
}
