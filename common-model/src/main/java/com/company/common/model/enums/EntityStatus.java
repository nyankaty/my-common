package com.company.common.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum EntityStatus {
    INACTIVE(0),
    ACTIVE(1),
    UNKNOWN(null);

    private static final Map<Integer, EntityStatus> STATUS_MAP = new HashMap<>();

    static {
        for (EntityStatus status : EntityStatus.values()) {
            STATUS_MAP.put(status.value, status);
        }
    }

    @JsonValue
    private final Integer value;

    EntityStatus(Integer value) {
        this.value = value;
    }

    @JsonCreator
    public static EntityStatus fromValue(Integer integer) {
        if (integer == null) {
            return EntityStatus.UNKNOWN;
        }
        EntityStatus status = STATUS_MAP.get(integer);
        if (status == null) {
            return EntityStatus.UNKNOWN;
        }
        return status;
    }

    @Converter(autoApply = true)
    public static class EntityStatusConverter implements AttributeConverter<EntityStatus, Integer> {

        @Override
        public Integer convertToDatabaseColumn(EntityStatus status) {
            if (status == null) {
                return null;
            }
            return status.getValue();
        }

        @Override
        public EntityStatus convertToEntityAttribute(Integer value) {
            return EntityStatus.fromValue(value);
        }
    }
}
