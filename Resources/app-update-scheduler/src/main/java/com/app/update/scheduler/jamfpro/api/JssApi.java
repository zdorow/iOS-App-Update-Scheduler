package com.app.update.scheduler.jamfpro.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.binary.Base64;

public class JssApi {
	public static final String FAIL = "failure";
	public static final String SUCCESS = "success";
	private String JSS_URL;
	private String credentials;
	private int lastResponseCode;
	public static enum FORMAT {
		XML, JSON
	}
	
	private FORMAT receiveFormat;
	private FORMAT sendFormat;
	
	/**
	 * Takes in all information needed to communicate with a JSS's API
	 * @param jssBaseUrl e.g. https://jfelt.local:8443 - the /JSSResrouce/ is automatically appended
	 * @param jssApiUsername a username that has access to the JSS API
	 * @param jssApiPassword if you need documentation for this...
	 * @param sendFormat Which format would you like to send information in to the JSS, JssApi.FORMAT.XML or JssApi.FORMAT.JSON ?
	 * @param receiveFormat Which format would you like to receive information in from the JSS, JssApi.FORMAT.XML or JssApi.FORMAT.JSON ?
	 */
	public JssApi(String jssBaseUrl, String jssApiUsername, String jssApiPassword, FORMAT sendFormat, FORMAT receiveFormat) {
		setJssUrl(jssBaseUrl);
		String userPass = jssApiUsername + ":" + jssApiPassword;
		this.credentials = new String(Base64.encodeBase64(userPass.getBytes()));
		this.receiveFormat = receiveFormat;
		this.sendFormat = sendFormat;
	}

	private HttpsURLConnection getConnection(String location, String method, String requestData) throws JssApiException {
		
		try {
			
			SSLContext ctx = SSLContext.getInstance("TLS");
	        ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()}, new SecureRandom());
	        SSLContext.setDefault(ctx);
			
			System.out.println(JSS_URL + location);
			URL url = new URL(JSS_URL + location);
			
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod(method);
			
			conn.setHostnameVerifier(new HostnameVerifier() {
	            @Override
	            public boolean verify(String arg0, SSLSession arg1) {
	                return true;
	            }
	        });
			
			if (this.sendFormat == FORMAT.JSON) {
				conn.setRequestProperty("Content-Type", "application/json");
			} else {
				conn.setRequestProperty("Content-Type", "application/xml");
			}
			
			if (this.receiveFormat == FORMAT.JSON) {
				conn.setRequestProperty("Accept", "application/json");
			} else {
				conn.setRequestProperty("Accept", "application/xml");
			}
			
			conn.setDoOutput(true);
			conn.setDoInput(true);
			
			// handle auth
			conn.setRequestProperty("Authorization", "Basic " + credentials);
			
			// add data to request
			if(requestData != null){
				OutputStream os = conn.getOutputStream();
				os.write(requestData.getBytes());
				os.close();
			}
			
			return conn;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			throw new JssApiException(e, -1);
		}
		
	}
	
	protected String parseResponse(HttpsURLConnection conn) throws JssApiException {
			
		try {
			this.lastResponseCode = conn.getResponseCode();
                        
			InputStream is = conn.getInputStream();
			
			byte[] buf = readInfoFromStream(is);
	      
			return new String(buf);
		} catch (IOException e) {
			e.printStackTrace();
			throw new JssApiException(e, this.lastResponseCode);
		}
	}

	private byte[] readInfoFromStream(InputStream is) throws IOException {
		int len;
		int size = 1024;
		byte[] buf;
		
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		buf = new byte[size];
		while ((len = is.read(buf, 0, size)) != -1){
			bos.write(buf, 0, len);
		}
		buf = bos.toByteArray();
		return buf;
	}
	
	private String doSequence(String method, String location, String data) throws JssApiException {
		// get the connection
		HttpsURLConnection conn;
		try {
//			location = location.replace(" ", "+");
//			location = URLEncoder.encode(location, "UTF-8");
			conn = getConnection(location, method, data);
			conn.connect();
		} catch (JssApiException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new JssApiException(e, -1);
		}
		if (method.equals("GET")) {
			return parseResponse(conn);
		} else {
			int responseCode = -1;
			try {
				responseCode = conn.getResponseCode();
				if (responseCode -200  < 100) {
					String response = parseResponse(conn);
					System.out.println(response);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			if (responseCode / 100 == 2) {
				return SUCCESS;
			}
			System.out.println(responseCode);
			return FAIL;
		}
		
	}
	
	/**
	 * 
	 * @param location example: mobiledevices - the /JSSResource/ is already taken care of
	 * @return JSON of request
	 * @throws JssApiException
	 */
	public String get(String location) throws JssApiException{
		return doSequence("GET", location, null);
	}
	
	/**
	 * 
	 * @param location example: mobiledevices - the /JSSResource/ is already taken care of
	 * @param postData
	 * @return
	 * @throws JssApiException
	 */
	public String post(String location, String postData) throws JssApiException{
		return doSequence("POST", location, postData);
	}
	
	/**
	 * 
	 * @param location example: mobiledevices - the /JSSResource/ is already taken care of
	 * @param putData
	 * @return
	 * @throws JssApiException
	 */
	public String put(String location, String putData) throws JssApiException{
		return doSequence("PUT", location, putData);
	}
	
	/**
	 * 
	 * @param location example: mobiledevices - the /JSSResource/ is already taken care of
	 * @return
	 * @throws JssApiException
	 */
	public String delete(String location) throws JssApiException {
		return doSequence("DELETE", location, null);
	}
	
	/**
	 * Not reliable for using the same JssApi instance for multiple threads
	 * @return the HTTP Response code of the last performed action
	 */
	public int getLastResponseCode() {
		return this.lastResponseCode;
	}
	
	private void setJssUrl(String jssBaseUrl) {
		this.JSS_URL = jssBaseUrl;
		if (!JSS_URL.endsWith("/")) {
			JSS_URL += "/";
		}
		JSS_URL += "JSSResource/";
	}

	private static class DefaultTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}
