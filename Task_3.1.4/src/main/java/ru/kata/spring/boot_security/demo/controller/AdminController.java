package ru.kata.spring.boot_security.demo.controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String index(@NotNull Principal principal, @NotNull Model model) {
        List<User> users = userService.getAllUsers();
        User infoUser = userService.getUserByUsername(principal.getName());
        model.addAttribute("userInf", infoUser);
        model.addAttribute("users", users);
        return "AdminPage";
    }

    @PostMapping("/adduser")
    public String createUser(@ModelAttribute @Valid User user, @RequestParam("roles") String[] selectedRoles, BindingResult bindingResult) {
        // Валидация полей объекта User
        if (bindingResult.hasErrors()) {
            return "redirect:/admin";
        }
        // Создание пользователя с ролями
        userService.createUserWithRoles(user, selectedRoles);
        return "redirect:/admin";
    }

    @PostMapping("/edit")
    public String editUserSubmit(@ModelAttribute @Valid User editedUser, @RequestParam("roles") String[] selectedRoles, BindingResult bindingResult) {
        // Валидация полей объекта User
        if (bindingResult.hasErrors()) {
            return "redirect:/admin";
        }
        // Редактирование пользователя с ролями
        userService.editUserWithRoles(editedUser, selectedRoles);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUserSubmit(@ModelAttribute User user) {
        userService.delete(user.getId());
        return "redirect:/admin";
    }
}