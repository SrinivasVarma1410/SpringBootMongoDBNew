package com.example.springbootmongo.controller;

import com.example.springbootmongo.dal.UserRepository;
import com.example.springbootmongo.model.SubUser;
import com.example.springbootmongo.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/")
public class UserController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Environment environment;

    @GetMapping(value = "")
    public List<User> getAllUsers() {
        LOG.info("Fetching all the users");
        return userRepository.findAll();
    }

    @GetMapping(value = "/{userId}")
    public Object getUserById(@PathVariable String userId) {
        LOG.info("Getting user info with ID: {}.", userId);

        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return user;
        } else {
            return "User not found";
        }
    }

    @PostMapping(value = "/create")
    public User createNewUser(@RequestBody User user) {
        LOG.info("Creating user.");
       return userRepository.save(user);
    }


    /**
     * fetch user settings
     * @param userId
     * @return
     */
    @GetMapping(value = "/settings/{userId}")
    public Object getAllUserSettings(@PathVariable String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return user.get().getUserSettings();
        } else {
            return "User not found";
        }
    }

    /**
     * Fetch a particular user setting
     * @param userId
     * @param key
     * @return
     */
    @GetMapping(value = "/settings/{userId}/{key}")
    public String getUserSettings(@PathVariable String userId, @PathVariable String key) {

        Optional<User> user =  userRepository.findById(userId);
        if (user.isPresent()) {
            return user.get().getUserSettings().get(key);
        } else {
            return "User not found";
        }

    }

    @GetMapping(value = "/settings/{userId}/{key}/{value}")
    public String addUserSettings(@PathVariable String userId, @PathVariable String key, @PathVariable String value) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            SubUser subUser = new SubUser();
            subUser.getUserSettings().put(key, value);
            userRepository.save(subUser);
            return "key value added";
        } else {
            return "User not found";
        }
    }

    @PostMapping(value = "/update/{userId}/{name}")
    public Object updateUser(@PathVariable String userId, @PathVariable String name) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            SubUser subUser = new SubUser();
            subUser.setName(name);
            return userRepository.save(subUser);
        } else {
            return "User not found";
        }

    }

    @PostMapping(value = "/delete/{userId}")
    public String deleteUserById(@PathVariable String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return "User deleted";
        } else {
            return "User not found";
        }
    }

}
