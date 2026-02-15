package com.zeafen.LocNetMonitoring.domain.stub;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<StubUser, Short> {
    @Query(
            value = "SELECT * FROM stub_users as su WHERE" +
                    "(:name IS NULL OR su.name LIKE %:name%) AND" +
                    "(:role IS NULL OR su.role = :role)",
            countQuery = "SELECT * FROM stub_users as su WHERE" +
                    "(:name IS NULL OR su.name LIKE %:name%) AND" +
                    "(:role IS NULL OR su.role = :role)",
            nativeQuery = true
    )
    List<StubUser> getUsersFiltered(
            @Param("name") String name,
            @Param("role") Integer role
    );
}
