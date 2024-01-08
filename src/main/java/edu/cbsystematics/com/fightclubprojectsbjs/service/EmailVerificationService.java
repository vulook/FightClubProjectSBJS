package edu.cbsystematics.com.fightclubprojectsbjs.service;

import edu.cbsystematics.com.fightclubprojectsbjs.model.AgeManager;
import edu.cbsystematics.com.fightclubprojectsbjs.model.ExpirationManager;
import edu.cbsystematics.com.fightclubprojectsbjs.model.Mail;
import edu.cbsystematics.com.fightclubprojectsbjs.model.User;
import edu.cbsystematics.com.fightclubprojectsbjs.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Service
public class EmailVerificationService {

    private static final Logger logger = LoggerFactory.getLogger(EmailVerificationService.class);

    private final UserRepository userRepository;

    @Autowired
    public EmailVerificationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mail buildVerificationEmail(User user, HttpServletRequest request) {
        // Create a new instance of Mail
        Mail mail = new Mail();
        mail.setFrom("admin@fight-club.com");
        mail.setTo(user.getEmail());
        mail.setSubject("Please verify your registration");

        // Populate the email template with necessary data
        Map<String, Object> modelAttributes = new HashMap<>();

        // Add the user object to the model
        modelAttributes.put("user", user);

        // Add a signature to the model
        modelAttributes.put("signature", "Fight Club. ");

        // Build the verification URL and add it to the model
        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String verificationUrl = url + "/verify?verificationCode=" + user.getVerificationCode();
        logger.info("Generated verification URL: {}", verificationUrl);
        modelAttributes.put("verificationUrl", verificationUrl);

        // Add expiry date to the model
        LocalDateTime expiryDateTime = ExpirationManager.expiryDateTimeVerificationCode(user.getDataReg());
        String expiryTimeVerificationCode = ExpirationManager.getExpiryDateTimeVerificationCode(expiryDateTime);
        modelAttributes.put("expiryTimeVerificationCode", expiryTimeVerificationCode);

        // Set the model for the email
        mail.setModel(modelAttributes);

        return mail;
    }

    // Verify the user based on the provided verification code
    public String verifyUser(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);

        if (user == null) {
            logger.error("Verification failed: User not found for verification code: {}", verificationCode);
            return "User not found for verification code.";
        }

        if (user.isEnabled()) {
            logger.error("Verification skipped: User already enabled with verification code: {}", verificationCode);
            return "User is already enabled";
        }

        boolean isExpired = ExpirationManager.isExpiredDateTimeVerificationCode(user.getDataReg());
        if (isExpired) {
            logger.error("Verification failed: Expired verification code: {}", verificationCode);

            // Delete the user
            userRepository.delete(user);
            return "Verification code has expired. Please register again.";
        }

        boolean isPermittedAge = AgeManager.isPermittedAgeForRegistration(user.getBirthDate());
        if (isPermittedAge) {
            logger.error("Verification failed: User age is not permitted for registration with verification code: {}", verificationCode);

            // Delete the user
            userRepository.delete(user);
            return "User's age is not permitted for registration. Please check the age requirements.";
        }


        // Mark the user as verified and activated
        user.setVerificationCode(null); // Clear the code
        user.setEnabled(true);
        userRepository.save(user);
        logger.info("Email '{}' successfully verified and activated. Account status: {}", user.getEmail(), user.isEnabled());

        return "SUCCESS";
    }

}
