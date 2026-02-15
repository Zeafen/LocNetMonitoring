package com.zeafen.LocNetMonitoring.domain.stub;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class JpaUsersService implements UsersService {
    private final UserRepository _users;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public JpaUsersService(UserRepository users) {
        _users = users;
    }

    @Override
    public StubUser getUserByName(String name) {
        return _users.findAll().stream().filter((user) -> Objects.equals(user.getName(), name)).findFirst().orElse(null);
    }

    @Override
    public List<StubUser> getUsersFiltered(@Nullable String name, @Nullable Integer role) {
        return _users.getUsersFiltered(name, role);
    }

    @Override
    public StubUser saveUser(StubUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return _users.save(user);
    }

    @Override
    public StubUser getUserById(Short id) {
        return _users.findById(id).orElse(null);
    }
}
