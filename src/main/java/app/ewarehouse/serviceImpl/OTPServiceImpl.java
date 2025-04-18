package app.ewarehouse.serviceImpl;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.ewarehouse.dto.CommonResponseModal;
import app.ewarehouse.dto.Mail;
import app.ewarehouse.dto.OTPRequestDTO;
import app.ewarehouse.dto.OTPValidationRequestDTO;
import app.ewarehouse.entity.OTP;
import app.ewarehouse.entity.Tuser;
import app.ewarehouse.repository.IOtpRepository;
import app.ewarehouse.service.OTPService;
import app.ewarehouse.service.TuserService;
import app.ewarehouse.util.DateTimeUtil;
import app.ewarehouse.util.EmailUtil;

@Service
public class OTPServiceImpl implements OTPService {

	@Autowired
	IOtpRepository otpRepository;
	
	@Autowired
	TuserService userService;

	@Override
	public CommonResponseModal getOTP(OTPRequestDTO requestDTO) {

		CommonResponseModal dto = new CommonResponseModal();
		if (requestDTO.getMobile().equals("") && requestDTO.getEmail().equals("")) {
			dto.setMessage("Email ID or Mobile required");
			dto.setStatus(false);
			return dto;
		} else {
			try {
				String email = requestDTO.getEmail();
				String mobile = requestDTO.getMobile();

				Date d2 = otpRepository.currentOTPTime(email);
				int noOfOTP = otpRepository.getCurrentOtpCount(email);
				
				//Check Email and loginId
				List<Tuser> userDb = userService.findByMobileOrEmail(mobile, email);
				
				boolean userExists = userService.checkIfUserExists(email);
				
		        if (!userDb.isEmpty() || userExists) {
		            dto.setMessage("Email/Mobile Number Already Exists");
		            dto.setStatus(false);
		            return dto;
		        }

				if (noOfOTP == 0 && d2 == null) {

					List<OTP> otpList = otpRepository.findByFlagAndEmail(0, email);
					if (!otpList.isEmpty()) {
						for (OTP otp : otpList) {
							otp.setFlag(1);
							otp = otpRepository.saveAndFlush(otp);
						}
					}
					String ip = requestDTO.getIp();
					SecureRandom random = new SecureRandom();
					int generatedOtp = 100000 + random.nextInt(900000);
					String message = "Your OTP has been sent to your registered Email ID.";
					
					//"Your OTP has been sent to your registered email ID.

					OTP otp = new OTP();
					otp.setGeneratedOtp(generatedOtp);
					otp.setEmail(email);
					otp.setMobile(mobile);
					otp.setIpAddress(ip);
					otp.setEdt(new Date());
					otp.setLudt(new Date());
					otp.setFlag(0);
					otp = otpRepository.saveAndFlush(otp);
					
					//SEND OTP DURING REGISTRATION ONLY
					if (otp.getGeneratedOtp() != 0 && !"twofactor".equalsIgnoreCase(requestDTO.getType())) {
						Mail mail = new Mail();
						mail.setMailSubject("Otp Mail.");
						mail.setContentType("text/html");
						//mail.setMailCc("uiidptestmail@gmail.com");
						mail.setTemplate("Your registration OTP is: " + generatedOtp);
						mail.setMailTo(email);
						EmailUtil.sendMail(mail.getMailSubject(), mail.getTemplate(),mail.getMailTo());
					}
					
					dto.setOtp(String.valueOf(generatedOtp));
				    dto.setMessage("Your OTP Is "+generatedOtp);
//					dto.setMessage(message);
					dto.setStatus(true);
					return dto;

				} else {
					long timeDiff = DateTimeUtil.timeDiff(new Date(), d2, "Seconds");

					if (timeDiff < 5) {
						System.err.println(timeDiff);
						System.err.println(d2);
						System.err.println(new Date());
					    dto.setMessage("Please wait before requesting another OTP.");
					    dto.setStatus(false);
					    return dto;
					}

					// If more than 60 seconds have passed, issue a new OTP instead of blocking the user
					if (timeDiff > 60 || noOfOTP <= 8) {
						List<OTP> otpList = otpRepository.findByFlagAndEmail(0, email);
						if (!otpList.isEmpty()) {
							for (OTP otp : otpList) {
								otp.setFlag(1);
								otp = otpRepository.saveAndFlush(otp);
							}
						}
						SecureRandom random = new SecureRandom();
					    int generatedOtp =  100000 + random.nextInt(900000);
					    OTP otp = new OTP();
					    otp.setGeneratedOtp(generatedOtp);
					    otp.setEmail(requestDTO.getEmail());
					    otp.setMobile(requestDTO.getMobile());
					    otp.setIpAddress(requestDTO.getIp());
					    otp.setEdt(new Date());
					    otp.setLudt(new Date());
					    otp.setFlag(0);
					    otp=otpRepository.saveAndFlush(otp);
					  //SEND OTP DURING REGISTRATION ONLY
						if (otp.getGeneratedOtp() != 0 && !"twofactor".equalsIgnoreCase(requestDTO.getType())) {
							Mail mail = new Mail();
							mail.setMailSubject("Otp Mail.");
							mail.setContentType("text/html");
							//mail.setMailCc("uiidptestmail@gmail.com");
							mail.setTemplate("Your registration OTP is: " + generatedOtp);
							mail.setMailTo(email);
							EmailUtil.sendMail(mail.getMailSubject(), mail.getTemplate(),mail.getMailTo());
						}
					    dto.setOtp(String.valueOf(generatedOtp));
					    dto.setMessage("Your OTP Is "+generatedOtp);
//					    dto.setMessage("Your OTP has been sent to your registered Email ID.");
					    dto.setStatus(true);
					    return dto;
					} 

					dto.setMessage("You have exceeded the allotted attempts. Please try again later.");
					dto.setStatus(false);
					return dto;

				}
//				else {
//					Date d1 = new Date();
//					long time = DateTimeUtil.timeDiff(d1, d2, "Seconds");
//
//					if (time < 5 || time > 60) {
//						if (noOfOTP <= 8) {
//							String ip = requestDTO.getIp();
//							SecureRandom random = new SecureRandom();
//							int generatedOtp = 100000 + random.nextInt(900000);
////							String message = "Your OTP is " + generatedOtp;
//							String message = "Your OTP has been sent to your registered email ID.";
//							OTP otp = new OTP();
//							otp.setGeneratedOtp(generatedOtp);
//							otp.setEmail(email);
//							otp.setMobile(mobile);
//							otp.setIpAddress(ip);
//							otp.setEdt(new Date());
//							otp.setLudt(new Date());
//							otp.setFlag(0);
//							otp = otpRepository.saveAndFlush(otp);
//							dto.setOtp(String.valueOf(generatedOtp));
//							dto.setMessage(message);
//							dto.setStatus(true);
//							return dto;
//						} else {
//
//							dto.setMessage("You have exceeded the allotted time. Please try again after some timess first.");
//							dto.setStatus(false);
//
//							return dto;
//						}
//
//					} else {
//						List<OTP> otpList = otpRepository.findByFlagAndEmail(0, email);
//						if (!otpList.isEmpty()) {
//							for (OTP otp : otpList) {
//								otp.setFlag(1);
//								otp = otpRepository.saveAndFlush(otp);
//							}
//						}
//						dto.setMessage("You have exceeded the allotted time. Please try again after some timesss second.");
//						dto.setStatus(false);
//						System.out.println("i am from time else");
//						return dto;
//					}
//				}

			} catch (Exception e) {
				dto.setMessage(e.getLocalizedMessage());
				dto.setStatus(false);
				return dto;
			}
		}
	}

	@Override
	public CommonResponseModal validateOTP(OTPValidationRequestDTO otpdto) {

		CommonResponseModal dto = new CommonResponseModal();
		OTP otp = otpRepository.findByEmailAndFlag(otpdto.getEmail(), 0);
		if (otp != null) {
			if (!otpdto.getOtp().equals(Integer.toString(otp.getGeneratedOtp()))) {

				dto.setMessage("Invalid OTP.");
				dto.setStatus(false);
				return dto;
			}

			Date d2 = otpRepository.currentOTPTime(otpdto.getEmail());
			Date d1 = new Date();
			long time = DateTimeUtil.timeDiff(d1, d2, "Minutes");

			if (time >= 2) {
				dto.setMessage("OTP has been expired.");
				dto.setStatus(false);
				return dto;
			}

			otp.setFlag(1);
			otp = otpRepository.saveAndFlush(otp);
			dto.setMessage("OTP Validated");
			dto.setStatus(true);

		}else {
			dto.setMessage("Invalid or expired OTP. Please request a new one.");
			dto.setStatus(false);
		}

		return dto;
	}
}
