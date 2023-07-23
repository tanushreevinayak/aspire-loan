package com.aspire.loan.services;

import com.aspire.loan.dto.UserDTO;
import com.aspire.loan.enums.UserType;
import com.aspire.loan.exceptions.UserNotAuthenticatedException;
import com.aspire.loan.exceptions.UserNotAuthorizedException;
import com.aspire.loan.model.User;
import com.aspire.loan.repository.UserRepository;
import com.aspire.loan.serviceImpl.UserServiceImpl;
import com.aspire.loan.utility.UserUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserServiceImpl userServiceImpl;
    private Long userId;
    private UserDTO userRequestDTO;
    private UserDTO adminRequestDTO;
    private List<UserDTO> userDTOList;
    private User user;
    private User user1;
    private List<User> users;

    @BeforeEach
    void setUp()
    {
        userDTOList=new ArrayList<>();
        users=new ArrayList<>();

         user= User.builder()
                .created_at_ts(new Date(System.currentTimeMillis()))
                .id(11L)
                .name("testUser")
                .type(UserType.CUSTOMER)
                .password("userTest@1")
                .build();

       user1 = User.builder()
                .created_at_ts(new Date(System.currentTimeMillis()))
                .id(12L)
                .name("testUser2")
                .type(UserType.ADMIN)
                .password("user2@test")
                .build();


       users.add(user);
       users.add(user1);


        userRequestDTO = UserUtility.toUserDTO(user);

        adminRequestDTO  = UserUtility.toUserDTO(user1);

        this.userServiceImpl
                = new UserServiceImpl(userRepository);

    }

    @Test
    void getAllUsers_UnauthorizedException()
    {
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(user));
        assertThrows(UserNotAuthorizedException.class, () -> {
            userServiceImpl.getAllUsers(user.getId(), user.getPassword());
        });
    }

    @Test
    void getAllUsers_NotAuthenticException()
    {
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(user));
        userRequestDTO.setPassword("fakePassword");
        assertThrows(UserNotAuthenticatedException.class, () -> {
            userServiceImpl.getAllUsers(user.getId(), userRequestDTO.getPassword());
        });
    }

    @Test
    void getAllUsers_Success()
    {
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(user1));
        when(userRepository.findAll()).thenReturn(users);
        List<UserDTO> userDtos = userServiceImpl.getAllUsers(12L, user1.getPassword());
        assertEquals(userDtos.size(), 2);
        assertThat(userDtos.contains(user1));
        assertThat(userDtos.contains(user));
    }

    @Test
    void createUser_Success()
    {
//        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(user));
        userRequestDTO.setName("newTestUser");
        when(userRepository.save(Mockito.any(User.class))).thenReturn(UserUtility.toUser(userRequestDTO));
        UserDTO userDto = userServiceImpl.createUser(userRequestDTO);
        assertEquals(userDto.getName(), "newTestUser");
    }

    @Test
    void getUserById_UnauthorizedException()
    {
        // any user cannot see other users data except admin
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(user));
        assertThrows(UserNotAuthorizedException.class, () -> {
            userServiceImpl.getUserById(12L, 11L, user.getPassword());
        });
    }

    @Test
    void getUserById_Succss()
    {
        // any user cannot see other users data except admin
        when(userRepository.findById(12L)).thenReturn(java.util.Optional.ofNullable(user1));
        when(userRepository.findById(11L)).thenReturn(java.util.Optional.ofNullable(user));
        UserDTO userDTO = userServiceImpl.getUserById(11L, 12L, user1.getPassword());
        assertEquals(userDTO.getId(), user.getId());
    }

    @Test
    void updateUser_Unauthorized()
    {
        // any user cannot see other users data except admin
        when(userRepository.findById(11L)).thenReturn(java.util.Optional.ofNullable(user));
        assertThrows(UserNotAuthorizedException.class, () -> {
            userServiceImpl.getUserById(12L, 11L, user.getPassword());
        });
    }

    @Test
    void updateUser_Success()
    {
        // any user cannot see other users data except admin
        when(userRepository.findById(12L)).thenReturn(java.util.Optional.ofNullable(user1));
        adminRequestDTO.setName("adminUpdated");
        when(userRepository.save(Mockito.any(User.class))).thenReturn(UserUtility.toUser(adminRequestDTO));
        UserDTO userDTO = userServiceImpl.updateUser(adminRequestDTO, 12L, 12L, user1.getPassword());
        assertEquals(userDTO.getName(), "adminUpdated");
    }

    @Test
    void deleteUser_Exception()
    {
        when(userRepository.findById(11L)).thenReturn(java.util.Optional.ofNullable(user));
        assertThrows(UserNotAuthorizedException.class, () -> {
            userServiceImpl.deleteUser(12L, 11L, user.getPassword());
        });
    }

    @Test
    void deleteUser_Success()
    {
        // any user cannot see other users data except admin
        when(userRepository.findById(12L)).thenReturn(java.util.Optional.ofNullable(user1));
        when(userRepository.findById(11L)).thenReturn(java.util.Optional.ofNullable(user));
        doNothing().when(userRepository).delete(Mockito.any(User.class));
        userRequestDTO.setPassword(user1.getPassword());
        userServiceImpl.deleteUser(11L, 12L, user1.getPassword());
        assertEquals(userRequestDTO.getId(), user.getId());
    }
}
