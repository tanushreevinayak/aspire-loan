package com.aspire.loan.dto;

import com.sun.istack.Nullable;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Builder
@Data
public class UserRequestDTO {

    @NotNull(message = "The password is required.")
    private String password;
    @Nullable
    private UserDTO userDTO;
}
