package com.spring.task.Project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepo extends JpaRepository<Project, Long> {

    @Query(value = "SELECT DISTINCT p.* FROM project p " +
            "JOIN project_technologies pt ON p.id = pt.project_id " +
            "WHERE to_tsvector('english', p.title || ' ' || (" +
            "    SELECT string_agg(pt2.technology, ' ') " +
            "    FROM project_technologies pt2 " +
            "    WHERE pt2.project_id = p.id" +
            ")) @@ to_tsquery(:query)", nativeQuery = true)
    List<Project> search(@Param("query") String query);

}
