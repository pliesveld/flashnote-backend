package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.AbstractAttachment;
import com.pliesveld.flashnote.model.json.response.AttachmentHeader;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by happs on 3/6/16.
 */
public interface AttachmentRepository<A extends AbstractAttachment> extends CrudRepository<A, Integer> {

    @Query("select new com.pliesveld.flashnote.model.json.response.AttachmentHeader(a.attachmentType, a.fileLength, a.modifiedOn) from AbstractAttachment a where a.id = ?1")
    AttachmentHeader findAttachmentHeaderById(int id);
}
