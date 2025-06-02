package app.ewarehouse.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
//import java.security.cert.CertificateException;
//import java.security.cert.X509Certificate;

//import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSession;
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;


public class SMSUtil {

    @Value("${https.url}")
    private String httpsUrl;

    static Logger loggger = LoggerFactory.getLogger(SMSUtil.class);

    private static String print_content(HttpsURLConnection con) {
        String input = "";
        if (con != null) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                while ((input = br.readLine()) != null) {
                    // Process input
                }
                br.close();
            } catch (IOException e) {
                //e.printStackTrace();
                loggger.error(e.getMessage());
            }
        }
        return input;
    }

    public static String validateSmsString(String sms) {
        if (sms.contains(" ")) {
            sms = sms.replace(" ", "%20");
            return sms;
        } else {
            return sms;
        }
    }

    public String Sendsms(String mobile, String message) throws NoSuchAlgorithmException, KeyManagementException {
        try {
            Thread smsthread = new Thread(() -> {
                String https_url = httpsUrl + message + "&mnumber=" + mobile
                        + "&signature=ESTATE&dlt_entity_id=1001663550000020145";
                URL url;
                try {
                    // Default SSL context with server certificate validation
                    SSLContext ssl_ctx = SSLContext.getInstance("TLS");
                    ssl_ctx.init(null, null, new SecureRandom()); // Use default trust manager

                    // Use default SSL context for validating certificates
                    HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());

                    // URL connection with proper validation
                    url = new URL(https_url);
                    HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                    con.setHostnameVerifier(HttpsURLConnection.getDefaultHostnameVerifier());

                    print_content(con);
                } catch (Exception e) {
                    loggger.info("Exception in side SMSUtil");
                }
            });

            smsthread.start();
            return "SMS send successfull To " + mobile;

        } catch (Exception e) {
            loggger.info("Exception in side SMSUtil");
            return "Failed To send sms to the Mobile No.: " + mobile + " Exception: " + e.getMessage();
        }
    }

    public String smsWithTemplate(String mobile, String message, String templateId) throws NoSuchAlgorithmException, KeyManagementException {
        String tmp = "";
        String https_url = httpsUrl + message
                + "&mnumber=" + mobile + "&signature=ESTATE&dlt_entity_id=1001663550000020145&dlt_template_id=" + templateId;
        URL url;
        try {
            // Default SSL context with server certificate validation
            SSLContext ssl_ctx = SSLContext.getInstance("TLS");
            ssl_ctx.init(null, null, new SecureRandom()); // Use default trust manager

            // Use default SSL context for validating certificates
            HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());

            // URL connection with proper validation
            url = new URL(https_url);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setHostnameVerifier(HttpsURLConnection.getDefaultHostnameVerifier());

            tmp = print_content(con);
        } catch (IOException e) {
            loggger.info("Exception in side SMSUtil");
        }
        return tmp;
    }
}



//public class SMSUtil {
//
//	@Value("${https.url}")
//    private String httpsUrl;
//	
//static	Logger loggger=LoggerFactory.getLogger(SMSUtil.class);
//	
//	private static TrustManager[] get_trust_mgr() {
//		return new TrustManager[] { new X509TrustManager() {
//
//			@Override
//			public X509Certificate[] getAcceptedIssuers() {
//				return null;
//			}
//
//			@Override
//			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//
//			}
//
//			@Override
//			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//			}
//		} };
//	}
//
//	private static String print_content(HttpsURLConnection con) {
//		String input = "";
//		if (con != null) {
//			try {
//				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
//				while ((input = br.readLine()) != null) {
//
//				}
//				br.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return input;
//	}
//
//		static HostnameVerifier allHostsValid = new HostnameVerifier() {  
//		        public boolean verify(String hostname, SSLSession session) {
//		            return true;  
//		        }
//		    };
//		public static String validateSmsString(String sms) {
//		        if (sms.contains(" ")) {
//		            sms = sms.replace(" ", "%20");
//		            return sms;
//		        } else {
//		            return sms;
//		        }
//		    }
//
//			public  String Sendsms(String mobile, String message)throws NoSuchAlgorithmException, KeyManagementException {
//
//				try {
//
//					Thread smsthread = new Thread(() -> {
//
//						HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
//						String https_url = httpsUrl+ message + "&mnumber=" + mobile
//								+ "&signature=ESTATE&dlt_entity_id=1001663550000020145";
//						URL url;
//						try {
//							SSLContext ssl_ctx = SSLContext.getInstance("TLS");
//							TrustManager[] trust_mgr = get_trust_mgr();
//							ssl_ctx.init(null, // key manager
//									trust_mgr, // trust manager
//									new SecureRandom()); // random number generator
//							HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
//							url = new URL(https_url);
//							HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
//							print_content(con);
//						} catch (Exception e) {
//							loggger.info("Exception in side SMSUtil");
//						}
//
//					});
//
//					smsthread.start();
//					return "SMS send successfull To " + mobile;
//
//				} catch (Exception e) {
//					loggger.info("Exception in side SMSUtil");
//					return "Failed To send sms to the Mobile No.: " + mobile + " Exception: " + e.getMessage();
//				}
//
//			}
//	
//			public String smsWithTemplate(String mobile, String message, String templateId)throws NoSuchAlgorithmException, KeyManagementException {
//				String tmp = "";
//				HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
//				String https_url = httpsUrl+ message
//						+ "&mnumber=" + mobile + "&signature=ESTATE&dlt_entity_id=1001663550000020145&dlt_template_id=" + templateId;
//				URL url;
//				try {
//					SSLContext ssl_ctx = SSLContext.getInstance("TLS");
//					TrustManager[] trust_mgr = get_trust_mgr();
//					ssl_ctx.init(null, // key manager
//							trust_mgr, // trust manager
//							new SecureRandom()); // random number generator
//					HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
//					url = new URL(https_url);
//					HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
//					tmp = print_content(con);
//				} catch (IOException e) {
//					loggger.info("Exception in side SMSUtil");
//				}
//				return tmp;
//			}
//}
