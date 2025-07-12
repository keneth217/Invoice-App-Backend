package com.app.invoice.master.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
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


    public MasterRole(MasterERole name) {
        this.name = name;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(MasterERole name) {
        this.name = name;
    }
}
