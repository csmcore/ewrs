package app.ewarehouse.util;

import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import app.ewarehouse.dto.PaymentDataStringDto;

@Service
public class PaymentSecureHashGenerate {
	private final ApplicationPropertiesValue applicationPropertiesValue;

    public PaymentSecureHashGenerate(ApplicationPropertiesValue applicationPropertiesValue) {
        this.applicationPropertiesValue = applicationPropertiesValue;
    }

	public String generateSignature(PaymentDataStringDto dataStringDto) {

		try {

			if (dataStringDto != null) {

				//String secretKey = applicationPropertiesValue.getSecretKey();
				String secretKey ="a3qLCEMr5kctDsi7";
				
				String apiClientID = applicationPropertiesValue.getApiClientID();

				String serviceID = applicationPropertiesValue.getServiceID();
				Double amount=Double.parseDouble(dataStringDto.getAmountExpected());
				Long roundedValue = Math.round(amount);
				String AmountExpected=String.valueOf(roundedValue);
				String dataString = apiClientID +AmountExpected + serviceID +

						dataStringDto.getClientIDNumber() + dataStringDto.getCurrency() +

						dataStringDto.getBillRefNumber() + dataStringDto.getBillDesc() +

						dataStringDto.getClientName() + secretKey;
//				
//				         "06211005-4c18-4b02-88ca-454b2f58256c"+"CollaterTestTwo" + secretKey;
				
				
				//String dataString="3541400197105232KES06211005-4c18-4b02-88ca-454b2f58256cPayment for Certificate of ConformityCollaterTestTwoj4MQiAcKNIJKlsgz9dSflHxYV8bpgtyv";

				System.out.println(dataString);
				
				String signKey = PaymentMainTest.generate_signature(dataString, secretKey);
				
				System.out.println(signKey);

				// SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(),
				// "HmacSHA256");

				// Mac mac = Mac.getInstance("HmacSHA256");

				// mac.init(keySpec);

				// byte[] result = mac.doFinal(dataString.getBytes());

				return signKey;

			} else {

				return "Cannot Process Signature";

			}

		} catch (Exception e) {

			return "Cannot Process Signature";

		}

	}

}
