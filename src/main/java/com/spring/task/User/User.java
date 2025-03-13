package com.spring.task.User;

import com.spring.task.Base.BaseUser;
import com.spring.task.Role.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class User extends BaseUser {

    @Enumerated(EnumType.STRING)
    private Role role; // Example: "ADMIN", "FREELANCER"
}
