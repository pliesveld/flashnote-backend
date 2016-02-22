package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.AttachmentHeader;

/**
 * Created by happs on 2/21/16.
 */
public interface AttachmentRepositoryCustomization {
    public AttachmentHeader findAttachmentHeaderById(int id);
}
