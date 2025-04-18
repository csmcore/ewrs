package app.ewarehouse.util;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class Warehouse_regValidationCheck {
private static final Logger logger = LoggerFactory.getLogger(Warehouse_regValidationCheck.class);
public static String BackendValidation(JSONObject obj) {
logger.info("Inside BackendValidation method of Warehouse_regValidationCheck");
String errMsg=null;
Integer errorStatus = 0;
 if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtapplicantname"),1)) {
errorStatus = 1;
errMsg= "applicant name  minimum length should be 1";}
 if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtapplicantname"),20)) {
errorStatus = 1;
errMsg= "applicant name maxmimum length should be 20";}
if (errorStatus == 0 &&CommonValidator.isAlphaNumericKey(obj.get("txtapplicantname").toString())){
 errorStatus = 1;
errMsg= "applicant name should be AlphaNumeric !";}
if (errorStatus == 0 &&CommonValidator.isCharecterKey(obj.get("txtaddress").toString(),"")){
 errorStatus = 1;
errMsg= "address should be a character!";}
return errMsg;
}
}