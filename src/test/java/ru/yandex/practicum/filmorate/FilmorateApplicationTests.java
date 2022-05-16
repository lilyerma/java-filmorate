package ru.yandex.practicum.filmorate;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.controllers.UserController;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import ru.yandex.practicum.filmorate.model.User;

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

    @MockBean
    FilmController filmController;

    @MockBean
    UserController userController;

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
                        .andExpect(status().is(400));
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
                .andExpect(status().is(400));
    }
    @Test
    void duration_less_than_0_gives_400_status() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .content("{ id\":\"0\", " +
                                        "\"name\": \"название\"," +
                                        "\"description\": \"Меньше Двести знаков Двести\"," +
                                        "\"releaseDate\": \"2008-03-25\"," +
                                "\"duration\": \"-120\"}")
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    void year_before_1895_gives_400_status() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .content("\n" +
                                "  \"id\":\"0\",\n" +
                                        "  \"name\": \"название\",\n" +
                                        "  \"description\": \"Меньше Двести знаков Двести\",\n" +
                                        "  \"releaseDate\": \"1798-03-25\",\n" +
                                        "  \"duration\": 120\n" +
                                        "}")
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
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
    void empty_email_gives_400_status() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .content("{\n" +
                                "  \"id\":\"0\",\n" +
                                "  \"email\": \"\",\n" +
                                "  \"login\": \"somelogin\",\n" +
                                "  \"name\": \"somename\",\n" +
                                "  \"birthday\": \"1967-03-25\",\n"+
                                "}")
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    void loginWithSpaces_gives_400_status() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .content("{\n" +
                                "  \"id\":\"0\",\n" +
                                "  \"email\": \"email@email.com\",\n" +
                                "  \"login\": \"some login\",\n" +
                                "  \"name\": \"somename\",\n" +
                                "  \"birthday\": \"1967-03-25\",\n"+
                                "}")
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
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

//    @Test
//    void testingTest() throws Exception {
//        LocalDate date = LocalDate.of(1982,8,2);
//        User user = new User();
//        user.setName("somename");
//        user.setId(0);
//        user.setBirthday(date);
//        user.setEmail("some@email.com");
//        user.setLogin("somelogin");
//
//        String body = mapper.writeValueAsString(user);
//        when(userController.create(user)).thenCallRealMethod();
//        mockMvc.perform(MockMvcRequestBuilders.post("/users")
//                        .content(body)
//                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().is(200))
//                .andExpect(content().string(containsString("Hello, World")));
//
//    }

}

