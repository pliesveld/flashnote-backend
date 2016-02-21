package com.pliesveld.flashnote.domain;

import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public enum AttachmentType
{
    AUDIO   (1,  "audio/wav",          MediaType.APPLICATION_OCTET_STREAM, ".wav"),
    IMAGE   (10, "image/jpeg",         MediaType.IMAGE_JPEG,               ".jpg"),
    DOC     (100, "application/pdf",   MediaType.APPLICATION_OCTET_STREAM, ".pdf");


    private final int id;
    private final String mime;
    private final MediaType mediatype;
    private final String extension;

    private final static Map<Integer,AttachmentType> intToEnum = new HashMap<>();

    static {
        for(AttachmentType type : values())
        {
            intToEnum.put(type.getId(),type);
        }
    }

    AttachmentType(int id,String mime, MediaType mediatype, String extension) { this.id = id; this.mime = mime; this.mediatype = mediatype; this.extension = extension;}

    public String getMime() {
        return mime;
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

    public static AttachmentType fromInteger(Integer id)
    {
        return intToEnum.get(id);
    }

    public static boolean isFilenameSupported(String filename)
    {
        for(AttachmentType type : values())
        {
            if(filename.endsWith(type.getExtension()))
                return true;
        }
        return false;
    }

    public static AttachmentType fileTypeFromMime(String mime) throws IllegalArgumentException
    {
        for(AttachmentType type : values())
        {
            if(mime.equals(type.getMime()))
                return type;
        }
        throw new IllegalArgumentException("Unsupported type: " + mime);
    }

    public static AttachmentType fileTypeOfExtension(String filename) throws IllegalArgumentException
    {
        for(AttachmentType type : values())
        {
            if(filename.endsWith(type.getExtension()))
                return type;
        }
        throw new IllegalArgumentException("Unsupported type: " + filename);
    }
}