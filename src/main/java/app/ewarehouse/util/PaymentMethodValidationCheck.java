package app.ewarehouse.util;

import org.json.JSONObject;

public class PaymentMethodValidationCheck {
    public static String BackendValidation(JSONObject obj) {
        String errMsg = null;
        Integer errorStatus = 0;
        if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtPaymentMethod").toString())) {
            errorStatus = 1;
            errMsg = "Payment Method Should Not  Be Empty!";
        }
        if (errorStatus == 0 && CommonValidator.minLengthCheck(obj.get("txtPaymentMethod").toString(), 3)) {
            errorStatus = 1;
            errMsg = "Payment Method  Minimum Length Should Be 3";
        }
        if (errorStatus == 0 && CommonValidator.isCharecterKey(obj.get("txtPaymentMethod").toString())) {
            errorStatus = 1;
            errMsg = "Payment Method Should Be A Character!";
        }
        if (errorStatus == 0 && CommonValidator.isSpecialCharKey(obj.get("txtPaymentMethod").toString())) {
            errorStatus = 1;
            errMsg = "Payment Method Should Be SpecialCharKey !";
        }
        if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtDescription").toString())) {
            errorStatus = 1;
            errMsg = "Description Should Not  Be Empty!";
        }
        if (errorStatus == 0 && CommonValidator.minLengthCheck(obj.get("txtDescription").toString(), 3)) {
            errorStatus = 1;
            errMsg = "Description  Minimum Length Should Be 3";
        }
        if (errorStatus == 0 && CommonValidator.isCharecterKey(obj.get("txtDescription").toString())) {
            errorStatus = 1;
            errMsg = "Description Should Be A Character!";
        }
        return errMsg;
    }
}
