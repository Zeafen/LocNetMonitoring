package com.zeafen.LocNetMonitoring.domain.stub;

public interface UsersService {
    StubUser getUserByName(String name);

    StubUser saveUser(StubUser user);
}
