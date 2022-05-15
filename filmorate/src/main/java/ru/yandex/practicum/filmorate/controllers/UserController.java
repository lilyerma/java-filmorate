package ru.yandex.practicum.filmorate.controllers;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validations.ValidationException;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private int counter = 0;
    //   Logbook logbook = Logbook.create();

    @GetMapping
    public List<User> findAll() {
        return (List<User>) users.values();
    }

    @PostMapping
    public User add(@NotNull @RequestBody User user) throws ValidationException {
        if (!users.containsKey(user.getId())) {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
                users.put(user.getId(), user);
                log.debug("Добавлен пользователь");
            } else {
                users.put(user.getId(), user);
                log.debug("Добавлен пользователь");
            }
            return user;
        } else {
            throw new ValidationException("Такой пользователь уже есть");
        }
    }

    @PutMapping
    public User update(@NotNull @RequestBody User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Пользователь добавлен");
        }
        users.put(user.getId(), user);
        log.debug("Пользователь добавлен",user.getId());
        return user;
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> exceptionHandler(ValidationException e) {
        log.debug(e.getMessage());
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


//    @PostMapping(value = "/users")
//    public User create(@RequestBody User user) throws ValidationException {
//        log.debug("Такой пользователь: " + user);
//      try {
//          validateUser(user);
//          if (user.getName() == null) {
//              user.setName(user.getLogin());
//          }
//          user.setId(counter + 1);
//          users.put(user.getId(), user);
//          log.debug("Добавил пользователя " + user);
//          return user;
//      }catch (ValidationException e){
//          log.debug(e.getMessage());
//          throw e;
//      }
//    }
//
//    @PutMapping(value = "/users")
//    public User update(@RequestBody User user) throws ValidationException {
//        log.debug("Такой пользователь: " + user);
//      try {
//          validateUser(user);
//          if (user.getName() == null) {
//              user.setName(user.getLogin());
//          }
//          if (users.containsKey(user.getId())) {
//              users.put(user.getId(), user);
//              log.debug("Обновил пользователя " + user);
//              return user;
//          } else {
//              log.debug("Нет пользователя с таким ID " + user);
//              throw new ValidationException("Нет пользователя с таким id");
//          }
//      } catch (ValidationException e){
//          log.debug(e.getMessage());
//          throw e;
//      }
//    }
//
//
//    public void validateUser(User user) throws ValidationException {
//        if (user.getEmail().isBlank() || user.getEmail() == null) {
//            throw new ValidationException("пустой email");
//        } else if (!user.getEmail().contains("@")) {
//            throw new ValidationException("неверный формат email");
//        } else if (user.getLogin().contains(" ")) {
//            throw new ValidationException("в логине пробелы");
//        } else if (user.getLogin().isBlank() || user.getLogin() == null) {
//            throw new ValidationException("пустой логин");
//        } else if (user.getBirthday().isAfter(LocalDate.now())) {
//            throw new ValidationException("дата рождения в будущем");
//        }
//    }


}
