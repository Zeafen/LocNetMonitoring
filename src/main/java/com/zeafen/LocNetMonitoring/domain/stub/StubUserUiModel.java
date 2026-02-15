package com.zeafen.LocNetMonitoring.domain.stub;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.jspecify.annotations.Nullable;

import java.util.function.Function;

public class StubUserUiModel {
    @NotNull
    private Short id;

    @Size(min = 5, max = 50, message = "Длина наименования пользователя должна составлять от 5 до 50 сиволов")
    private String name;

    @Size(min = 6, message = "Длина пароля должна составлять от 6 сиволов")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z]).+$", message = "Пароль делжен содержать как минимум 1 заглавную букву и 1 прописную букву")
    @Pattern(regexp = "^(?=.*[0-9]).+$", message = "Пароль делжен содержать как минимум 1 цифру")
    @Pattern(regexp = "^(?=.*[!\"#$%&'()*+,\\-./:;<=>?@\\[\\]^_{}~]).+$", message = "Пароль делжен содержать как минимум 1 специальный символ")
    private String password;

    @NotNull
    private Short role;

    public Short getRole() {
        return role;
    }

    public void setRole(Short role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public Short getId() {
        return this.id;
    }

    public StubUser toEntity() {
        StubUser user = new StubUser();
        user.setId(this.id);
        user.setName(this.name);
        user.setRole(this.role);
        user.setPassword(this.password);
        return user;
    }
}
