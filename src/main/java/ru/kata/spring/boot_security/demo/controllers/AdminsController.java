package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminsController {
    private final UserService userService;
    private final UserServiceImpl userServiceImpl;
    private final RoleRepository roleRepository;
    private final RoleService roleService;

    @Autowired
    public AdminsController(UserService userService, UserServiceImpl userServiceImpl, RoleRepository roleRepository, RoleService roleService) {
        this.userService = userService;
        this.userServiceImpl = userServiceImpl;
        this.roleRepository = roleRepository;
        this.roleService = roleService;
    }

    @GetMapping
    public String showUsersList(@AuthenticationPrincipal UserDetails userDetails, Principal principal, Model model) {
        model.addAttribute("usersList", userService.getAllUsers());
        model.addAttribute("userDetails", userDetails);
        model.addAttribute("userAuthorized", userServiceImpl.findByEmail(userDetails.getUsername()));
        model.addAttribute("roles", roleService.findAll());
        return "adminPage";
    }

    @GetMapping("/new")
    public String showNewUserForm(@AuthenticationPrincipal UserDetails userDetails, Principal principal, Model model) {
        model.addAttribute("userDetails", userDetails);
        model.addAttribute("userAuthorized", userServiceImpl.findByEmail(userDetails.getUsername()));
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("user", new User());
        return "/new";
    }

    @PostMapping("/new")
    public String saveNewUser(@ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/new";
        }
        userService.addNewUser(user);
        return "redirect:/admin";
    }

    @PatchMapping("/edit/{id}")
    public String updateUser(@ModelAttribute("user") User user, BindingResult result, @PathVariable("id") Integer id) {

        if (result.hasErrors()) {
            return "redirect:/edit/{id}";
        }
//        if (user.hasRole("ROLE_ADMIN")) {
//            user.setUserRoles(roleRepository.findById(2).orElse(null));
//        }
//        if (user.hasRole("ROLE_USER")) {
//            user.setUserRoles(roleRepository.findById(1).orElse(null));
//        }

        userService.updateUser(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

}
