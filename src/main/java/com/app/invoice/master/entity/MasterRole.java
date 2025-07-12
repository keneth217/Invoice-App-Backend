package com.app.invoice.master.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "roles")
public class MasterRole implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoleID")
    private Integer id;
    @Enumerated(EnumType.STRING)
    @Column(length = 20, name = "RoleName")
    private MasterERole name;

    public MasterRole() {
    }

    public MasterRole(MasterERole name) {
        this.name = name;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MasterERole getName() {
        return name;
    }

    public void setName(MasterERole name) {
        this.name = name;
    }
}
