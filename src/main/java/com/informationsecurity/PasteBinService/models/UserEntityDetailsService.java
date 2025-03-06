package com.informationsecurity.PasteBinService.models;

import com.informationsecurity.PasteBinService.repositories.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserEntityDetailsService implements UserDetailsService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userEntityRepository.findUserByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }

        return new User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                Collections.singleton(userEntity.getRole())
        );
    }

    public List<UserEntity> allUsers() {
        return userEntityRepository.findAll();
    }

    public boolean saveUser(UserEntity user, boolean asAdmin) {
        UserEntity customUser = userEntityRepository.findUserByUsername(user.getUsername());
        if (customUser != null) return false;
        Role role = asAdmin ? Role.ADMIN : Role.USER;

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(role);
        userEntityRepository.save(user);
        return true;
    }

    public Optional<UserEntity> findById(Long id) {
        return userEntityRepository.findById(id);
    }

    public void deleteById(Long id) {
        if (userEntityRepository.findById(id).isPresent()) {
            userEntityRepository.deleteById(id);
        }
    }
}
