package com.eccomerce.auth_service.model;

import com.eccomerce.auth_service.exception.MainException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum StatusEnum {
    ACTIVE('A'),
    INACTIVE('I');

    private final Character abbreviation;


    public static StatusEnum fromAbbreviation(Character abbreviation) {
        if (abbreviation.equals('A')) {
            return ACTIVE;
        } else if (abbreviation.equals('I')) {
            return INACTIVE;
        } else {
            throw new MainException("400-VALIDATION", "Status " + abbreviation + " not found");
        }
    }
}
