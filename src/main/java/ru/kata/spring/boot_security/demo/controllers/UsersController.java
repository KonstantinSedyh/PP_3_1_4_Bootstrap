package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.security.Principal;

@Controller
public class   UsersController {

    private final UserServiceImpl userServiceImpl;
    private final RoleService roleService;

    @Autowired
    public UsersController(UserServiceImpl userServiceImpl, RoleService roleService) {
        this.userServiceImpl = userServiceImpl;
        this.roleService = roleService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/user")
    public String userPage(@AuthenticationPrincipal UserDetails userDetails, Principal principal, Model model) {
        model.addAttribute("currentUser", userServiceImpl.findByEmail(userDetails.getUsername()));
        model.addAttribute("userDetails", userDetails);
        model.addAttribute("user", roleService.findByName("ROLE_ADMIN"));
        return "user";
    }
}
