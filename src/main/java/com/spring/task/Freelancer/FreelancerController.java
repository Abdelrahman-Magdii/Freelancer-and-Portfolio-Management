package com.spring.task.Freelancer;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class FreelancerController {

    private final FreelancerService freelancerService;

    @PostMapping("/register")
    public Freelancer registerFreelancer(@RequestBody Freelancer freelancer) {
        return freelancerService.registerFreelancer(freelancer);
    }
}
