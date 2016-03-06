package com.pliesveld.flashnote.domain;

import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public enum AttachmentType
{
    AUDIO       (1,  "audio/wav",                   MediaType.APPLICATION_OCTET_STREAM, ".wav", AttachmentTypeDataFormat.BINARY),
    IMAGE       (10, "image/jpeg",                  MediaType.IMAGE_JPEG,               ".jpg", AttachmentTypeDataFormat.BINARY),
    TEXT        (100, MediaType.TEXT_PLAIN_VALUE,   MediaType.TEXT_PLAIN,               ".txt", AttachmentTypeDataFormat.TEXT);

    private final int id;
    private final String mime;
    private final MediaType mediatype;
    private final String extension;
    private final AttachmentTypeDataFormat dataFormat;

    private final static Map<Integer,AttachmentType> intToEnum = new HashMap<>();

    static {
        for(AttachmentType type : values())
        {
            intToEnum.put(type.getId(),type);
        }
    }

    AttachmentType(int id, String mime, MediaType mediatype, String extension, AttachmentTypeDataFormat dataFormat) { this.id = id; this.mime = mime; this.mediatype = mediatype; this.extension = extension; this.dataFormat = dataFormat;}

    public String getMime() { return this.mime; };

    public String getExtension() {
        return extension;
    }

    public int getId() {
        return id;
    }

    public MediaType getMediatype() {
        return mediatype;
    }

    public AttachmentTypeDataFormat getDataFormat() {
        return dataFormat;
    }

    public boolean isBinary()
    {
        return dataFormat == AttachmentTypeDataFormat.BINARY;
    }

    public static AttachmentType fromInteger(Integer id)
    {
        return intToEnum.get(id);
    }

    public boolean supportsMimeType(String type)
    {
        return mime.equalsIgnoreCase(type);
    }

    public boolean supportsFilenameBySuffix(String filename)
    {
        return StringUtils.endsWithIgnoreCase(filename, this.getExtension());
    }

    public static AttachmentType valueOfMime(String mime) throws IllegalArgumentException
    {
        for(AttachmentType type : values())
        {
            if(type.supportsMimeType(mime))
                return type;
        }
        return null;
    }

    public static AttachmentType valueOfFileSuffix(String filename) throws IllegalArgumentException
    {
        for(AttachmentType type : values())
        {
            if(type.supportsFilenameBySuffix(filename))
                return type;
        }
        throw new IllegalArgumentException("Unsupported type: " + filename);
    }
}