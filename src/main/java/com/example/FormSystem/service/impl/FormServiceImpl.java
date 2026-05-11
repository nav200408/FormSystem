package com.example.FormSystem.service.impl;

import com.example.FormSystem.constant.MessageConstant;
import com.example.FormSystem.dto.request.CreateFormRequest;
import com.example.FormSystem.dto.response.FormDtoResponse;
import com.example.FormSystem.dto.response.PageResponse;
import com.example.FormSystem.entity.Form;
import com.example.FormSystem.mapper.FormMapper;
import com.example.FormSystem.repository.FormRepository;

import com.example.FormSystem.service.FormService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FormServiceImpl implements FormService {

    @Autowired
    private FormRepository formRepository;

    public FormServiceImpl(FormRepository formRepository) {
        this.formRepository = formRepository;
    }

    @Override
    public PageResponse<FormDtoResponse> getForms(int page, int size) {
        int offset = (page - 1) * size;
        List<Form> forms = formRepository.findAllWithDeferredPagination(size + 1, offset);

        boolean hasNext = forms.size() > size;
        if (hasNext) {
            forms.remove(forms.size() - 1);
        }

        List<FormDtoResponse> content = forms.stream()
                .map(FormMapper::toResponse)
                .toList();

        return new PageResponse<>(content, page, size, hasNext);
    }

    @Override
    @Transactional
    public FormDtoResponse createForm(CreateFormRequest request) {
        Form form = FormMapper.toEntity(request);
        Form savedForm = formRepository.save(form);
        return FormMapper.toResponse(savedForm);
    }

    @Override
    public Form getFormById(Long id) {
        return formRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageConstant.FORM_NOT_FOUND + id));
    }
}

