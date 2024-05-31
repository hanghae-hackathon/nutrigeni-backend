package org.nutrigenie.technopark.auth;

import lombok.RequiredArgsConstructor;
import org.nutrigenie.technopark.model.User;
import org.nutrigenie.technopark.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);

        if(user != null){
            return createUserdetails(user);
        }else{
            throw new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다.");
        }
    }

    private UserDetails createUserdetails(User user){
        Collection<? extends GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(user.getRole()));

        return new CustomUserDetail(
                user.getEmail(),
                user.getPassword(),
                authorities,
                user.getId(),
                user.getRole()
        );

    }

}
