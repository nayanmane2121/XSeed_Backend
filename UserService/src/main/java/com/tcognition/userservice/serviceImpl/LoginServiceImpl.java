package com.tcognition.userservice.serviceImpl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import com.tcognition.userservice.dto.EmailRequestDTO;
import com.tcognition.userservice.dto.JwtClaimsDTO;
import com.tcognition.userservice.dto.LoginRequestDTO;
import com.tcognition.userservice.dto.LoginResponseDTO;
import com.tcognition.userservice.dto.ResetPasswordRequestDTO;
import com.tcognition.userservice.dto.ResponseDTO;
import com.tcognition.userservice.dto.ValidateOtpDTO;
import com.tcognition.userservice.entity.UserEntity;
import com.tcognition.userservice.entity.UserRoleMappingEntity;
import com.tcognition.userservice.exception.UserValidationException;
import com.tcognition.userservice.jwt.JwtService;
import com.tcognition.userservice.repository.UserRepository;
import com.tcognition.userservice.repository.UserRoleMappingRepository;
import com.tcognition.userservice.service.EmailServiceClient;
import com.tcognition.userservice.service.LoginService;
//import com.tcognition.userservice.service.RestTemplateService;̥
import com.tcognition.userservice.utils.AppConstants;
import com.tcognition.userservice.utils.ErrorMessagesConstants;
//import com.tcognition.userservice.utils.JwtUtil;
import com.tcognition.userservice.utils.MessageConstants;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.type.PhoneNumber;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserRoleMappingRepository userRoleMappingRepository;

	@Autowired
	EmailServiceClient emailServiceClient;

//	@Value("${email.service.sendemail.url}")
//	private String emailServiceUrl;

	@Autowired
	private JwtService jwtService;

//	@Autowired
//	JwtUtil jwtUtil;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	Environment environment;

//	@Autowired
//	private RestTemplateService restTemplateService;

	private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

	private UserEntity validateUser(String userName) {
		logger.info("Validating user details for: {}", userName);

		Optional<UserEntity> userEntity = userRepository.findByEmailOrContactIgnoreCase(userName);

		if (userEntity.isEmpty()) {
			logger.warn("No user found for user: {}", userName);
			throw new UserValidationException(ErrorMessagesConstants.ERROR_NO_USER_PRESENT_IN_SYSTEM);
		}

		UserEntity user = userEntity.get();

		if (!user.isActive()) {
			logger.warn("User account is deactivated: {}", userName);
			throw new UserValidationException(ErrorMessagesConstants.ERROR_USER_DEACTIVATED);
		}

		return user;
	}

	@Override
	public ResponseDTO login(LoginRequestDTO loginRequest) {
		logger.info("Inside login()");
		logger.info("Fetching user details for: {}", loginRequest.getUserContact());

		try {
			UserEntity userEntity = validateUser(loginRequest.getUserContact());

			if (!loginRequest.getPassword().equals(userEntity.getPassword())) {
				logger.warn("Incorrect password for user: {}", loginRequest.getUserContact());
				throw new UserValidationException(ErrorMessagesConstants.ERROR_INCORRECT_USERNAME_OR_PASSWORD);
			}

			logger.info("Fetching role details for: {}", loginRequest.getUserContact());
			UserRoleMappingEntity userRoleMapping = userRoleMappingRepository.findByUser(userEntity);

			JwtClaimsDTO jwtClaims = new JwtClaimsDTO();
			jwtClaims.setUserId(userEntity.getId());
			jwtClaims.setUserName(userEntity.getEmail());
			jwtClaims.setUserRole(userRoleMapping.getRole().getRoleCode());

			LoginResponseDTO loginResponseDTO = new LoginResponseDTO();

			loginResponseDTO.setToken(jwtService.generateToken(jwtClaims));

			LoginResponseDTO.User userDTO = new LoginResponseDTO.User();

			userDTO.setEmail(userEntity.getEmail());
			userDTO.setId(userEntity.getId());
			userDTO.setName(userEntity.getName());
			userDTO.setUserRole(userRoleMapping.getRole().getRoleCode());

			loginResponseDTO.setUser(userDTO);

			return ResponseDTO.success(loginResponseDTO);

		} catch (UserValidationException e) {
			logger.error("Validation failed: {}", e.getMessage());
			return ResponseDTO.error(e.getMessage());
		} catch (Exception e) {
			logger.error(ErrorMessagesConstants.ERROR_LOGIN_PROCESS, e);
			return ResponseDTO.error(ErrorMessagesConstants.ERROR_LOGIN_PROCESS);
		}
	}

	@Override
	public ResponseDTO resetPassword(ResetPasswordRequestDTO resetPasswordRequestDTO) {
		logger.info("Inside resetPassword()");
		logger.info("Fetching user details for: {}", resetPasswordRequestDTO.getUserContact());

		

		try {

			UserEntity user = validateUser(resetPasswordRequestDTO.getUserContact());

			if (user != null && resetPasswordRequestDTO.getMode().equals("SMS")) {
				return sendOtpViaSms(user);
			}

			if (user != null && resetPasswordRequestDTO.getMode().equals("EMAIL")) {

				return sendOtpViaEmail(user);
			}

			// return restTemplateService.callExternalServiceWithBody(emailServiceUrl,
			// emailRequestDTO);

			return ResponseDTO.error("Please select correct mode.");

		} catch (UserValidationException e) {
			logger.error("Validation failed: {}", e.getMessage());
			return ResponseDTO.error(e.getMessage());
		} catch (Exception e) {
			logger.error(ErrorMessagesConstants.ERROR_PASSWORD_RESET_PROCESS, e);
			return ResponseDTO.error(ErrorMessagesConstants.ERROR_PASSWORD_RESET_PROCESS);
		}
	}

	private ResponseDTO sendOtpViaSms(UserEntity user) {

		try {
		Twilio.init(environment.getProperty("twilio_acc_sid"), environment.getProperty("twilio_auth_token"));
		
//		Message.creator(new PhoneNumber("+918379956518"),
//                new PhoneNumber("+919588645519"), "Hello! Your One-Time Password (OTP) is: " +generateOtp()+". Please use it within the next 5 minutes").create();
//		
		
		 Message message = Message
                 .creator(new com.twilio.type.PhoneNumber("+918379956518"),
                     new com.twilio.type.PhoneNumber("+919588645519"),
                     "Hello! Your One-Time Password (OTP) is: " +generateOtp()+". Please use it within the next 5 minutes")
                 .create();

		 
//		    Verification verification = Verification.creator(
//		            "VAb8b06774817f6169a2e588f237452b2b",
//		            "+919588645519",
//		            "sms")
//		        .create();

		 //   System.out.println(verification.getSid());
		  
		
		
//		try {
//			String apiKey = "MzU0OTRhNGU0NjM5NGEzODc0NTYzNTQ4NmQ2NzM1MzU=";
//			String sender = "XSEEDAI"; // Customize sender ID (Optional)
//
//			// Create a custom message with the OTP
//			String message = "Hello! Your One-Time Password (OTP) is: " + generateOtp()
//					+ ". Please use it within the next 5 minutes.";
//
//			// Number to send the OTP to (in international format, e.g., +91 for India)
//			String numbers = user.getContact();
//
//			// Construct the API request URL
//			String url = "https://api.textlocal.in/send/?apikey=" + apiKey + "&numbers=" + numbers + "&message="
//					+ message + "&sender=" + sender;
//
//			// Create a URL object and open a connection to the API
//			URL obj = new URL(url);
//			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//			con.setRequestMethod("GET");
//			con.setDoOutput(true);
//
//			// Send the HTTP request
//			int responseCode = con.getResponseCode();
//
//			System.out.println("Response Code : " + responseCode); // Response code to check if message was sent
			return ResponseDTO.success("OTP sent successfully...");
		} catch (Exception e) {
			e.printStackTrace(); // Handle any exceptions

			return ResponseDTO.success("Error while sending OTP...");
		}
	}

	private ResponseDTO sendOtpViaEmail(UserEntity user) {

		EmailRequestDTO emailRequestDTO = new EmailRequestDTO();
		
		emailRequestDTO.setSubject(MessageConstants.SUBJECT_RESET_PASSWORD);
		emailRequestDTO.setTemplateName(AppConstants.TEMPLATE_RESET_PASSWORD);
		emailRequestDTO.setMessage(generateOtp());
		emailRequestDTO.setTo(user.getEmail());
		emailRequestDTO.setName(user.getName());
		
		return ResponseDTO.success(emailServiceClient.sendEmail(emailRequestDTO));

	}

	public static String generateOtp() {
		SecureRandom random = new SecureRandom();
		int otp = random.nextInt(1_000_000); // Generates a number between 0 and 999999
		return String.format("%06d", otp); // Zero-pads to 6 digits
	}

	@Override
	public ResponseDTO validateOTP(ValidateOtpDTO validateOtpDTO) {

		logger.info("Inside validateOTP()");
		logger.info("Fetching user details for: {}", validateOtpDTO.getEmail());

		try {
			UserEntity user = validateUser(validateOtpDTO.getEmail());

			if (user != null) {

				logger.info("Validating OTP...");
				ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

				if (!validateOtpDTO.getOtp().equals(valueOperations.get("OTP:" + user.getName()))) {

					return ResponseDTO.error(ErrorMessagesConstants.ERROR_INCORRECT_OTP);
				}

			}
		} catch (Exception e) {
			logger.error(ErrorMessagesConstants.ERROR_OTP_VARIFICATION, e);
			return ResponseDTO.error(ErrorMessagesConstants.ERROR_OTP_VARIFICATION);
		}

		return ResponseDTO.success(MessageConstants.OTP_VERIFIED);
	}

}
