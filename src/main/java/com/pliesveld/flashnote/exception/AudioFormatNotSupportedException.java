package com.pliesveld.flashnote.exception;

public class AudioFormatNotSupportedException extends Exception {
    private static final long serialVersionUID = 7043457815601134034L;

    public AudioFormatNotSupportedException() {
    }

    public AudioFormatNotSupportedException(String message) {
        super(message);
    }

    public AudioFormatNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AudioFormatNotSupportedException(Throwable cause) {
        super(cause);
    }

    protected AudioFormatNotSupportedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
