package cz.ivosahlik.ecommerce.controller;

import cz.ivosahlik.ecommerce.config.AppConstants;
import cz.ivosahlik.ecommerce.payload.AuthenticationResult;
import cz.ivosahlik.ecommerce.security.request.LoginRequest;
import cz.ivosahlik.ecommerce.security.request.SignupRequest;
import cz.ivosahlik.ecommerce.security.response.MessageResponse;
import cz.ivosahlik.ecommerce.service.AuthService;
import cz.ivosahlik.ecommerce.security.jwt.JwtUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        AuthenticationResult result = authService.login(loginRequest);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                result.getJwtCookie().toString())
                .body(result.getResponse());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return authService.register(signUpRequest);
    }

    @GetMapping("/username")
    public String currentUserName(Authentication authentication){
        if (authentication == null) {
            return "";
        }
        return authentication.getName();

    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(Authentication authentication){
        return ResponseEntity.ok().body(authService.getCurrentUserDetails(authentication));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signoutUser(){
        // Clear cookie for both "/api" and "/" paths to remove any legacy cookies
        ResponseCookie apiPathCookie = authService.logoutUser();
        ResponseCookie rootPathCookie = jwtUtils.getCleanJwtCookieRoot();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, apiPathCookie.toString(), rootPathCookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }

    @GetMapping("/sellers")
    public ResponseEntity<?> getAllSellers(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber) {

        Sort sortByAndOrder = Sort.by(AppConstants.SORT_USERS_BY).descending();
        Pageable pageDetails = PageRequest.of(pageNumber ,
                Integer.parseInt(AppConstants.PAGE_SIZE), sortByAndOrder);

        return ResponseEntity.ok(authService.getAllSellers(pageDetails));
    }

}
