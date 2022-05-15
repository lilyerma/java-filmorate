package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validations.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private int counter = 0;
    //   Logbook logbook = Logbook.create();

    @GetMapping("/users")
    public HashMap<Integer, User> findAll() {
        log.debug("Текущее количество пользователей: " + users.size());
        return (HashMap<Integer, User>) users;
    }

    @PostMapping(value = "/users")
    public User create(@RequestBody User user) throws ValidationException {
        log.debug("Такой пользователь: " + user);
      try {
          validateUser(user);
          if (user.getName() == null) {
              user.setName(user.getLogin());
          }
          user.setId(counter + 1);
          users.put(user.getId(), user);
          log.debug("Добавил пользователя " + user);
          return user;
      }catch (ValidationException e){
          log.debug(e.getMessage());
          throw e;
      }
    }

    @PutMapping(value = "/users")
    public User update(@RequestBody User user) throws ValidationException {
        log.debug("Такой пользователь: " + user);
      try {
          validateUser(user);
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
      } catch (ValidationException e){
          log.debug(e.getMessage());
          throw e;
      }
    }


    public void validateUser(User user) throws ValidationException {
        if (user.getEmail().isBlank() || user.getEmail() == null) {
            throw new ValidationException("пустой email");
        } else if (!user.getEmail().contains("@")) {
            throw new ValidationException("неверный формат email");
        } else if (user.getLogin().contains(" ")) {
            throw new ValidationException("в логине пробелы");
        } else if (user.getLogin().isBlank() || user.getLogin() == null) {
            throw new ValidationException("пустой логин");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("дата рождения в будущем");
        }
    }


}
