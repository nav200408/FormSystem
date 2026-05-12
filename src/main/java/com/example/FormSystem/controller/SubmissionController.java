package com.example.FormSystem.controller;

import com.example.FormSystem.constant.MessageConstant;
import com.example.FormSystem.dto.request.SubmissionRequest;
import com.example.FormSystem.dto.response.FormDtoResponse;
import com.example.FormSystem.dto.response.PageResponse;
import com.example.FormSystem.dto.response.ResponseData;
import com.example.FormSystem.entity.Submission;
import com.example.FormSystem.service.SubmissionService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @GetMapping("/submissions")
    public ResponseEntity<ResponseData<PageResponse<FormDtoResponse>>> getSubmissionsForCurrentUser(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResponse<FormDtoResponse> response = submissionService.getSubmissionsForCurrentUser(page, size);

        ResponseData<PageResponse<FormDtoResponse>> res = new ResponseData<>(
                MessageConstant.RETRIEVE_DATA_SUCCESS,
                response,
                HttpStatus.OK.value(),
                true);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/forms/{id}/submit")
    public ResponseEntity<ResponseData<Submission>> submitForm(
            @PathVariable Long id,
            @Valid @RequestBody SubmissionRequest request) {

        Submission submission = submissionService.submitForm(id, request);

        ResponseData<Submission> res = new ResponseData<>(
                MessageConstant.CREATE_SUCCESS,
                submission,
                HttpStatus.CREATED.value(),
                true);

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }
}
