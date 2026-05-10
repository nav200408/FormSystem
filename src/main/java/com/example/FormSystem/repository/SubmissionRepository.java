package com.example.FormSystem.repository;

import com.example.FormSystem.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByFormFormId(Long formId);
    List<Submission> findByUserId(Long userId);
}
