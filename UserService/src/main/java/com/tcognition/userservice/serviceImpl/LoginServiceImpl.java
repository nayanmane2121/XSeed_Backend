package com.tcognition.userservice.serviceImpl;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.tcognition.userservice.utils.AppConstants;
import com.tcognition.userservice.utils.ErrorMessagesConstants;
import com.tcognition.userservice.utils.MessageConstants;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserRoleMappingRepository userRoleMappingRepository;

	@Autowired
	EmailServiceClient emailServiceClient;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	Environment environment;

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

			if (user != null && resetPasswordRequestDTO.getMode().equals(AppConstants.MODE_SMS)) {
				return sendOtpViaSms(user);
			}

			if (user != null && resetPasswordRequestDTO.getMode().equals(AppConstants.MODE_EMAIL)) {

				return sendOtpViaEmail(user);
			}

			return ResponseDTO.error(ErrorMessagesConstants.ERROR_CORRECT_MODE_SELECTION);

		} catch (UserValidationException e) {
			logger.error("Validation failed: {}", e.getMessage());
			return ResponseDTO.error(e.getMessage());
		} catch (Exception e) {
			logger.error(ErrorMessagesConstants.ERROR_PASSWORD_RESET_PROCESS, e);
			return ResponseDTO.error(ErrorMessagesConstants.ERROR_PASSWORD_RESET_PROCESS);
		}
	}

	private ResponseDTO sendOtpViaSms(UserEntity user) {

		logger.info("Inside sendOtpViaSms()");

		try {
			Twilio.init(environment.getProperty("twilio_acc_sid"), environment.getProperty("twilio_auth_token"));

			Message message = Message.creator(new com.twilio.type.PhoneNumber("+918379956518"),
					new com.twilio.type.PhoneNumber("+919588645519"), "Hello! Your One-Time Password (OTP) is: "
							+ generateOtp() + ". Please use it within the next 5 minutes")
					.create();

			return ResponseDTO.success(MessageConstants.OTP_SENT_SUCCESSFULLY);
		} catch (Exception e) {
			e.printStackTrace(); // Handle any exceptions

			return ResponseDTO.error(ErrorMessagesConstants.ERROR_SENDING_OTP);
		}
	}

	private ResponseDTO sendOtpViaEmail(UserEntity user) {

		logger.info("Inside sendOtpViaEmail()");

		EmailRequestDTO emailRequestDTO = new EmailRequestDTO();

		emailRequestDTO.setSubject(MessageConstants.SUBJECT_RESET_PASSWORD);
		emailRequestDTO.setTemplateName(AppConstants.TEMPLATE_RESET_PASSWORD);
		emailRequestDTO.setMessage(generateOtp());
		emailRequestDTO.setTo(user.getEmail());
		emailRequestDTO.setName(user.getName());

		logger.info("Saving OTP in redis...");

		// Saving the OTP in redis
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		valueOperations.set("OTP:" + emailRequestDTO.getName(), emailRequestDTO.getMessage(), Duration.ofMinutes(5));

		logger.info("OTP saved...");

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
