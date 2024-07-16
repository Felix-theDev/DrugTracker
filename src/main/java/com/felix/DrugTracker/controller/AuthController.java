package com.felix.DrugTracker.controller;

import com.felix.DrugTracker.entity.User;
import com.felix.DrugTracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController  {



    private final UserService userService;


    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        user.setPassword(user.getPassword());
        userService.registerUser(user.getUsername(), user.getPassword(), user.getRole());
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/home")
    public String loginUser() {
        System.out.println("success logging in");
        return "redirect:/login";

    }
}
