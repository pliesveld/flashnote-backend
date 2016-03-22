package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.AttachmentBinary;
import com.pliesveld.flashnote.repository.base.AbstractAttachmentRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AttachmentBinaryRepository extends AbstractAttachmentRepository<AttachmentBinary>, CrudRepository<AttachmentBinary,Integer> {
}
