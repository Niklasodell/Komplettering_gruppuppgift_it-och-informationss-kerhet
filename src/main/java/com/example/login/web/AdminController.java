package com.example.login.web;

import com.example.login.model.User;
import com.example.login.model.UserDTO;
import com.example.login.service.UserService;
import com.example.login.util.MaskingUtils;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.util.HtmlUtils;

import java.util.List;
import java.util.Optional;

/**
 * AdminController hanterar webbförfrågningar relaterade till administration, såsom registrering och borttagning av användare.
 */
@Controller
public class AdminController {

    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(AdminController.class);

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserDTO());
        logger.debug("Register user");
        return "register_form";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult, Model model) {
        logger.debug("Processing registration for email: {}", MaskingUtils.anonymize(userDTO.getEmail()));
        if (bindingResult.hasErrors()) {
            logger.warn("Registration failed due to validation errors for email: {}", MaskingUtils.anonymize(userDTO.getEmail()));
            return "register_form";
        }

        try {
            userService.registerUser(userDTO);
            logger.debug("User successfully registered with email: {}", MaskingUtils.anonymize(userDTO.getEmail()));
            return "register_success";
        } catch (DataIntegrityViolationException e) {
            logger.warn("Registration failed due to email already being registered: {}", MaskingUtils.anonymize(userDTO.getEmail()));
            model.addAttribute("error", "Email already registered");
            return "register_error";
        } catch (Exception e) {
            logger.error("An unexpected error occurred during registration for email: {}", MaskingUtils.anonymize(userDTO.getEmail()), e);
            model.addAttribute("error", "An unexpected error occurred");
            return "register_error";
        }
    }

    @GetMapping("/homepage")
    public String loggedIn() {
        logger.debug("Homepage");
        return "homepage";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("user", new UserDTO());
        logger.debug("Showing login page.");
        return "login";
    }

    @GetMapping("/admin")
    public String adminPage() {
        logger.debug("Admin accessed admin page.");
        return "admin_page";
    }

    @GetMapping("/users")
    public String userPage(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        logger.debug("Showing usersPage with a list of registered users.");
        return "users_list";
    }

    @GetMapping("/delete")
    public String deleteUserForm(Model model) {
        logger.debug("Displaying user deletion form.");
        model.addAttribute("user", new UserDTO());
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "delete_form";
    }


    @PostMapping("/delete")
    public String deleteUser(@ModelAttribute("user") UserDTO user, Model model) {
        logger.debug("Processing user deletion.");

        try {

            Optional<User> userOptional = Optional.ofNullable(userService.findByEmail( HtmlUtils.htmlEscape(user.getEmail())));
            if (userOptional.isPresent()) {
                User user1 = userOptional.get();

                if (!user1.getRole().equals("ROLE_ADMIN")) {
                    logger.debug("User does not have the ADMIN role.");

                    userService.deleteUser(user1.getEmail());

                    logger.debug("User deleted: {}", MaskingUtils.anonymize(user1.getEmail()));
                    return "delete_success";
                } else {
                    logger.warn("ADMIN cannot be deleted.");
                    return "admin_error";
                }
            } else {
                logger.warn("User {} not found for deletion.", MaskingUtils.anonymize(user.getEmail()));
                model.addAttribute("id", HtmlUtils.htmlEscape(user.getEmail()));
                return "user_not_found";
            }
        } catch (Exception e) {
            logger.error("An error occurred while deleting the user: {}", MaskingUtils.anonymize(user.getEmail()), e);
            model.addAttribute("error", "An error occurred while deleting the user.");
            return "delete_error";
        }
    }

    @GetMapping("/delete_success")
    public String deleteSuccess() {
        logger.debug("User deleted successfully.");
        return "delete_success";
    }

    @GetMapping("/delete-error")
    public String deleteError() {
        logger.debug("Error during user deletion.");
        return "delete_error";
    }
}
