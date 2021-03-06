package com.pliesveld.flashnote.domain.converter;

import com.pliesveld.flashnote.domain.AttachmentType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class AttachmentTypeConverter implements AttributeConverter<AttachmentType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(AttachmentType attribute) {
        return attribute.getId();
    }

    @Override
    public AttachmentType convertToEntityAttribute(Integer dbData) {
        return AttachmentType.fromInteger(dbData);
    }
}
