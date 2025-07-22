package ps.demo.jpademo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;



@Data
public class GeekEmployee {

    @NotNull(message = "Enter a valid Employee Id")
    private Long Emp_id;

    @NotEmpty(message = "Must not be Empty and NULL")
    private String phoneNumber;

    @NotBlank(message = "Employee name can't be left empty")
    private String geekEmployeeName;

    @Min(value = 18, message = "Minimum working age 18")
    @Max(value = 60, message = "Maximum working age 60")
    private Integer age;

    @Email(message = "Please enter a valid email Id")
    @NotNull(message = "Email cannot be NULL")
    private String geekEmailId;

    @Pattern(regexp = "^[0-9]{5}$", message = "Employee postal code must be a 5-digit number.")
    private String employeePostalCode;

    @Size(
            min = 10, max = 100,
            message = "Address should have a length between 10 and 100 characters.")
    private String employeeAddress;
}
