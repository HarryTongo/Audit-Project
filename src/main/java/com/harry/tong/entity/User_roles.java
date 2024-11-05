package com.harry.tong.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "user_roles")
public class User_roles {

    @Id
    @Column(name = "user_id")
    private long user_id;

    @Id
    @Column(name = "role_id")
    private long role_id;
}
