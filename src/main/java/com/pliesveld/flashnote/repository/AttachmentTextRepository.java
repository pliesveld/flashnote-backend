package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.AttachmentText;
import com.pliesveld.flashnote.repository.base.AbstractAttachmentRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AttachmentTextRepository extends AbstractAttachmentRepository<AttachmentText>, CrudRepository<AttachmentText,Integer> {
}
