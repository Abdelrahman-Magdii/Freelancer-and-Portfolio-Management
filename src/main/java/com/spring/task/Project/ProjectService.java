package com.spring.task.Project;

import com.spring.task.Freelancer.Freelancer;
import com.spring.task.Freelancer.FreelancerRepo;
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
        String processedQuery = query.trim().replaceAll("\\s+", " & ");
        return projectRepository.search(processedQuery);
    }
}
