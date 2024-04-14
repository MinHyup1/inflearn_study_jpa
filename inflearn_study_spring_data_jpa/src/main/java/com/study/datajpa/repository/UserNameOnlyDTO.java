package com.study.datajpa.repository;

import lombok.Getter;

@Getter
public class UserNameOnlyDTO {
    private final String userName;

    public UserNameOnlyDTO(String userName) {
        this.userName = userName;
    }
}
