package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.security.Principal;

@Controller
public class UsersController {
    private final UserService userService;
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public UsersController(UserService userService, UserServiceImpl userServiceImpl) {
        this.userService = userService;
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping("/user")
    public String userPage (Principal principal, Model model) {
        User user = userServiceImpl.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "user";
    }

    /////CRUD Operations///////////////////////////////////////

    @GetMapping("/admin")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "getAll";
    }

    @GetMapping("/admin/{id}")
    public String find(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "find";
    }

    @GetMapping("/admin/new")
    public String newUser(@ModelAttribute("user") User user) {
        return "new";
    }

    @PostMapping("/admin")
    public String create(@ModelAttribute("user") User user) {
        userService.addNewUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/{id}/edit")
    public String edit(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("user", userService.findById(id));
        return "edit";
    }

    @PatchMapping("/admin/{id}")
    public String update(@ModelAttribute("user") User user, @PathVariable("id") Integer id) {
        userService.updateUser(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("/admin/{id}")
    public String delete(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
