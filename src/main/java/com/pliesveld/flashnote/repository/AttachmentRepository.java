package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.AbstractAttachment;
import com.pliesveld.flashnote.model.json.response.AttachmentHeader;
import com.pliesveld.flashnote.repository.base.AbstractAttachmentRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by happs on 3/6/16.
 */
public interface AttachmentRepository extends AbstractAttachmentRepository<AbstractAttachment> {

    @Query("select new com.pliesveld.flashnote.model.json.response.AttachmentHeader(a.attachmentType, a.fileLength, a.modifiedOn) from AbstractAttachment a where a.id = ?1")
    AttachmentHeader findAttachmentHeaderById(int id);

}
