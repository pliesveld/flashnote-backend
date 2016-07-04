package com.pliesveld.flashnote.domain.converter;

import com.pliesveld.flashnote.domain.NotificationType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class NotificationTypeConverter implements AttributeConverter<NotificationType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(NotificationType attribute) {
        return attribute.getId();
    }

    @Override
    public NotificationType convertToEntityAttribute(Integer roleId) {
        return NotificationType.fromInteger(roleId);
    }
}
