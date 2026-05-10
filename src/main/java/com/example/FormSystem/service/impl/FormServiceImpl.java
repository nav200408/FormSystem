package com.example.FormSystem.service.impl;

import com.example.FormSystem.dto.response.PageResponse;
import com.example.FormSystem.entity.Form;
import com.example.FormSystem.repository.FormRepository;

import com.example.FormSystem.service.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormServiceImpl implements FormService {

    @Autowired
    private FormRepository formRepository;

    public FormServiceImpl(FormRepository formRepository) {
        this.formRepository = formRepository;
    }

    @Override
    public PageResponse<Form> getForms(int page, int size) {
        int offset = (page - 1) * size;
        List<Form> forms = formRepository.findAllWithDeferredPagination(size + 1, offset);

        boolean hasNext = forms.size() > size;
        if (hasNext) {
            forms.remove(forms.size() - 1);
        }

        return new PageResponse<>(forms, page, size, hasNext);
    }
}
