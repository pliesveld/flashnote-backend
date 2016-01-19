package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment,Integer> {
}
