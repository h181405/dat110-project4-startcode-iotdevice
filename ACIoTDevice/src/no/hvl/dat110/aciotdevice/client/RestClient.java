package no.hvl.dat110.aciotdevice.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.Gson;

public class RestClient {
	
	private static String domain = "http://localhost:8080";

	public RestClient() {
		// TODO Auto-generated constructor stub
	}

	private static String logpath = "/accessdevice/log";

	public void doPostAccessEntry(String message) {

		// TODO: implement a HTTP POST on the service to post the message
		AccessMessage am = new AccessMessage(message);
		Gson gson = new Gson();
		String json = gson.toJson(am);
		
		try {
			URL urlobj = new URL(domain + logpath);
			HttpURLConnection huc = (HttpURLConnection) urlobj.openConnection();
			huc.setRequestMethod("POST");
			huc.setRequestProperty("content-type", "application/json");
			
			huc.setDoOutput(true);
			PrintWriter out = new PrintWriter(huc.getOutputStream());
			out.print(json);
			out.flush();
			out.close();
			
			int rc = huc.getResponseCode();
			
			System.out.println("RC: " + rc);
			
			huc.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String codepath = "/accessdevice/code";
	
	public AccessCode doGetAccessCode() {

		AccessCode code = null;
		
		// TODO: implement a HTTP GET on the service to get current access code
		
		String json = "";
		try {
			URL urlobj = new URL(domain + codepath);
			HttpURLConnection huc = (HttpURLConnection) urlobj.openConnection();
			huc.setRequestMethod("GET");
			
			int rc = huc.getResponseCode();
			
			System.out.println("RC: " + rc);
			
			if(rc == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(huc.getInputStream()));
				String input;
				
				while((input = in.readLine()) != null) {
					json += input;
				}
				in.close();
			}
			huc.disconnect();
		} catch (MalformedURLException e) { 
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		Gson gson = new Gson();
		code = gson.fromJson(json, AccessCode.class);
		
		return code;
	}
}
