package com.springlearning.rest.webservices.restfulwebservices.controllers;

import com.springlearning.rest.webservices.restfulwebservices.beans.User;
import com.springlearning.rest.webservices.restfulwebservices.exceptions.UserNotFoundException;
import com.springlearning.rest.webservices.restfulwebservices.services.UserDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private final UserDaoService userService;

    public UserController(UserDaoService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/users")
    public ResponseEntity<Object> saveUser(@Valid @RequestBody User user) {
        // status should be created , statusCode 201
        User createdUser = userService.save(user);
        ServletUriComponentsBuilder servletUriComponentsBuilder = ServletUriComponentsBuilder.fromCurrentRequest();
        URI uri = servletUriComponentsBuilder.path("/{id}").buildAndExpand(createdUser.getId()).toUri();
        // this will append the URI to the ResponseHeader and statusCode will be 201
        return ResponseEntity.created(uri).build();
    }

    // retrieveAllUsers
    @GetMapping(path = "/users")
    public List<User> retrieveAllUsers() {
        return userService.findAll();
    }

    // retrieveUser(INT ID)
    @GetMapping(path = "/users/{id}")
    public User getUniqueUser(@PathVariable int id) {
        User user = userService.findOne(id);
        if (user == null) {
            throw new UserNotFoundException("id-" + id);
        }
        return user;
    }

    @DeleteMapping(path = "/users/{id}")
    public void deleteUser(@PathVariable int id) {
        User user = userService.deleteById(id);
        if (user == null) {
            throw new UserNotFoundException("id-" + id);
        }
    }
}
