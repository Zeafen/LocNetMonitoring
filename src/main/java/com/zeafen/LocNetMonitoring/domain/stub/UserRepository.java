package com.zeafen.LocNetMonitoring.domain.stub;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<StubUser, Short> {

}
