package com.pliesveld.flashnote.domain;

public enum AttachmentCategory {
    UNSUPPORTED, AUDIO(AttachmentTypeDataFormat.BINARY), IMAGE(AttachmentTypeDataFormat.BINARY), DOCUMENT(AttachmentTypeDataFormat.TEXT);

    private final AttachmentTypeDataFormat dataFormat;
    AttachmentCategory() {
        dataFormat = null;
    }
    AttachmentCategory(AttachmentTypeDataFormat dataFormat) {
        this.dataFormat = dataFormat;
    }

    public AttachmentTypeDataFormat getDataFormat() { return dataFormat; }
}
