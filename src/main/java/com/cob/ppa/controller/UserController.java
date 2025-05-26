package com.cob.ppa.controller;

import com.cob.ppa.service.user.FindUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    FindUserService findUserService;

    @GetMapping("/find")
    public ResponseEntity findUsers() {
        return ResponseEntity.ok()
                .body(findUserService.findAll());
    }
}
