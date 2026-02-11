package com.zeafen.LocNetMonitoring.domain.stub;

import jakarta.persistence.*;
import jakarta.websocket.ClientEndpoint;

@Entity(name = "stub_users")
public class StubUser {
    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "smallint")
    @SequenceGenerator(
            name = "stub_users_id_seq",
            sequenceName = "stub_users_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "stub_users_id_seq"
    )
    private Short id;

    @Column(length = 50, unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(columnDefinition = "SMALLINT", nullable = false)
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
}
