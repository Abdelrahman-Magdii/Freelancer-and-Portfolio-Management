package com.springbatch.task.Project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepo extends JpaRepository<Project, Long> {

    @Query(value = "SELECT DISTINCT p.* FROM project p " +
            "JOIN project_technologies pt ON p.id = pt.project_id " +
            "WHERE to_tsvector(p.title || ' ' || pt.technology) @@ to_tsquery(:query)", nativeQuery = true)
    List<Project> searchProjects(@Param("query") String query);
}
