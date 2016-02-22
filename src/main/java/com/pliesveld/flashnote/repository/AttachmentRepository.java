package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.Attachment;
import org.springframework.data.repository.CrudRepository;

public interface AttachmentRepository extends CrudRepository<Attachment,Integer>, AttachmentRepositoryCustomization {
}
