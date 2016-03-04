package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.AttachmentBinary;
import org.springframework.data.repository.CrudRepository;

public interface AttachmentBinaryRepository extends CrudRepository<AttachmentBinary,Integer>, AttachmentRepositoryCustomization {
}
