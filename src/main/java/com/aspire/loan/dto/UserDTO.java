package com.aspire.loan.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDTO {

    private long id;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String type;
    private java.util.Date created_at_timestamp;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;
}
