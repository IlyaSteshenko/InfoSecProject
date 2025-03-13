package com.informationsecurity.PasteBinService.models;

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

    public boolean saveUser(UserEntity user, boolean asAdmin) {
        UserEntity customUser = userEntityRepository.findUserByUsername(user.getUsername());
        if (customUser != null) return false;
        Role role = asAdmin ? Role.ADMIN : Role.USER;

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(role);
        user.setAccountLocked(false);
        userEntityRepository.save(user);
        return true;
    }

    public void saveAsUser(UserEntity user) {
        userEntityRepository.save(user);
    }

    public UserEntity findById(Long id) {
        return userEntityRepository.findUserById(id);
    }

    public boolean deleteById(Long id) {
        if (userEntityRepository.findById(id).isPresent()) {
            userEntityRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
