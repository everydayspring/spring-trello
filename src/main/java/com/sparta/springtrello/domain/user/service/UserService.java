package com.sparta.springtrello.domain.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.springtrello.domain.common.dto.AuthUser;
import com.sparta.springtrello.domain.common.exception.InvalidRequestException;
import com.sparta.springtrello.domain.user.dto.request.DeleteUserRequest;
import com.sparta.springtrello.domain.user.dto.request.UserChangePasswordRequest;
import com.sparta.springtrello.domain.user.dto.response.UserResponse;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse getUser(long userId) {
        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new InvalidRequestException("User not found"));
        return new UserResponse(user.getId(), user.getEmail());
    }

    @Transactional
    public void changePassword(long userId, UserChangePasswordRequest userChangePasswordRequest) {
        validateNewPassword(userChangePasswordRequest);

        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new InvalidRequestException("User not found"));

        if (passwordEncoder.matches(
                userChangePasswordRequest.getNewPassword(), user.getPassword())) {
            throw new InvalidRequestException("새 비밀번호는 기존 비밀번호와 같을 수 없습니다.");
        }

        if (!passwordEncoder.matches(
                userChangePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new InvalidRequestException("잘못된 비밀번호입니다.");
        }

        user.changePassword(passwordEncoder.encode(userChangePasswordRequest.getNewPassword()));
    }

    @Transactional
    public void deleteUser(AuthUser authUser, DeleteUserRequest deleteUserRequest) {
        User user =
                userRepository
                        .findById(authUser.getId())
                        .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        if (user.isDeleted()) {
            throw new IllegalArgumentException("이미 탈퇴한 유저입니다.");
        }

        if (!passwordEncoder.matches(deleteUserRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        user.changeIsDeleted();
    }

    private static void validateNewPassword(UserChangePasswordRequest userChangePasswordRequest) {
        if (userChangePasswordRequest.getNewPassword().length() < 8
                || !userChangePasswordRequest.getNewPassword().matches(".*[a-zA-Z].*")
                || // 대문자 또는 소문자 포함
                !userChangePasswordRequest.getNewPassword().matches(".*\\d.*")
                || // 숫자 포함
                !userChangePasswordRequest
                        .getNewPassword()
                        .matches(".*[!@#$%^&*(),.?\":{}|<>].*")) { // 특수문자 포함
            throw new InvalidRequestException(
                    "새 비밀번호는 8자 이상이어야 하고, 대소문자, 숫자, 특수문자를 각각 최소 1글자씩 포함해야 합니다.");
        }
    }
}
