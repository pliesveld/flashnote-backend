package com.pliesveld.flashnote.domain.converter;

import com.pliesveld.flashnote.domain.StudentRole;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class StudentRoleConverter implements AttributeConverter<StudentRole,Integer>
{
    @Override
    public Integer convertToDatabaseColumn(StudentRole attribute) {
        return attribute.getId();
    }

    @Override
    public StudentRole convertToEntityAttribute(Integer roleId) {
        return StudentRole.fromInteger(roleId);
    }
}
