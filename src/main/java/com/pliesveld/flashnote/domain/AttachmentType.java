package com.pliesveld.flashnote.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by happs on 1/14/16.
 */
public enum AttachmentType
{
    AUDIO   (1, "audio/wav",          ".wav"),
    IMAGE   (10, "image/jpg",         ".jpg"),
    DOC     (100, "application/pdf",  ".pdf");

    private int id;
    private String mime;
    private String extension;

    private final static Map<Integer,AttachmentType> intToEnum = new HashMap<>();

    static {
        for(AttachmentType type : values())
        {
            intToEnum.put(type.getId(),type);
        }
    }

    AttachmentType(int id,String mime, String extension) { this.id = id; this.mime = mime; this.extension = extension;}

    public String getMime() {
        return mime;
    }

    public String getExtension() {
        return extension;
    }

    public int getId() {
        return id;
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

    public static AttachmentType fileTypeOf(String filename)
    {
        for(AttachmentType type : values())
        {
            if(filename.endsWith(type.getExtension()))
                return type;
        }
        throw new IllegalArgumentException("Unsupported type: " + filename);
    }

}