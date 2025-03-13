package com.spring.task.Project;

import com.spring.task.Freelancer.Freelancer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import java.util.List;

@Getter
@Setter
@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @ElementCollection
    @CollectionTable(name = "project_technologies", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "technology")
    private List<String> technologiesUsed;

    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private Freelancer freelancer;
}