package com.aspire.loan.controller;

import com.aspire.loan.dto.UserDTO;
import com.aspire.loan.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(@RequestHeader(name="Authorization_id") long id,
                                                     @RequestHeader(name="Authorization_password") String password){
        List<UserDTO> users= userService.getAllUsers(id, password);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO){
        UserDTO savedUser = userService.createUser(userDTO);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("userId") long userId, @RequestHeader(name="Authorization_id") long id,
                                               @RequestHeader(name="Authorization_password") String password){
        UserDTO user = userService.getUserById(userId, id, password);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable long userId, @RequestBody UserDTO userDTORequest,
                                              @RequestHeader(name="Authorization_id") long id,
                                              @RequestHeader(name="Authorization_password") String password){
        UserDTO userDTO=userService.updateUser(userDTORequest, userId, id, password);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable long userId, @RequestHeader(name="Authorization_id") long id,
                                                 @RequestHeader(name="Authorization_password") String password){
        userService.deleteUser(userId, id, password);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
