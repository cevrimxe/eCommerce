package com.example.eCommerce.controllers;

import com.example.eCommerce.model.User;
import com.example.eCommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    // Register işlemi
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        userService.registerUser(user);
        return "redirect:/login";  // Kayıt olduktan sonra login sayfasına yönlendiriyoruz.
    }

    // Login işlemi
    @PostMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password) {
        if (userService.loginUser(email, password) != null) {
            return "redirect:/";  // Giriş başarılı ise ana sayfaya yönlendir
        }
        return "redirect:/login";  // Giriş başarısız ise login sayfasına geri yönlendir
    }
}