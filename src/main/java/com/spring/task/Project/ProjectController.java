package com.spring.task.Project;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/portfolio")
@AllArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectRepo projectRepo;

    @PostMapping("/add")
    public Project addProject(@RequestBody Project project, @RequestParam Long freelancerId) {
        return projectService.addProject(project, freelancerId);
    }

    @GetMapping("/search")
    public List<Project> searchProjects(@RequestParam String query) {
        return projectService.searchProjects(query);
    }

    @GetMapping("")
    public List<Project> searchProjects() {
        return projectRepo.findAll();
    }

}
