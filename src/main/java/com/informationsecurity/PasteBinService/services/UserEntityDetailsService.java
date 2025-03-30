package com.informationsecurity.PasteBinService.services;

import com.informationsecurity.PasteBinService.models.Role;
import com.informationsecurity.PasteBinService.models.UserEntity;
import com.informationsecurity.PasteBinService.repositories.UserEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserEntityDetailsService implements UserDetailsService {

    private UserEntityRepository userEntityRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userEntityRepository.findUserByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }

        return userEntity;
    }

    public List<UserEntity> allUsers() {
        return userEntityRepository.findAll();
    }

    public List<UserEntity> findAllWithUserAuthorities() {
        return userEntityRepository.findAllWithUserAuthorities();
    }

    public boolean saveUser(UserEntity user, boolean asAdmin) {
        UserEntity customUser = userEntityRepository.findUserByUsername(user.getUsername());
        if (customUser != null) return false;
        Role role = asAdmin ? Role.ADMIN : Role.USER;

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(role);
        user.setAccountLocked(false);
        user.setDescription(user.getDescription());
        userEntityRepository.save(user);
        return true;
    }

    public void saveAsUser(UserEntity user) {
        userEntityRepository.save(user);
    }

    public UserEntity findById(Long id) {
        return userEntityRepository.findUserById(id);
    }

    public UserEntity findByName(String name) {
        return userEntityRepository.findUserByUsername(name);
    }

    public boolean deleteById(Long id) {
        if (userEntityRepository.findById(id).isPresent()) {
            userEntityRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public UserEntity findUserByEmail(String email) {
        return userEntityRepository.findUserByEmail(email);
    }
}
