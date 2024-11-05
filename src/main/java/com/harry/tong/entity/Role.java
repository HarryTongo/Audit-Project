package com.harry.tong.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "roles")
@Data
public class Role {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "role_name")
    private String role_name;
}
