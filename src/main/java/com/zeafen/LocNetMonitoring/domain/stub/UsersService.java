package com.zeafen.LocNetMonitoring.domain.stub;

import org.jspecify.annotations.Nullable;

import java.util.List;

public interface UsersService {
    StubUser getUserByName(String name);

    StubUser getUserById(Short id);

    List<StubUser> getUsersFiltered(
            @Nullable String name,
            @Nullable Integer role);

    StubUser saveUser(StubUser user);
}
