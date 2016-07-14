package com.pliesveld.flashnote.domain;

import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.*;

public enum AttachmentType {
    BINARY(AttachmentCategory.UNSUPPORTED, 1, APPLICATION_OCTET_STREAM_VALUE, APPLICATION_OCTET_STREAM, ".dat"),
    AUDIO_WAV(AttachmentCategory.AUDIO, 10, "audio/wav", APPLICATION_OCTET_STREAM, ".wav"),
    AUDIO_MP3(AttachmentCategory.AUDIO, 11, "audio/mp3", APPLICATION_OCTET_STREAM, ".mp3"),
    IMAGE_JPG(AttachmentCategory.IMAGE, 50, "image/jpeg", IMAGE_JPEG, ".jpg"),
    DOCUMENT_TEXT(AttachmentCategory.DOCUMENT, 100, TEXT_PLAIN_VALUE, TEXT_PLAIN, ".txt");

    private final AttachmentCategory category;
    private final int id;
    private final String mime;
    private final MediaType mediatype;
    private final String extension;

    private final static Map<Integer, AttachmentType> intToEnum = new HashMap<>();

    static {
        for (AttachmentType type : values()) {
            intToEnum.put(type.getId(), type);
        }
    }

    AttachmentType(final AttachmentCategory category, final int id, final String mime, final MediaType mediatype,
                   final String extension) {
        this.id = id;
        this.category = category;
        this.mime = mime;
        this.mediatype = mediatype;
        this.extension = extension;
    }

    public AttachmentCategory getCategory() { return category; }

    public String getMime() {
        return this.mime;
    }

    public String getExtension() {
        return extension;
    }

    public int getId() {
        return id;
    }

    public MediaType getMediatype() {
        return mediatype;
    }

    public boolean isBinary() {
        return getCategory().getDataFormat() == AttachmentTypeDataFormat.BINARY;
    }

    public static AttachmentType fromInteger(final Integer id) {
        return intToEnum.get(id);
    }

    public boolean supportsMimeType(final String type) {
        return mime.equalsIgnoreCase(type);
    }

    public boolean supportsFilenameBySuffix(final String filename) {
        return StringUtils.endsWithIgnoreCase(filename, this.getExtension());
    }

    public static AttachmentType valueOfMime(final String mime) throws IllegalArgumentException {
        for (AttachmentType type : values()) {
            if (type.supportsMimeType(mime))
                return type;
        }
        return null;
    }

    public static AttachmentType valueOfFileSuffix(final String filename) throws IllegalArgumentException {
        for (AttachmentType type : values()) {
            if (type.supportsFilenameBySuffix(filename))
                return type;
        }
        throw new IllegalArgumentException("Unsupported type: " + filename);
    }
}
