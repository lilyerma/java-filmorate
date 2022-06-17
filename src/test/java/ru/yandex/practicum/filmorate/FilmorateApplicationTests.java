package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.yandex.practicum.filmorate.controllers.ErrorHandler;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.controllers.UserController;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.services.UserService;
import ru.yandex.practicum.filmorate.storages.FilmStorage;
import ru.yandex.practicum.filmorate.storages.UserStorage;
import ru.yandex.practicum.filmorate.validations.UserNotFoundException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@WebMvcTest
@AutoConfigureMockMvc
class FilmorateApplicationTests {


    @Autowired
    FilmController filmController;

    @Autowired
    UserController userController;

    @Autowired
    FilmStorage filmStorage;

    @Autowired
    UserStorage userStorage;

    @Autowired
    FilmService filmService;

    @Autowired
    UserService userService;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    MockMvc mockMvc;


    @Test
    void contextLoads() throws Exception {
        assertThat(filmController).isNotNull();
    }


    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    ObjectMapper mapper;

    @Test
    void empty_name_gives_400_status() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .content("{id\":\"0\", " +
                                "\"name\": \"\"," +
                                "\"description\": \"Меньше Двести знаков Двести\"," +
                                "\"releaseDate\": \"2008-03-25\"," +
                                "\"duration\": \"120\"}")
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(500));
    }

    @Test
    void description_more_than_200() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .content("\n" +
                                "  \"id\":\"0\",\n" +
                                "  \"name\": \"название\",\n" +
                                "  \"description\": \"Больше Двести знаков Двести знаков Двести знаков Двести знаков " +
                                "Двести знаков Двести знаков Двести знаков \" +\n" +
                                "                \"Двести знаков Двести знаков Двести знаков Двести знаков Двести " +
                                "знаков Двести знаков Двести\" +\n" +
                                "                \" знаков Двести\",\n" +
                                "  \"releaseDate\": \"2008-03-25\",\n" +
                                "  \"duration\": 120\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(500));
    }

    @Test
    void duration_less_than_0_gives_500_status() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .content("{ id\":\"0\", " +
                                "\"name\": \"название\"," +
                                "\"description\": \"Меньше Двести знаков Двести\"," +
                                "\"releaseDate\": \"2008-03-25\"," +
                                "\"duration\": \"-120\"}")
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(500));
    }

    @Test
    void year_before_1895_gives_500_status() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .content("\n" +
                                "  \"id\":\"0\",\n" +
                                "  \"name\": \"название\",\n" +
                                "  \"description\": \"Меньше Двести знаков Двести\",\n" +
                                "  \"releaseDate\": \"1798-03-25\",\n" +
                                "  \"duration\": 120\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(500));
    }

    @Test
    void create_film_with_good_data() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .content("{\"id\":\"0\", " +
                                "\"name\": \"название\", " +
                                "\"description\": \"Меньше Двести знаков Двести\"," +
                                "\"releaseDate\": \"1967-03-25\"," +
                                "\"duration\": \"120\" }")
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }


    @Test
    void empty_email_gives_500_status() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .content("{\n" +
                                "  \"id\":\"0\",\n" +
                                "  \"email\": \"\",\n" +
                                "  \"login\": \"somelogin\",\n" +
                                "  \"name\": \"somename\",\n" +
                                "  \"birthday\": \"1967-03-25\",\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(500));
    }

    @Test
    void loginWithSpaces_gives_500_status() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .content("{\n" +
                                "  \"id\":\"0\",\n" +
                                "  \"email\": \"email@email.com\",\n" +
                                "  \"login\": \"some login\",\n" +
                                "  \"name\": \"somename\",\n" +
                                "  \"birthday\": \"1967-03-25\",\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(500));
    }

    @Test
    void empty_name_gives_200_status() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .content("{ \"id\":\"0\"," +
                                " \"email\": \"email@email.com\"," +
                                " \"login\": \"somelogin\"," +
                                " \"name\": \"\"," +
                                "\"birthday\": \"1967-03-25\"}")
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));

    }


    @Test
    void createUser_200_status() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .content("{ \"id\":\"0\"," +
                                " \"email\": \"email@email.com\"," +
                                " \"login\": \"somelogin\"," +
                                " \"name\": \"somename\"," +
                                "\"birthday\": \"1967-03-25\"}")
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(200));
    }


    @Test
    void addFriend_valid_returns_200() throws Exception {
        User user1 = new User();
        user1.setName("Somebody");
        user1.setBirthday(LocalDate.parse("1967-03-25"));
        user1.setEmail("email@email.com");
        userStorage.create(user1);

        User user2 = new User();
        user2.setName("Somebody");
        user2.setBirthday(LocalDate.parse("1967-03-25"));
        user2.setEmail("email@email.com");
        userStorage.create(user2);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/1/friends/2")
                        .content("")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }

    @Test
    void addFriend_valid_returns_404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}/friends/{friendId}", 1L, 3L))
                .andExpect(status().is(404));
    }

    @Test
    void delete_wrong_Friend_returns_404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}/friends/{friendId}", 1L, 3L))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteFriend_returns_200() throws Exception {
        User user1 = new User();
        user1.setName("Somebody");
        user1.setBirthday(LocalDate.parse("1967-03-25"));
        user1.setEmail("email@email.com");
        userStorage.create(user1);

        User user2 = new User();
        user2.setName("Somebody");
        user2.setBirthday(LocalDate.parse("1967-03-25"));
        user2.setEmail("email@email.com");
        userStorage.create(user2);

        userService.addFriend(1L, 2L);


        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}/friends/{friendId}", 1L, 2L))
                .andExpect(status().isOk());

    }

    @Test
    void return_friends_by_id() throws Exception {
        User user1 = new User();
        user1.setName("Somebody");
        user1.setBirthday(LocalDate.parse("1967-03-25"));
        user1.setEmail("email@email.com");
        userStorage.create(user1);

        User user2 = new User();
        user2.setName("Somebody");
        user2.setBirthday(LocalDate.parse("1967-03-25"));
        user2.setEmail("email@email.com");
        userStorage.create(user2);

        userService.addFriend(1L, 2L);


        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}/friends", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void return_friends_by_id_not_found() throws Exception {


        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}/friends", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void return_mutual_friends_200() throws Exception {
        User user1 = new User();
        user1.setName("Somebody");
        user1.setBirthday(LocalDate.parse("1967-03-25"));
        user1.setEmail("email@email.com");
        userStorage.create(user1);

        User user2 = new User();
        user2.setName("Somebody");
        user2.setBirthday(LocalDate.parse("1967-03-25"));
        user2.setEmail("email@email.com");
        userStorage.create(user2);

        User user3 = new User();
        user3.setName("Somebody");
        user3.setBirthday(LocalDate.parse("1967-03-25"));
        user3.setEmail("email@email.com");
        userStorage.create(user3);

        userService.addFriend(1L, 2L);
        userService.addFriend(1L, 3L);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}/friends/common/{otherId}", 3L, 2L))
                .andExpect(status().isOk());
    }

    @Test
    void return_mutual_friends_404() throws Exception {
        User user1 = new User();
        user1.setName("Somebody");
        user1.setBirthday(LocalDate.parse("1967-03-25"));
        user1.setEmail("email@email.com");
        userStorage.create(user1);

        User user2 = new User();
        user2.setName("Somebody");
        user2.setBirthday(LocalDate.parse("1967-03-25"));
        user2.setEmail("email@email.com");
        userStorage.create(user2);

        User user3 = new User();
        user3.setName("Somebody");
        user3.setBirthday(LocalDate.parse("1967-03-25"));
        user3.setEmail("email@email.com");
        userStorage.create(user3);

        userService.addFriend(1L, 2L);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}/friends/common/{otherId}", 30L, 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_nonfriend_friend_404() throws Exception {
        User user1 = new User();
        user1.setName("Somebody");
        user1.setBirthday(LocalDate.parse("1967-03-25"));
        user1.setEmail("email@email.com");
        userStorage.create(user1);

        User user2 = new User();
        user2.setName("Somebody");
        user2.setBirthday(LocalDate.parse("1967-03-25"));
        user2.setEmail("email@email.com");
        userStorage.create(user2);

        User user3 = new User();
        user3.setName("Somebody");
        user3.setBirthday(LocalDate.parse("1967-03-25"));
        user3.setEmail("email@email.com");
        userStorage.create(user3);

        userService.addFriend(1L, 2L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}/friends/{otherId}", 3L, 1L))
                .andExpect(status().is(404));
    }

    @Test
    void delete_valid_friend_200() throws Exception {
        User user1 = new User();
        user1.setName("Somebody");
        user1.setBirthday(LocalDate.parse("1967-03-25"));
        user1.setEmail("email@email.com");
        userStorage.create(user1);

        User user2 = new User();
        user2.setName("Somebody");
        user2.setBirthday(LocalDate.parse("1967-03-25"));
        user2.setEmail("email@email.com");
        userStorage.create(user2);

        userService.addFriend(1L, 2L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}/friends/{otherId}", 1L, 2L))
                .andExpect(status().isOk());
    }

    @Test
    void get_valid_friends_200() throws Exception {
        User user1 = new User();
        user1.setName("Somebody");
        user1.setBirthday(LocalDate.parse("1967-03-25"));
        user1.setEmail("email@email.com");
        userStorage.create(user1);

        User user2 = new User();
        user2.setName("Somebody");
        user2.setBirthday(LocalDate.parse("1967-03-25"));
        user2.setEmail("email@email.com");
        userStorage.create(user2);

        User user3 = new User();
        user3.setName("Somebody");
        user3.setBirthday(LocalDate.parse("1967-03-25"));
        user3.setEmail("email@email.com");
        userStorage.create(user3);

        userService.addFriend(1L, 2L);

        userService.addFriend(1L, 3L);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}/friends", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void user_sets_like_to_film_200() throws Exception {
        User user1 = new User();
        user1.setName("Somebody");
        user1.setBirthday(LocalDate.parse("1967-03-25"));
        user1.setEmail("email@email.com");
        userStorage.create(user1);

        Film film1 = new Film();
        film1.setName("some film");
        film1.setDescription("some description");
        film1.setDuration(120);
        filmStorage.create(film1);

        //    filmService.addLike(1L, 1L);

        mockMvc.perform(MockMvcRequestBuilders.put("/films/{id}/like/{userId}", 1L, 1L))
                .andExpect(status().isOk());

    }

    @Test
    void user_delete_like_from_film_200() throws Exception {
        User user1 = new User();
        user1.setName("Somebody");
        user1.setBirthday(LocalDate.parse("1967-03-25"));
        user1.setEmail("email@email.com");
        userStorage.create(user1);

        Film film1 = new Film();
        film1.setName("some film");
        film1.setDescription("some description");
        film1.setDuration(120);
        filmStorage.create(film1);

        filmService.addLike(1L, 1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/films/{id}/like/{userId}", 1L, 1L))
                .andExpect(status().isOk());
    }

    @Test
    void get_popular_count_from_film_200() throws Exception {
        User user1 = new User();
        user1.setName("Somebody");
        user1.setBirthday(LocalDate.parse("1967-03-25"));
        user1.setEmail("email@email.com");
        userStorage.create(user1);

        User user2 = new User();
        user2.setName("Somebody");
        user2.setBirthday(LocalDate.parse("1967-03-25"));
        user2.setEmail("email@email.com");
        userStorage.create(user2);

        Film film1 = new Film();
        film1.setName("some film");
        film1.setDescription("some description");
        film1.setDuration(120);
        filmStorage.create(film1);

        filmService.addLike(1L, 1L);
        filmService.addLike(2L, 1L);

        mockMvc.perform(MockMvcRequestBuilders.get("/films/popular?count={count}",10))
                .andExpect(status().isOk());
    }

}





