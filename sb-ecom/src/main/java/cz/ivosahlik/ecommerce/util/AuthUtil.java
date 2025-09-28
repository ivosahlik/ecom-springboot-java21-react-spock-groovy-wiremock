package cz.ivosahlik.ecommerce.util;

import cz.ivosahlik.ecommerce.model.User;
import cz.ivosahlik.ecommerce.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuthUtil {

    public static final String USER_NOT_FOUND_WITH_USERNAME = "User Not Found with username: ";
    private final UserRepository userRepository;

    public String loggedInEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_WITH_USERNAME + authentication.getName()));

        return user.getEmail();
    }

    public Long loggedInUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_WITH_USERNAME + authentication.getName()));

        return user.getUserId();
    }

    public User loggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return userRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_WITH_USERNAME + authentication.getName()));

    }


}
