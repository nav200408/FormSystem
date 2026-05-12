package com.example.FormSystem.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "submissions")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long submissionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", nullable = false)
    @org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    @JsonIgnore
    private Form form;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    private LocalDateTime submittedAt;

    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubmissionValue> values;

    public Submission() {
        this.submittedAt = LocalDateTime.now();
    }

    public Long getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Long submissionId) {
        this.submissionId = submissionId;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public List<SubmissionValue> getValues() {
        return values;
    }

    public void setValues(List<SubmissionValue> values) {
        this.values = values;
    }
}
