package com.study.datajpa.dto;


import lombok.Data;

@Data
public class MemberDTO {
    private Long id;
    private String userName;
    private String teamName;

    public MemberDTO(Long id, String userName, String teamName) {
        this.id = id;
        this.userName = userName;
        this.teamName = teamName;
    }
}
