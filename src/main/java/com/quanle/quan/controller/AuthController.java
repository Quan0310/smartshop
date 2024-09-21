package com.quanle.quan.controller;

import com.quanle.quan.models.entity.Role;
import com.quanle.quan.models.entity.User;
import com.quanle.quan.models.entity.UserProvider;
import com.quanle.quan.models.enums.AuthProvider;
import com.quanle.quan.models.enums.ERole;
import com.quanle.quan.models.request.LoginRequest;
import com.quanle.quan.models.request.SignupRequest;
import com.quanle.quan.models.response.AuthResponse;
import com.quanle.quan.models.response.MessageResponse;
import com.quanle.quan.repository.RoleRepository;
import com.quanle.quan.repository.UserRepository;
import com.quanle.quan.security.jwt.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

//    @Autowired
//    RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtils.createToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @GetMapping("/ok")
    public String te() {
        Role userRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        return "quan";
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getName(),signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);
        user.setRoles(roles);
        user.setId(UUID.randomUUID().toString());

        UserProvider firstProvider = new UserProvider();
        firstProvider.setId(UUID.randomUUID().toString());
        firstProvider.setProvider(AuthProvider.local);
        firstProvider.setUser(user);

        List<UserProvider> providers = new ArrayList<>();
        providers.add(firstProvider);
        user.setProviders(providers);

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

//    @PostMapping("/refreshtoken")
//    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
//        String requestRefreshToken = request.getRefreshToken();
//
//        return refreshTokenService.findByToken(requestRefreshToken).map(refreshTokenService::verifyExpiration)
//                .map(RefreshToken::getUser).map(user -> {
//                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
//                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
//                })
//                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
//    }
//
//    @PostMapping("/signout")
//    public ResponseEntity<?> logoutUser() {
//        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
//                .getPrincipal();
//        Long userId = userDetails.getId();
//        refreshTokenService.deleteByUserId(userId);
//        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
//    }

}