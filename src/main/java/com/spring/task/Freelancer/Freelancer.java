package com.spring.task.Freelancer;

import com.spring.task.User.User;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Freelancer extends User {

    private String specialization; // Example: "Web Development", "Graphic Design"
}
