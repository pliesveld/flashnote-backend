package com.pliesveld.flashnote.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class StudentTypeConverter implements AttributeConverter<StudentType,Integer>
{
    @Override
    public Integer convertToDatabaseColumn(StudentType attribute) {
        return attribute.getId();
    }

    @Override
    public StudentType convertToEntityAttribute(Integer roleId) {
        return StudentType.fromInteger(roleId);
    }
}
