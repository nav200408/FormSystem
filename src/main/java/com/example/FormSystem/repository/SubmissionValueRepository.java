package com.example.FormSystem.repository;

import com.example.FormSystem.entity.SubmissionValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionValueRepository extends JpaRepository<SubmissionValue, Long> {
}
