package app.ewarehouse.util;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.lang.*;
import java.io.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
public class PaymentMainTest {
private final String USER_AGENT = "Mozilla/5.0";

	
	  static String generate_signature(String data_string, String secret){
	     try{

	           SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(),"HmacSHA256");

	           Mac mac = Mac.getInstance("HmacSHA256");
	           mac.init(keySpec);
	           byte[] result = mac.doFinal(data_string.getBytes());

	          String encode_result = Base16EncoderTest.encode(result);

	          return Base64.getEncoder().encodeToString(encode_result.toLowerCase().getBytes());

	       } catch (Exception e) {

	            return "Cannot Process Signature";
	      }
	  }
	}





