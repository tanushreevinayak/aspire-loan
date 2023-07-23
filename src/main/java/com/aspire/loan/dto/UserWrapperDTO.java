package com.aspire.loan.dto;

import com.sun.istack.Nullable;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserWrapperDTO {

    @Nullable
    private String name;

    @Nullable
    private String password;

}
