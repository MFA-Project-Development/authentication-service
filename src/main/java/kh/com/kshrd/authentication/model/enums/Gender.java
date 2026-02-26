package kh.com.kshrd.authentication.model.enums;

import lombok.Getter;

@Getter
public enum Gender {

    MALE("male"),
    FEMALE("female"),
    OTHER("other");

    private final String value;

    Gender(String value) {
        this.value = value;
    }

    public static Gender fromValue(String value) {
        for (Gender gender : Gender.values()) {
            if (gender.value.equalsIgnoreCase(value)) {
                return gender;
            }
        }
        return OTHER;
    }
}
