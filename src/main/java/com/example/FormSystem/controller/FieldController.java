package com.example.FormSystem.controller;

import com.example.FormSystem.constant.MessageConstant;
import com.example.FormSystem.dto.request.CreateFieldRequest;
import com.example.FormSystem.dto.response.ResponseData;
import com.example.FormSystem.entity.Field;
import com.example.FormSystem.service.FieldService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/forms")
public class FieldController {

    private final FieldService fieldService;

    public FieldController(FieldService fieldService) {
        this.fieldService = fieldService;
    }

    @PostMapping("/{id}/fields")
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

    @PutMapping("/{id}/fields/{fid}")
    public ResponseEntity<ResponseData<Field>> updateField(
            @PathVariable Long id,
            @PathVariable Long fid,
            @jakarta.validation.Valid @RequestBody com.example.FormSystem.dto.request.UpdateFieldRequest request) {

        Field field = fieldService.updateField(id, fid, request);

        ResponseData<Field> res = new ResponseData<>(
                MessageConstant.UPDATE_SUCCESS,
                field,
                HttpStatus.OK.value(),
                true);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
