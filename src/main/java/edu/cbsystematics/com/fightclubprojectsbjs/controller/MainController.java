package edu.cbsystematics.com.fightclubprojectsbjs.controller;

import edu.cbsystematics.com.fightclubprojectsbjs.internal.PortAggregator;
import edu.cbsystematics.com.fightclubprojectsbjs.model.Mail;
import edu.cbsystematics.com.fightclubprojectsbjs.model.User;
import edu.cbsystematics.com.fightclubprojectsbjs.service.EmailService;
import edu.cbsystematics.com.fightclubprojectsbjs.service.EmailVerificationService;
import edu.cbsystematics.com.fightclubprojectsbjs.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/")
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private final UserService userService;

    private final EmailService emailService;

    private final EmailVerificationService verificationService;

    private final PortAggregator portAggregator;

    @Autowired
    public MainController(UserService userService, EmailService emailService, EmailVerificationService verificationService, PortAggregator portAggregator) {
        this.userService = userService;
        this.emailService = emailService;
        this.verificationService = verificationService;
        this.portAggregator = portAggregator;
    }

    // This method maps the GET request to the "/fight-club" endpoint
    @GetMapping("/fight-club")
    public String getViewMainPage(Model model) {
        return "view-main-page";
    }

    // This method maps the GET request to the "/login" endpoint
    @GetMapping("/login")
    public String getLogin(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           Model model) {


        int serverPort = portAggregator.getServerPort();
        int anotherPort = portAggregator.getAnotherPort();
        String serverURL = "http://localhost:" + serverPort + "/server/all";
        String internalURL = "http://localhost:" + anotherPort + "/internal/all";
        String internalGetURL = "http://localhost:" + anotherPort + "/internal/get-users";

        model.addAttribute("portServer", serverPort);
        model.addAttribute("portAnother", anotherPort);
        model.addAttribute("serverURL", serverURL);
        model.addAttribute("internalURL", internalURL);
        model.addAttribute("internalGetURL", internalGetURL);
        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        return "login";
    }

    // This method maps the GET request to the "/registration"
    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        User regUser = new User();
        model.addAttribute("regUser", regUser);
        return "registration";
    }


    // This method maps the POST request to the "/registration"
    @PostMapping("/registration")
    public String registerNewUser(@Validated @ModelAttribute("regUser") User regUser,
                                  BindingResult result, HttpServletRequest request, Model model) {

        User usernameExisting = userService.findByUsername(regUser.getUsername());
        if (usernameExisting != null) {
            result.rejectValue("username", null, "There is already an account registered with that Username");
        }

        User emailExisting = userService.findByMail(regUser.getEmail());
        if (emailExisting != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }

        if (result.hasErrors()) {
            logger.error("Validation errors: {}", result);
            model.addAttribute("error", "There are validation errors. Please check the form.");
            return "registration";
        }

        // Register a new user and obtain the saved user details.
        User savedUser = userService.registerNewUser(regUser);

        // Build an email with the verification instructions for the new user.
        Mail mail = verificationService.buildVerificationEmail(savedUser, request);

        // Send the verification email to the user.
        emailService.sendEmail(mail);

        // Redirect the user to the login page after successful registration.
        return "redirect:/registration?success";
    }

    @GetMapping("/verify")
    public String verifyUser(@RequestParam("verificationCode") String verificationCode,
                             RedirectAttributes redirectAttributes) {

        // Verify user in the service
        String result = verificationService.verifyUser(verificationCode);

        if ("SUCCESS".equals(result)) {

            redirectAttributes.addFlashAttribute("verificationSuccess", true);

        } else {

            redirectAttributes.addFlashAttribute("verificationError", result);
        }

        return "redirect:/login";
    }

}