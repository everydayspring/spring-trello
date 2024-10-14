package com.sparta.springtrello.domain.user.enums;

public enum WorkspaceUserRole {
    WORKSPACE("워크스페이스 관리자"),
    BOARD("보드 멤버"),
    READ_ONLY("읽기 전용");

    private final String description;

    WorkspaceUserRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
