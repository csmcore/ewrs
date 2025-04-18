package app.ewarehouse.util;



import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class Complaint_reportingValidationCheck {
private static final Logger logger = LoggerFactory.getLogger(Complaint_reportingValidationCheck.class);
public static String BackendValidation(JSONObject obj) {
logger.info("Inside BackendValidation method of Complaint_reportingValidationCheck");
String errMsg=null;
Integer errorStatus = 0;
if (errorStatus == 0  &&CommonValidator.isEmpty( obj.get("txtFullName").toString())){
 errorStatus = 1;
errMsg= "Full Name should not  be empty!";}
 if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtFullName"),50)) {
errorStatus = 1;
errMsg= "Full Name maxmimum length should be 50";}
if (errorStatus == 0 &&CommonValidator.isCharecterKey(obj.get("txtFullName").toString(),"")){
 errorStatus = 1;
errMsg= "Full Name should be a character!";}
if (errorStatus == 0  &&CommonValidator.isEmpty( obj.get("txtContactNumber").toString())){
 errorStatus = 1;
errMsg= "Contact Number should not  be empty!";}
 if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtContactNumber"),10)) {
errorStatus = 1;
errMsg= "Contact Number maxmimum length should be 10";}
if (errorStatus == 0 &&CommonValidator.validTelCheck(obj.get("txtContactNumber").toString())){
 errorStatus = 1;
errMsg= "Contact Number should be  valid !";}
if (errorStatus == 0  &&CommonValidator.isEmpty( obj.get("txtEmailAddress").toString())){
 errorStatus = 1;
errMsg= "Email Address should not  be empty!";}
 if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtEmailAddress"),50)) {
errorStatus = 1;
errMsg= "Email Address maxmimum length should be 50";}
if (errorStatus == 0 &&CommonValidator.emailCheck(obj.get("txtEmailAddress").toString())){
 errorStatus = 1;
errMsg= "Email Address should be valid !";}
if (errorStatus == 0  &&CommonValidator.isEmpty( obj.get("txtrAddress").toString())){
 errorStatus = 1;
errMsg= "Address should not  be empty!";}
 if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtrAddress"),10)) {
errorStatus = 1;
errMsg= "Address  minimum length should be 10";}
 if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtrAddress"),50)) {
errorStatus = 1;
errMsg= "Address maxmimum length should be 50";}
if (errorStatus == 0  && CommonValidator.BlankCheckRdoDropChk(obj.get("selCounty").toString())){
 errorStatus = 1;
errMsg= "County  should not be empty !";}
if (errorStatus == 0  && CommonValidator.BlankCheckRdoDropChk(obj.get("selSubCounty").toString())){
 errorStatus = 1;
errMsg= "Sub County   should not be empty !";}
if (errorStatus == 0  && (obj.get("rdoComplaintAgainst").toString() ==  "1" ||obj.get("rdoComplaintAgainst").toString() ==  "3" ||obj.get("rdoComplaintAgainst").toString() ==  "4" ) && CommonValidator.BlankCheckRdoDropChk(obj.get("selCountyofWarehouse").toString())){
 errorStatus = 1;
errMsg= "County of Warehouse   should not be empty !";}
if (errorStatus == 0  && (obj.get("rdoComplaintAgainst").toString() ==  "1" ||obj.get("rdoComplaintAgainst").toString() ==  "3" ||obj.get("rdoComplaintAgainst").toString() ==  "4" ) && CommonValidator.BlankCheckRdoDropChk(obj.get("selSubCountyofWarehouse").toString())){
 errorStatus = 1;
errMsg= "Sub-County of Warehouse  should not be empty !";}
if (errorStatus == 0  && (obj.get("rdoComplaintAgainst").toString() ==  "1" ||obj.get("rdoComplaintAgainst").toString() ==  "3" ||obj.get("rdoComplaintAgainst").toString() ==  "4" ) && CommonValidator.BlankCheckRdoDropChk(obj.get("selWarehouseShopName").toString())){
 errorStatus = 1;
errMsg= "Warehouse Shop Name  should not be empty !";}
if (errorStatus == 0  && (obj.get("rdoComplaintAgainst").toString() ==  "1" ||obj.get("rdoComplaintAgainst").toString() ==  "3" ||obj.get("rdoComplaintAgainst").toString() ==  "4" ) &&CommonValidator.isEmpty( obj.get("txtWarehouseOperator").toString())){
 errorStatus = 1;
errMsg= "Warehouse Operator should not  be empty!";}
 if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtWarehouseOperator"),50)) {
errorStatus = 1;
errMsg= "Warehouse Operator maxmimum length should be 50";}
if (errorStatus == 0 &&CommonValidator.isAlphaNumericKey(obj.get("txtWarehouseOperator").toString())){
 errorStatus = 1;
errMsg= "Warehouse Operator should be AlphaNumeric !";}
if (errorStatus == 0  && (obj.get("rdoComplaintAgainst").toString() ==  "4" ) && CommonValidator.BlankCheckRdoDropChk(obj.get("selNameofGrader").toString())){
 errorStatus = 1;
errMsg= "Name of Grader  should not be empty !";}
if (errorStatus == 0  && (obj.get("rdoComplaintAgainst").toString() ==  "1" ||obj.get("rdoComplaintAgainst").toString() ==  "2" ||obj.get("rdoComplaintAgainst").toString() ==  "3" ||obj.get("rdoComplaintAgainst").toString() ==  "4" ) && CommonValidator.BlankCheckRdoDropChk(obj.get("selTypeofComplain").toString())){
 errorStatus = 1;
errMsg= " Type of Complain  should not be empty !";}
if (errorStatus == 0  && (obj.get("rdoComplaintAgainst").toString() ==  "1" ||obj.get("rdoComplaintAgainst").toString() ==  "2" ||obj.get("rdoComplaintAgainst").toString() ==  "3" ||obj.get("rdoComplaintAgainst").toString() ==  "4" ) &&CommonValidator.isEmpty( obj.get("txtDateofIncident").toString())){
 errorStatus = 1;
errMsg= "Date of Incident  should not  be empty!";}
if (errorStatus == 0  && (obj.get("rdoComplaintAgainst").toString() ==  "1" ||obj.get("rdoComplaintAgainst").toString() ==  "2" ||obj.get("rdoComplaintAgainst").toString() ==  "3" ||obj.get("rdoComplaintAgainst").toString() ==  "4" ) &&CommonValidator.isEmpty( obj.get("txtrDescriptionofIncident").toString())){
 errorStatus = 1;
errMsg= "Description of Incident should not  be empty!";}
 if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtrDescriptionofIncident"),10)) {
errorStatus = 1;
errMsg= "Description of Incident  minimum length should be 10";}
 if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtrDescriptionofIncident"),50)) {
errorStatus = 1;
errMsg= "Description of Incident maxmimum length should be 50";}
if (errorStatus == 0  && (obj.get("rdoComplaintAgainst").toString() ==  "3" ) && CommonValidator.BlankCheckRdoDropChk(obj.get("selNameofCollateralManager").toString())){
 errorStatus = 1;
errMsg= "Name of Collateral Manager  should not be empty !";}
return errMsg;
}
}