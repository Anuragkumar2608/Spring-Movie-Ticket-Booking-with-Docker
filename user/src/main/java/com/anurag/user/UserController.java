package com.anurag.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public ResponseEntity<UserT> createUser(@RequestBody UserRequest request){
        return userService.createUser(request);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<Optional<UserT>> getUser(@PathVariable Integer userId){
        return userService.getUser(userId);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId){
        return userService.deleteUser(userId);
    }

    @DeleteMapping("/users")
    public ResponseEntity<Void> deleteUsers(){
        return userService.deleteUsers();
    }

}
