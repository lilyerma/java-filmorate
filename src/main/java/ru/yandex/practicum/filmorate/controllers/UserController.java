package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.UserService;
import ru.yandex.practicum.filmorate.storages.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storages.UserStorage;
import ru.yandex.practicum.filmorate.validations.ValidationException;

import javax.naming.InvalidNameException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    //  private final Map<Integer, User> users = new HashMap<Integer, User>();
    //  private static final Logger log = LoggerFactory.getLogger(UserController.class);
    UserStorage userStorage;
    UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }


    @GetMapping("{id}")
    public User getById(@PathVariable Long id) {
        return userStorage.getById(id);
    }

    @GetMapping("{id}/friends")
    public List<User> getFriendsById(@PathVariable Long id) {
        return userService.getFriendsById(id);
    }


    @GetMapping
    public List<User> findAll() {
        return userStorage.getUsers().values().stream().collect(Collectors.toList());
    }

    @PostMapping
    public User create(@Valid @NotNull @RequestBody User user) {
        return userStorage.create(user);
    }

    @PutMapping
    public User update(@Valid @NotNull @RequestBody User user) {
        return userStorage.update(user);
    }

    @DeleteMapping
    public User remove(@Valid @NotNull @RequestBody User user) {
        return userStorage.remove(user);
    }

    @PutMapping ("{id}/friends/{friendId}")
    public Map<User,User> addFriend(
    @PathVariable Long id,
    @PathVariable Long friendId) {
      return userService.addFriend(id,friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public Map<Long,Long> removeFriend(
            @PathVariable Long id,
            @PathVariable Long friendId
    ){
        return userService.deleteFriend(id,friendId);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(
            @PathVariable Long id,
            @PathVariable Long otherId
    ){
        return userService.mutualFriends(id, otherId);
    }


    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> exceptionHandler(ValidationException e) {
        log.debug(e.getMessage());
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }





}
