package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.AttachmentText;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AttachmentTextRepository extends CrudRepository<AttachmentText,Integer>, AttachmentRepositoryCustomization {
    List<AttachmentText> findByCreatedByUser(String email);
}