package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.AttachmentText;
import org.springframework.data.repository.CrudRepository;

public interface AttachmentTextRepository extends CrudRepository<AttachmentText,Integer>, AttachmentRepositoryCustomization {
}
