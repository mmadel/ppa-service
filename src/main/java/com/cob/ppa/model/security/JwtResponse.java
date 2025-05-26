package com.cob.ppa.model.security;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public  class JwtResponse {
    private String accessToken;
    private String username;
    private List<String> roles;
}
