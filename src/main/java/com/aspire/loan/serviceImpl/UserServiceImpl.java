package com.aspire.loan.serviceImpl;


import com.aspire.loan.dto.UserDTO;
import com.aspire.loan.enums.UserType;
import com.aspire.loan.exceptions.UserNotAuthorizedException;
import com.aspire.loan.exceptions.UserNotFoundException;
import com.aspire.loan.model.User;
import com.aspire.loan.repository.UserRepository;
import com.aspire.loan.services.UserService;
import com.aspire.loan.utility.UserUtility;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserDTO> getAllUsers(long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User with id: "+ userId+" does not exist"));
        UserUtility.isUserAuthentic(user, password);

        if(user.getType().equals(UserType.ADMIN)) {
            List<User> users = userRepository.findAll();
            return users.stream()
                    .map(u-> UserUtility.toUserDTO(u))
                    .collect(Collectors.toList());
        }else{
            throw new UserNotAuthorizedException("User with id: "+userId+" is not authorized to view other user details");
        }
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = UserUtility.toUser(userDTO);
        user.setType(UserType.CUSTOMER);
        User savedUser = userRepository.save(user);
        return UserUtility.toUserDTO(user);
    }

    @Override
    public UserDTO getUserById(long userId, long id, String password) {
        User userAuth = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("User with id: "+ id+" does not exist"));
        UserUtility.isUserAuthentic(userAuth, password);

        if(!userAuth.getType().equals(UserType.ADMIN) && !(userId==id))
               throw new UserNotAuthorizedException("User with id: "+id+" is not authorized to view other users data");

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User with id: "+ userId+" does not exist"));
        UserDTO userDTO = UserUtility.toUserDTO(user);
        return userDTO;
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO, long userId, long id, String password) {
        User userAuth = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("User does not exists with id: "+ id));
        UserUtility.isUserAuthentic(userAuth, password);

        if(!userAuth.getType().equals(UserType.ADMIN) && !(userId==id))
            throw new UserNotAuthorizedException("User with id: "+id+" is not authorized to view other users data");

        User savedUser = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User with id: "+ userId +" does not exist"));

        savedUser.setName(userDTO.getName());
        savedUser.setType(savedUser.getType());
        String pass = userDTO.getPassword();
        if(pass!=null)
            savedUser.setPassword(pass);
        User newUser= userRepository.save(savedUser);
        return UserUtility.toUserDTO(newUser);
    }

    @Override
    public void deleteUser(long userId, long id, String password) {
        User userAuth = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("User does not exists with id: "+ id));
        UserUtility.isUserAuthentic(userAuth, password);

        if(!userAuth.getType().equals(UserType.ADMIN) && !(userId==id))
            throw new UserNotAuthorizedException("User with id: " + id + " is not authorized to view other users data");

        User user = userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("User does not exists with id: "+ userId));
        userRepository.delete(user);
    }

}
