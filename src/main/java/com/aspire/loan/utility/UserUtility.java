package com.aspire.loan.utility;

import com.aspire.loan.dto.UserDTO;
import com.aspire.loan.enums.UserType;
import com.aspire.loan.exceptions.UserNotAuthenticatedException;
import com.aspire.loan.exceptions.UserNotAuthorizedException;
import com.aspire.loan.model.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserUtility {

    public boolean isUserAuthentic(User user, String password){
        if(!LoanUtility.isAuthenticUser(password, user.getPassword())){
            throw new UserNotAuthenticatedException("User with id: "+user.getId()+" is not authentic. Please recheck userId and/or password");
        }
        return true;
    }

    public boolean isUserAuthorized(User user, String password, Long loanId){
        isUserAuthentic(user, password);

        boolean isUserAuthorized;

        if(user.getType().equals(UserType.ADMIN))
            isUserAuthorized=true;
        else {
            if(user.getLoans()==null)
                isUserAuthorized=false;
            else
                isUserAuthorized = user.getLoans().stream()
                    .anyMatch(l -> l.getId() == loanId);
        }

        if(!isUserAuthorized)
            throw new UserNotAuthorizedException("User with id: "+ user.getId()+" is not authorized to view loan with id: "+loanId);

        return isUserAuthorized;
    }

    public UserDTO toUserDTO(User user){
        return UserDTO.builder()
                .id(user.getId())
                .created_at_timestamp(user.getCreated_at_ts())
                .name(user.getName())
                .type(user.getType().toString())
                .build();
    }

    public User toUser(UserDTO userDTO){
        String type = userDTO.getType();
        if(type==null)
            type=UserType.CUSTOMER.toString();

        return User.builder()
                .id(userDTO.getId())
                .created_at_ts(userDTO.getCreated_at_timestamp())
                .password(userDTO.getPassword())
                .name(userDTO.getName())
                .type(UserType.valueOf(type))
                .build();
    }
}
