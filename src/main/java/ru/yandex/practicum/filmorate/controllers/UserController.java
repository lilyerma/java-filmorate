package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validations.ValidationException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<Integer, User>();
  //  private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private int counter = 0;


    @GetMapping
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }



    @PostMapping
    public User create(@Valid @NotNull @RequestBody User user)  {
        log.debug("Такой пользователь: " + user);
  //        validateUser(user);
          if (user.getName() == null) {
              user.setName(user.getLogin());
          }
          user.setId(counter + 1);
          counter+=1;
          int id = user.getId();
          users.put(id, user);
          log.debug("Добавил пользователя " + user);
          return user;

    }

    @PutMapping
    public User update(@Valid @NotNull @RequestBody User user)  {
        log.debug("Такой пользователь: " + user);
          if (user.getName() == null) {
              user.setName(user.getLogin());
          }
          if (users.containsKey(user.getId())) {
              users.put(user.getId(), user);
              log.debug("Обновил пользователя " + user);
              return user;
          } else {
              log.debug("Нет пользователя с таким ID " + user);
              throw new ValidationException("Нет пользователя с таким id");
          }
    }


    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> exceptionHandler(ValidationException e) {
        log.debug(e.getMessage());
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


}
