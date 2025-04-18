package app.ewarehouse.util;

import org.json.JSONObject;
public class TrolepermissionValidationCheck {
public static String BackendValidation(JSONObject obj) {
String errMsg=null;
Integer errorStatus = 0;
if (errorStatus == 0 && CommonValidator.BlankCheckRdoDropChk(obj.get("selPermissionFor").toString())){
 errorStatus = 1;
errMsg= "Permission For  Should Not Be Empty !";}

return errMsg;
}
}
