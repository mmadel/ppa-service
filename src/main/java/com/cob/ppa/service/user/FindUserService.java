package com.cob.ppa.service.user;

import com.cob.ppa.dto.UserDTO;
import com.cob.ppa.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FindUserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ModelMapper mapper;

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(user -> mapper.map(user, UserDTO.class))
                .collect(Collectors.toList());

    }
}
