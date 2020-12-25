package com.springlearning.rest.webservices.restfulwebservices.services;

import com.springlearning.rest.webservices.restfulwebservices.beans.Post;
import com.springlearning.rest.webservices.restfulwebservices.beans.User;
import com.springlearning.rest.webservices.restfulwebservices.exceptions.UserNotFoundException;
import com.springlearning.rest.webservices.restfulwebservices.respositories.PostRepository;
import com.springlearning.rest.webservices.restfulwebservices.respositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class UserJpaResource {
    @Autowired
    private final UserDaoService userService;

    @Autowired
    private final PostRepository postRepository;

    @Autowired
    private final UserRepository userRepository;

    public UserJpaResource(UserDaoService userService, PostRepository postRepository, UserRepository userRepository) {
        this.userService = userService;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }


    @PostMapping(path = "/jpa/users")
    public ResponseEntity<Object> saveUser(@Valid @RequestBody User user) {
        // status should be created , statusCode 201
        User createdUser = userRepository.save(user);
        ServletUriComponentsBuilder servletUriComponentsBuilder = ServletUriComponentsBuilder.fromCurrentRequest();
        URI uri = servletUriComponentsBuilder.path("/{id}").buildAndExpand(createdUser.getId()).toUri();
        // this will append the URI to the ResponseHeader and statusCode will be 201
        return ResponseEntity.created(uri).build();
    }

    // retrieveAllUsers
    @GetMapping(path = "/jpa/users")
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    // retrieveUser(INT ID)
    @GetMapping(path = "/jpa/users/{id}")
    public User getUniqueUser(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new UserNotFoundException("id-" + id);
        }
        return user.get();
    }

    @DeleteMapping(path = "/jpa/users/{id}")
    public void deleteUser(@PathVariable int id) {
        userRepository.deleteById(id);
    }

    @GetMapping("/jpa/users/{id}/posts")
    public List<Post> retrieveAllUsers(@PathVariable int id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException("id-" + id);
        }
        return optionalUser.get().getPosts();
    }

    @PostMapping("/jpa/users/{id}/posts")
    public ResponseEntity<Object> createPost(@PathVariable int id, @RequestBody Post post) {
        Optional<User> savedUser = userRepository.findById(id);
        if (!savedUser.isPresent()) {
            throw new UserNotFoundException("id-" + id);
        }
        User user = savedUser.get();
        post.setUser(user);
        postRepository.save(post);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(post.getId()).toUri();
        return ResponseEntity.created(location).build();
    }
}
