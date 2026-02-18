package com.zeafen.LocNetMonitoring.data.controller;

import com.zeafen.LocNetMonitoring.domain.RoleNotAllowedException;
import com.zeafen.LocNetMonitoring.domain.stub.StubUser;
import com.zeafen.LocNetMonitoring.domain.stub.StubUserUiModel;
import com.zeafen.LocNetMonitoring.domain.stub.UserRole;
import com.zeafen.LocNetMonitoring.domain.stub.UsersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/users")
public class UserRolesController {
    @Autowired
    private UsersService usersService;

    @GetMapping
    public String userRolesList(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "role", required = false) Integer role,
            @RequestParam(name = "user", required = false) Short selectedUserId,
            Model model
    ) {
        var users = usersService.getUsersFiltered(name, role).stream().map(StubUser::toUiModel).toList();

        if (selectedUserId != null)
            model.addAttribute("userModel", usersService.getUserById(selectedUserId));
        model.addAttribute("users", users);
        model.addAttribute("name", name);
        model.addAttribute("role", role);
        model.addAttribute("roles", UserRole.values());

        return "auth/rolesList";
    }

    @PostMapping("/save")
    public String saveRole(
            @Valid @ModelAttribute StubUserUiModel user,
            BindingResult bindingResult,
            RedirectAttributes attributes
    ) {
        if (bindingResult.hasErrors()) {
            attributes.addFlashAttribute("errorMessage", bindingResult.getAllErrors());
            return "redirect:/error";
        }

        usersService.saveUser(user.toEntity());
        return "redirect:/users";
    }
}
