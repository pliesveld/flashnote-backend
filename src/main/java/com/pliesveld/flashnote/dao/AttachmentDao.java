package com.pliesveld.flashnote.dao;

import com.pliesveld.flashnote.domain.Attachment;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class AttachmentDao extends GenericDao<Attachment> {
    public AttachmentDao() {
        setPersistentClass(Attachment.class);
    }
}

