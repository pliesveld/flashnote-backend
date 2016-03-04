package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.dto.AttachmentHeader;

/**
 * Created by happs on 2/21/16.
 */
public interface AttachmentRepositoryCustomization {
    AttachmentHeader findAttachmentHeaderById(int id);
}
