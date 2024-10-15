package com.sparta.springtrello.domain.workspace.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.sparta.springtrello.domain.user.enums.WorkspaceUserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AddUserDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotNull private Long workspaceId;
        @NotBlank @Email private String email;
        private WorkspaceUserRole role;
    }
}
