package com.sparta.springtrello.domain.workspace.entity;

import jakarta.persistence.*;

import com.sparta.springtrello.domain.common.entity.Timestamped;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "workspace")
public class Workspace extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long managerId;
    private String name;
    private String description;

    public Workspace(Long managerId, String name, String description) {
        this.managerId = managerId;
        this.name = name;
        this.description = description;
    }

    public void changeManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public void updateWorkspace(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
