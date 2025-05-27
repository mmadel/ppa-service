package com.cob.ppa.controller;

import com.cob.ppa.entity.security.Permission;
import com.cob.ppa.entity.security.Role;
import com.cob.ppa.entity.security.User;
import com.cob.ppa.model.security.*;
import com.cob.ppa.repository.PermissionRepository;
import com.cob.ppa.repository.RoleRepository;
import com.cob.ppa.repository.UserRepository;
import com.cob.ppa.util.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.GrantedAuthority;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtTokenUtil.generateToken(userDetails);

            return ResponseEntity.ok(new JwtResponse(
                    jwt,
                    userDetails.getUsername(),
                    userDetails.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList())
            ));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ErrorResponse("Invalid username or password")
            );
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setEmail(signUpRequest.getEmail());
        Set<Role> roles = new HashSet<>();

        // Assign USER role by default
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Error: Role not found."));
        roles.add(userRole);

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/assign-role")
    public ResponseEntity<?> assignRole(@RequestBody RoleAssignmentRequest request) {
        System.out.println("request.getUsername() " + request.getUsername());
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("request.getRoleName() " + request.getRoleName());
        Role role = roleRepository.findByName(request.getRoleName())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.getRoles().add(role);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse(
                "Role " + request.getRoleName() + " assigned to user " + request.getUsername()));
    }

    @PostMapping("/create-role")
    @PreAuthorize("hasAuthority('USER_CREATE')")
    public ResponseEntity<?> createRole(@RequestBody RoleCreationRequest request) {
        if (roleRepository.existsByName(request.getName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("Error: Role already exists!"));
        }

        Role role = new Role(request.getName());
        roleRepository.save(role);

        return ResponseEntity.ok(new MessageResponse("Role created successfully!"));
    }

    @PostMapping("/assign-permission")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public ResponseEntity<?> assignPermission(@RequestBody PermissionAssignmentRequest request) {
        Role role = roleRepository.findByName(request.getRoleName())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        Permission permission = permissionRepository.findByName(request.getPermissionName())
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found"));

        role.getPermissions().add(permission);
        roleRepository.save(role);

        return ResponseEntity.ok(new MessageResponse(
                "Permission " + request.getPermissionName() + " assigned to role " + request.getRoleName()));
    }
}
