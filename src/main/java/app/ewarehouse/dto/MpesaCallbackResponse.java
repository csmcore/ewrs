package app.ewarehouse.dto;

import java.util.List;

//m-paisa Callback URL Response Bean Object
public class MpesaCallbackResponse {
private Body body;
public static class Body {
 private StkCallback stkCallback;
 public StkCallback getStkCallback() {
   return stkCallback;
 }
 public void setStkCallback(StkCallback stkCallback) {
   this.stkCallback = stkCallback;
 }
}
public static class StkCallback {
 private String merchantRequestID;
 private String checkoutRequestID;
 private int resultCode;
 private String resultDesc;
 private CallbackMetadata callbackMetadata;
 public String getMerchantRequestID() {
   return merchantRequestID;
 }
 public void setMerchantRequestID(String merchantRequestID) {
   this.merchantRequestID = merchantRequestID;
 }
 public String getCheckoutRequestID() {
   return checkoutRequestID;
 }
 public void setCheckoutRequestID(String checkoutRequestID) {
   this.checkoutRequestID = checkoutRequestID;
 }
 public int getResultCode() {
   return resultCode;
 }
 public void setResultCode(int resultCode) {
   this.resultCode = resultCode;
 }
 public String getResultDesc() {
   return resultDesc;
 }
 public void setResultDesc(String resultDesc) {
   this.resultDesc = resultDesc;
 }
 public CallbackMetadata getCallbackMetadata() {
   return callbackMetadata;
 }
 public void setCallbackMetadata(CallbackMetadata callbackMetadata) {
   this.callbackMetadata = callbackMetadata;
 }
}
public static class CallbackMetadata {
 private List<Item> item;
 public List<Item> getItem() {
   return item;
 }
 public void setItem(List<Item> item) {
   this.item = item;
 }
}
public static class Item {
 private String name;
 private Object value;
 public String getName() {
   return name;
 }
 public void setName(String name) {
   this.name = name;
 }
 public Object getValue() {
   return value;
 }
 public void setValue(Object value) {
   this.value = value;
 }
}
public Body getBody() {
 return body;
}
public void setBody(Body body) {
 this.body = body;
}
}
