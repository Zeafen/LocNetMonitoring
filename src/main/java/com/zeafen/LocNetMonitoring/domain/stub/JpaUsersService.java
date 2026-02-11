package com.zeafen.LocNetMonitoring.domain.stub;

import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class JpaUsersService implements UsersService {
    private UserRepository _users;

    public JpaUsersService(UserRepository users) {
        _users = users;
    }

    @Override
    public StubUser getUserByName(String name) {
        return _users.findAll().stream().filter((user) -> Objects.equals(user.getName(), name)).findFirst().orElse(null);
    }

    @Override
    public StubUser saveUser(StubUser user) {
        return _users.save(user);
    }
}
