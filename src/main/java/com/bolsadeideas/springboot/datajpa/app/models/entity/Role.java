package com.bolsadeideas.springboot.datajpa.app.models.entity;

import jakarta.persistence.*;

@Entity
@Table(name="authorities", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "authority"})})
public class Role {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private Long user_id;

    private String authority;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
