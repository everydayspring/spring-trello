package com.sparta.springtrello.domain.workspace.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;

import com.sparta.springtrello.domain.workspace.entity.Workspace;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CreateWorkspaceDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private Long managerId;
        @NotBlank private String name;
        @NotBlank private String description;
    }

    @Getter
    public static class Response {
        private final Long id;
        private final Long managerId;
        private final String name;
        private final String description;
        private final LocalDateTime createdAt;
        private final LocalDateTime modifiedAt;

        public Response(Workspace workspace) {
            this.id = workspace.getId();
            this.managerId = workspace.getManagerId();
            this.name = workspace.getName();
            this.description = workspace.getDescription();
            this.createdAt = workspace.getCreatedAt();
            this.modifiedAt = workspace.getModifiedAt();
        }
    }
}
