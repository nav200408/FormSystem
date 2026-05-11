package com.example.FormSystem.controller;

import com.example.FormSystem.constant.MessageConstant;
import com.example.FormSystem.dto.request.CreateFormRequest;
import com.example.FormSystem.dto.response.FormDtoResponse;
import com.example.FormSystem.dto.response.PageResponse;
import com.example.FormSystem.dto.response.ResponseData;
import com.example.FormSystem.entity.Form;
import com.example.FormSystem.service.FormService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/forms")
public class FormController {

    @Autowired
    private FormService formService;

    public FormController(FormService formService) {
        this.formService = formService;
    }

    @GetMapping
    public ResponseEntity<ResponseData<PageResponse<FormDtoResponse>>> getAllForms(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (page < 1) {
            page = 1;
        }
        if (size < 1) {
            size = 1;
        }
        PageResponse<FormDtoResponse> response = formService.getForms(page, size);

        ResponseData<PageResponse<FormDtoResponse>> res = new ResponseData<>(
                MessageConstant.RETRIEVE_DATA_SUCCESS,
                response,
                HttpStatus.OK.value(),
                true);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseData<FormDtoResponse>> createForm(@Valid @RequestBody CreateFormRequest request) {
        FormDtoResponse form = formService.createForm(request);

        ResponseData<FormDtoResponse> res = new ResponseData<>(
                MessageConstant.CREATE_SUCCESS,
                form,
                HttpStatus.CREATED.value(),
                true);

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<Form>> getFormById(@PathVariable Long id) {
        Form form = formService.getFormById(id);

        ResponseData<Form> res = new ResponseData<>(
                MessageConstant.RETRIEVE_DATA_SUCCESS,
                form,
                HttpStatus.OK.value(),
                true);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
