package com.example.FormSystem.repository;

import com.example.FormSystem.entity.Field;
import com.example.FormSystem.entity.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldRepository extends JpaRepository<Field, Long> {
    List<Field> findByFormAndFieldOrderGreaterThanEqual(Form form, Integer fieldOrder);
    boolean existsByFormAndFieldOrder(Form form, Integer fieldOrder);
}
