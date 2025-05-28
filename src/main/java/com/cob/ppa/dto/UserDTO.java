package com.cob.ppa.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
}
