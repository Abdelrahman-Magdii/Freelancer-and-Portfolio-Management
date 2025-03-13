package com.spring.task.Freelancer;

import com.spring.task.Role.Role;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FreelancerService {

    private final FreelancerRepo freelancerRepository;

    public Freelancer registerFreelancer(Freelancer freelancer) {
        freelancer.setRole(Role.FREELANCER);
        return freelancerRepository.save(freelancer);
    }
}
