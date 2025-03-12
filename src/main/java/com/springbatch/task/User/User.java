package com.springbatch.task.User;

import com.springbatch.task.Base.BaseUser;
import com.springbatch.task.Role.Role;
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
