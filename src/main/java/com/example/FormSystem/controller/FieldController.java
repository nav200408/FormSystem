package com.example.FormSystem.controller;

import com.example.FormSystem.constant.MessageConstant;
import com.example.FormSystem.dto.request.CreateFieldRequest;
import com.example.FormSystem.dto.request.UpdateFieldRequest;
import com.example.FormSystem.dto.response.ResponseData;
import com.example.FormSystem.entity.Field;
import com.example.FormSystem.service.FieldService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class FieldController {

    @Autowired
    private FieldService fieldService;

    public FieldController(FieldService fieldService) {
        this.fieldService = fieldService;
    }

    @PostMapping("/forms/{id}/fields")
    public ResponseEntity<ResponseData<Field>> addFieldToForm(
            @PathVariable Long id,
            @Valid @RequestBody CreateFieldRequest request) {

        Field field = fieldService.addFieldToForm(id, request);

        ResponseData<Field> res = new ResponseData<>(
                MessageConstant.CREATE_SUCCESS,
                field,
                HttpStatus.CREATED.value(),
                true);

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/forms/{id}/fields/{fid}")
    public ResponseEntity<ResponseData<Field>> updateField(
            @PathVariable Long id,
            @PathVariable Long fid,
            @Valid @RequestBody UpdateFieldRequest request) {

        Field field = fieldService.updateField(id, fid, request);

        ResponseData<Field> res = new ResponseData<>(
                MessageConstant.UPDATE_SUCCESS,
                field,
                HttpStatus.OK.value(),
                true);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping("/forms/{id}/fields/{fid}")
    public ResponseEntity<ResponseData<Void>> deleteField(
            @PathVariable Long id,
            @PathVariable Long fid) {

        fieldService.deleteField(id, fid);

        ResponseData<Void> res = new ResponseData<>(
                MessageConstant.DELETE_SUCCESS,
                null,
                HttpStatus.OK.value(),
                true);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
