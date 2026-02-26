package kh.com.kshrd.authentication.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import kh.com.kshrd.authentication.model.enums.Gender;
import kh.com.kshrd.authentication.model.validation.MinAge;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileRequest {

    @NotBlank
    @NotNull
    private String firstName;

    @NotBlank
    @NotNull
    private String lastName;


    @Pattern(
            regexp = "^(https?://).+",
            message = "Profile image must be a valid URL starting with http:// or https://"
    )
    private String profileImage;

    @NotNull
    private Gender gender;

    @Pattern(
            regexp = "^\\+?[0-9]{8,20}$",
            message = "Phone number must contain only digits and may start with + (8-20 digits)"
    )
    private String phone;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Date of birth must be in the past")
    @MinAge(value = 7, message = "You must be at least 7 years old")
    private LocalDate dob;
}