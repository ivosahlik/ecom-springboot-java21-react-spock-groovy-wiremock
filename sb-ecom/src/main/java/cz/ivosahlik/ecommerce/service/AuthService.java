package cz.ivosahlik.ecommerce.service;

import cz.ivosahlik.ecommerce.payload.AuthenticationResult;
import cz.ivosahlik.ecommerce.payload.UserResponse;
import cz.ivosahlik.ecommerce.security.request.LoginRequest;
import cz.ivosahlik.ecommerce.security.request.SignupRequest;
import cz.ivosahlik.ecommerce.security.response.MessageResponse;
import cz.ivosahlik.ecommerce.security.response.UserInfoResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface AuthService {

    AuthenticationResult login(LoginRequest loginRequest);

    ResponseEntity<MessageResponse> register(SignupRequest signUpRequest);

    UserInfoResponse getCurrentUserDetails(Authentication authentication);

    ResponseCookie logoutUser();

    UserResponse getAllSellers(Pageable pageable);
}
