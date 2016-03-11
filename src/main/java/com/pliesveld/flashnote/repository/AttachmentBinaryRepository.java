package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.AttachmentBinary;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AttachmentBinaryRepository extends CrudRepository<AttachmentBinary,Integer> {
    List<AttachmentBinary> findByCreatedByUser(String student_email);
}
