package app.ewarehouse.service;

import app.ewarehouse.dto.CommonResponseModal;
import app.ewarehouse.dto.OTPRequestDTO;
import app.ewarehouse.dto.OTPValidationRequestDTO;

public interface OTPService {
	public CommonResponseModal getOTP(OTPRequestDTO requestDTO) ;
	public CommonResponseModal validateOTP(OTPValidationRequestDTO otpdto);
}
