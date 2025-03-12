package com.springbatch.task.Project;

import com.springbatch.task.Freelancer.Freelancer;
import com.springbatch.task.Freelancer.FreelancerRepo;
import com.springbatch.task.Role.Role;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProjectService {

    private final FreelancerRepo freelancerRepository;

    private final ProjectRepo projectRepository;


    public Project addProject(Project project, Long freelancerId) {
        Freelancer freelancer = freelancerRepository.findById(freelancerId)
                .orElseThrow(() -> new RuntimeException("Freelancer not found"));

        project.setFreelancer(freelancer);


        return projectRepository.save(project);
    }

    public List<Project> searchProjects(String query) {
        return projectRepository.searchProjects(query);
    }
}
