package com.pliesveld.flashnote.exception;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * Created by happs on 1/20/16.
 */
public class FlashCardCreateException extends ResourceRepositoryException {
    final static private HttpStatus EXCEPTION_STATUS_IM_USED = HttpStatus.IM_USED;

    @Override
    public String getRepositoryMessage() {
        return "FlashCard exists: " + getRepositoryId();
    }

    @Override
    public HttpStatus getRepositoryStatus() {
        return EXCEPTION_STATUS_IM_USED;
    }
    
    public FlashCardCreateException(Serializable id) {
        super(id);
    }
}
