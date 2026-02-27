package kh.com.kshrd.authentication.model.enums;

import lombok.Getter;

@Getter
public enum SchoolLevel {

    PRE_PRIMARY("Pre Primary School"),
    PRIMARY("Primary School"),
    LOWER_SECONDARY("Lower Secondary School"),
    UPPER_SECONDARY("Upper Secondary School"),
    VOCATIONAL("Vocational School"),
    COLLEGE("College"),
    UNIVERSITY("University"),
    OTHER("Other");

    private final String type;

    SchoolLevel(String type) {
        this.type = type;
    }

    public static SchoolLevel fromValue(String type) {
        for (SchoolLevel gradedSchoolType : SchoolLevel.values()) {
            if (gradedSchoolType.type.equalsIgnoreCase(type)) {
                return gradedSchoolType;
            }
        }
        return OTHER;
    }

}