package com.example.FormSystem.controller;

import com.example.FormSystem.constant.MessageConstant;
import com.example.FormSystem.dto.response.PageResponse;
import com.example.FormSystem.dto.response.ResponseData;
import com.example.FormSystem.entity.Form;
import com.example.FormSystem.service.FormService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/forms")
public class FormController {

    @Autowired
    private FormService formService;

    public FormController(FormService formService) {
        this.formService = formService;
    }

    @GetMapping
    public ResponseEntity<ResponseData<PageResponse<Form>>> getAllForms(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (page < 1) {
            page = 1;
        }
        if (size < 1) {
            size = 1;
        }
        PageResponse<Form> response = formService.getForms(page, size);

        ResponseData<PageResponse<Form>> res = new ResponseData<PageResponse<Form>>(
                MessageConstant.RETRIEVE_DATA_SUCCESS,
                response,
                HttpStatus.OK.value(),
                true);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
