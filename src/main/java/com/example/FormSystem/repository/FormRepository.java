package com.example.FormSystem.repository;

import com.example.FormSystem.entity.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.FormSystem.enums.FormStatus;

import java.util.List;

@Repository
public interface FormRepository extends JpaRepository<Form, Long> {

    @Query(value = "SELECT f.* FROM forms f " +
            "INNER JOIN (" +
            "   SELECT form_id FROM forms ORDER BY form_order ASC, form_id ASC LIMIT :limit OFFSET :offset" +
            ") as tmp ON f.form_id = tmp.form_id " +
            "ORDER BY f.form_order ASC, f.form_id ASC", nativeQuery = true)
    List<Form> findAllWithDeferredPagination(@Param("limit") int limit, @Param("offset") int offset);

    List<Form> findByStatus(FormStatus status);
}
