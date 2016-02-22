package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.AttachmentHeader;
import com.pliesveld.flashnote.exception.AttachmentNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Created by happs on 2/21/16.
 */

public class AttachmentRepositoryImpl implements AttachmentRepositoryCustomization {
    @PersistenceContext
    EntityManager em;

    @Override
    @Transactional
    public AttachmentHeader findAttachmentHeaderById(int id) throws AttachmentNotFoundException {
        TypedQuery<AttachmentHeader> query = em.createNamedQuery("Attachment.findHeaderByAttachmentId",AttachmentHeader.class);
        query.setParameter("id",id);
        AttachmentHeader header = query.getSingleResult();
        return header;
    }
}
