package com.quanle.quan.security.oauth2;


import com.quanle.quan.exception.OAuth2AuthenticationProcessingException;
import com.quanle.quan.models.entity.Role;
import com.quanle.quan.models.entity.User;
import com.quanle.quan.models.entity.UserProvider;
import com.quanle.quan.models.enums.AuthProvider;
import com.quanle.quan.models.enums.ERole;
import com.quanle.quan.repository.RoleRepository;
import com.quanle.quan.repository.UserRepository;
import com.quanle.quan.security.oauth2.user.OAuth2UserInfo;
import com.quanle.quan.security.oauth2.user.OAuth2UserInfoFactory;
import com.quanle.quan.security.services.UserPrincipal;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        System.out.println("oAuth2UserInfo "+ oAuth2UserInfo.getEmail());

        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());

        User user;
        if(userOptional.isPresent()) {
            user = userOptional.get();
            List<UserProvider> providers = user.getProviders();
            boolean hasMatchingProvider = providers.stream()
                    .anyMatch(provider -> provider.getProvider().equals(
                            AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId())
                    ));
            System.out.println("test1");
            if(!hasMatchingProvider) {
                System.out.println("test2");
                UserProvider newProvider = new UserProvider();
                newProvider.setId(UUID.randomUUID().toString());
                newProvider.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
                newProvider.setProviderId(oAuth2UserInfo.getId());
                newProvider.setUser(user);  // Liên kết với user hiện tại

                user.getProviders().add(newProvider);
                System.out.println("test3");
            }
//            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }
        System.out.println("userId: "+ UserPrincipal.create(user, oAuth2User.getAttributes()).getId());
        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

        private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
            User user = new User();

            user.setId(UUID.randomUUID().toString());
            user.setName(oAuth2UserInfo.getName());
            user.setEmail(oAuth2UserInfo.getEmail());
            user.setImageUrl(oAuth2UserInfo.getImageUrl());

            // Tạo provider đầu tiên cho người dùng mới
            UserProvider firstProvider = new UserProvider();
            firstProvider.setId(UUID.randomUUID().toString());
            firstProvider.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
            firstProvider.setProviderId(oAuth2UserInfo.getId());
            firstProvider.setUser(user);

            // Thêm provider vào danh sách của người dùng
            List<UserProvider> providers = new ArrayList<>();
            providers.add(firstProvider);
            user.setProviders(providers);

            Set<Role> roles = new HashSet<>();
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
            user.setRoles(roles);
            return userRepository.save(user);
        }
    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setName(oAuth2UserInfo.getName());
        existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userRepository.save(existingUser);
    }

}
