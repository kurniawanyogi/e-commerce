package com.eccomerce.auth_service.service;

import com.eccomerce.auth_service.entity.Role;
import com.eccomerce.auth_service.exception.MainException;
import com.eccomerce.auth_service.model.*;
import com.eccomerce.auth_service.repository.RoleRepository;
import com.eccomerce.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eccomerce.auth_service.entity.User;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User registrationUser(RegistrationRequest request, String username) {
        Optional<User> existingUser = userRepository.findByUsername(request.getUsername());
        if (existingUser.isPresent()) throw new MainException("400-VALIDATION", "username sudah terpakai");

        Role role = validateRole(request.getRoleId());

        User user = new User();
        user.setId(String.valueOf(new Date().getTime()));
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        if (request.getStatus() != null) {
            user.setStatus(StatusEnum.fromAbbreviation(request.getStatus()).getAbbreviation());
        } else {
            user.setStatus(StatusEnum.ACTIVE.getAbbreviation());
        }
        user.setCreatedBy(username);
        user.setLastUpdatedBy(username);
        user.setCreatedDate(new Timestamp(new Date().getTime()));
        user.setLastUpdatedDate(new Timestamp(new Date().getTime()));

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(UpdateUserRequest request, String userId, String username) {

        User existingUser = validateUser(userId);
        if (request.getRoleId() != null) {
            Role role = validateRole(request.getRoleId());

            Set<Role> updatedRoles = new HashSet<>();
            updatedRoles.add(role);
            existingUser.setRoles(updatedRoles);
        }

        if (request.getStatus() != null)
            existingUser.setStatus(StatusEnum.fromAbbreviation(request.getStatus()).getAbbreviation());

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            String regex = "^(?=.*[0-9])(?=.*[a-z])([a-z0-9$!%*?&_-]+)$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(request.getPassword());
            if (!matcher.matches()) throw new MainException("400-VALIDATION", "password tidak valid");

            if (!request.getPassword().equals(request.getConfirmPassword()))
                throw new MainException("400-VALIDATION", "password dan confirm password tidak sama");

            existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        existingUser.setFirstName(request.getFirstName());
        existingUser.setLastName(request.getLastName());
        existingUser.setLastUpdatedBy(username);
        existingUser.setLastUpdatedDate(new Timestamp(new Date().getTime()));

        Set<Role> roles = existingUser.getRoles().stream()
                .filter(existingRole -> existingRole.getRoleName().equals("PRODUCTION"))
                .collect(Collectors.toSet());

        return userRepository.save(existingUser);
    }

    @Override
    public User updateCurrentUser(UpdateCurrentUserRequest request, String userId, String username) {
        User existingUser = validateUser(userId);

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            String regex = "^(?=.*[0-9])(?=.*[a-z])([a-z0-9$!%*?&_-]+)$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(request.getPassword());
            if (!matcher.matches()) throw new MainException("400-VALIDATION", "password tidak valid");

            if (!request.getPassword().equals(request.getConfirmPassword()))
                throw new MainException("400-VALIDATION", "password dan confirm password tidak sama");

            existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        existingUser.setFirstName(request.getFirstName());
        existingUser.setLastName(request.getLastName());
        existingUser.setLastUpdatedBy(username);
        existingUser.setLastUpdatedDate(new Timestamp(new Date().getTime()));
        return userRepository.save(existingUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User changePassword(ChangePasswordRequest request, String userId, String username) {
        User user = validateUser(userId);

        if (!passwordEncoder.matches(request.getExistingPassword(), user.getPassword()))
            throw new MainException("400-VALIDATION", "existingPassword is incorrect");

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setLastUpdatedBy(username);
        user.setLastUpdatedDate(new Timestamp(new Date().getTime()));
        return userRepository.save(user);
    }

    @Override
    public User getDetail(String id) {
        return validateUser(id);
    }

    @Override
    public Page<User> getList(Integer index, Integer size, String username, String firstName) {
        Pageable pageable = PageRequest.of(index, size);
        return userRepository.getListUser(username, firstName, pageable);
    }

    private User validateUser(String userId) {
        Optional<User> existingUser = userRepository.findById(userId);
        if (!existingUser.isPresent()) throw new MainException("400-DATA-NOT-FOUND", "user tidak ditemukan");
        return existingUser.get();
    }

    private Role validateRole(Integer roleId) {
        Optional<Role> role = roleRepository.findById(roleId);
        if (!role.isPresent()) throw new MainException("400-DATA-NOT-FOUND", "role tidak ditemukan");
        return role.get();
    }
}