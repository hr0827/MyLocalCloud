package com.test.model;

import java.io.InputStream;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import sun.misc.BASE64Encoder;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;

/**
 * 
 * @author ����
 * ��ɽ��������Token
 * 
 *
 */
public class ApiTest {
	//ע��õ���key��secret
	public String comsumerKey ;
	public String comsumerSecret ;
	
	// ��һ���ķ��ʻ�����ַ
	public String stepOneUrl = new String("https://openapi.kuaipan.cn/open/requestToken");
	// �������Ļ�����ַ
	public String stepThreeUrl = new String("https://openapi.kuaipan.cn/open/accessToken");
	
	// ����������ַ���
	public String nonce;
	// ʱ��� 
	public String timestamp;
	
	// ��һ������ǩ���Ļ��ַ���
	public String stepOneBaseString;
	// ����������ǩ���Ļ��ַ���
	public String stepThreeBaseString;
	
	// ��һ����ǩ��
	public String stepOneSignature;
	// ��������ǩ��
	public String stepThreeSignature;
	
	// ��һ��ǩ������Կ
	public String stepOneSignatureKey;
	// ������ǩ������Կ
	public String stepThreeSignatureKey;
	
	// ��ʱ��token
	public String tempToken ;
	// ��ʱ��token secret
	public String tempTokenSecret ;
	
	// ��һ�����ʵ�ַ
	public String stepOneRealUrl;
	// ���������ʵ�ַ
	public String stepThreeRealUrl;
	
	
	public ApiTest(String comsumerKey ,String comsumerSecret) {
		this.comsumerKey = comsumerKey ;
		this.comsumerSecret = comsumerSecret ;
	}
	
	//����nonce
	public void setnonce(){
		String base = "abcdefghijklmnopqrstuvwxyz0123456789"; 
		Random random = new Random(); 
		StringBuffer sb = new StringBuffer(); 
	    for (int i = 0; i < 8; i++) {
	        int number = random.nextInt(base.length());
	        sb.append(base.charAt(number));
	        }
	    this.nonce = sb.toString(); 
	}
	
	//����timestamp
	public void settimestamp(){
	    Date date = new Date();
	    long time = date.getTime();
	    this.timestamp = (time + "").substring(0, 10); //ʱ���λ�����ɣ�
	}
	
	//��ȡ����
	@SuppressWarnings("deprecation")
	public void getBaseString(){
		this.setnonce();
		this.settimestamp();
		String encodebaseurl = URLEncoder.encode(stepOneUrl);
		String encodeparam = URLEncoder.encode("oauth_consumer_key="+this.comsumerKey+"&"+"oauth_nonce="+this.nonce+"&"+"oauth_signature_method=HMAC-SHA1"+"&"+"oauth_timestamp="+this.timestamp+"&"+"oauth_version=1.0");
		this.stepOneBaseString = "GET&"+encodebaseurl+"&"+encodeparam;
	}
	//��ȡ����2
	@SuppressWarnings("deprecation")
	public void getBaseString2(){
		this.setnonce();
		this.settimestamp();
		String encodebaseurl = URLEncoder.encode(stepThreeUrl);
		String encodeparam = URLEncoder.encode("oauth_consumer_key="+this.comsumerKey+"&"+"oauth_nonce="+this.nonce+"&"+"oauth_signature_method=HMAC-SHA1"+"&"+"oauth_timestamp="+this.timestamp+"&"+"oauth_token="+this.tempToken+"&"+"oauth_version=1.0");
		this.stepThreeBaseString = "GET&"+encodebaseurl+"&"+encodeparam;
	}
	//����signaturekey
	public void getsignaturekey(){
		this.stepOneSignatureKey = comsumerSecret+"&";

	}
	//����signaturekey2
	public void getsignaturekey2(){
		this.stepThreeSignatureKey = comsumerSecret+"&"+tempTokenSecret;
	}
	//����signature
	@SuppressWarnings("deprecation")
	public void getsignature(){
		this.getBaseString();
		this.getsignaturekey();
		byte[] byteHMAC = null; 
		 try {
			    Mac mac = Mac.getInstance("HmacSHA1");
			    SecretKeySpec spec = new SecretKeySpec(stepOneSignatureKey.getBytes(), "HmacSHA1");
			    mac.init(spec);
			    byteHMAC = mac.doFinal(stepOneBaseString.getBytes());
			    } catch (InvalidKeyException e) {
			    	e.printStackTrace();
			    } catch (NoSuchAlgorithmException ignore) {
			    } 
		 BASE64Encoder enc = new BASE64Encoder();
		 String s = enc.encode(byteHMAC);
		 this.stepOneSignature = URLEncoder.encode(s);
	}
	//����signature2
	@SuppressWarnings("deprecation")
	public void getsignature2(){
		this.getBaseString2();
		this.getsignaturekey2();
		byte[] byteHMAC = null; 
		 try {
			    Mac mac = Mac.getInstance("HmacSHA1");
			    SecretKeySpec spec = new SecretKeySpec(stepThreeSignatureKey.getBytes(), "HmacSHA1");
			    mac.init(spec);
			    byteHMAC = mac.doFinal(stepThreeBaseString.getBytes());
			    } catch (InvalidKeyException e) {
			    	e.printStackTrace();
			    } catch (NoSuchAlgorithmException ignore) {
			    } 
		 BASE64Encoder enc = new BASE64Encoder();
		 String s = enc.encode(byteHMAC);
		 this.stepThreeSignature = URLEncoder.encode(s);
	}
	//��������
	public void geturl(){
		this.getsignature();
		stepOneRealUrl = "https://openapi.kuaipan.cn/open/requestToken?oauth_consumer_key="+this.comsumerKey+"&oauth_nonce="+this.nonce+"&oauth_signature_method=HMAC-SHA1&oauth_timestamp="+this.timestamp+"&oauth_version=1.0&oauth_signature="+this.stepOneSignature;
	}
	//��������2
	public void geturl2(){
		this.getsignature2();
		stepThreeRealUrl = "https://openapi.kuaipan.cn/open/accessToken?oauth_signature="+this.stepThreeSignature+"&oauth_consumer_key="+this.comsumerKey+"&oauth_nonce="+this.nonce+"&oauth_signature_method=HMAC-SHA1&oauth_timestamp="+this.timestamp+"&oauth_token="+this.tempToken+"&oauth_version=1.0";
	}
	//��������
	public JSONObject sendGet(String urlname) 
	{
		JSONObject result = null;
		try
		{
			URL realUrl = new URL(urlname);
			// �򿪺�URL֮�������
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
			conn.setConnectTimeout(5 * 1000);
			conn.setRequestMethod("GET");
			// ��ȡ������Ӧͷ�ֶ�
			InputStream map = conn.getInputStream();
			int i;
			String str="";
			while((i = map.read())!=-1){
				str = str+String.valueOf((char)i);
			}
			result = new JSONObject(str);
			conn.disconnect();

		}
		catch(Exception e)
		{
			System.out.println("����GET��������쳣��" + e);
			e.printStackTrace();
		}
		return result;
	}
}
