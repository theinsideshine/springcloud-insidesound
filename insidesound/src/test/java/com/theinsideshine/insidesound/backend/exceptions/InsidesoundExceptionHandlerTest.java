package com.theinsideshine.insidesound.backend.exceptions;

import com.theinsideshine.insidesound.backend.albums.controllers.AlbumController;
import com.theinsideshine.insidesound.backend.albums.services.AlbumService;
import com.theinsideshine.insidesound.backend.exceptions.insidesound.InsidesoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AlbumController.class)
@ActiveProfiles("test")
public class InsidesoundExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlbumService albumService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    public void whenInvalidShowImageByAlbumId_thenErrorResponse() throws Exception {

        Long invalidId = 1L;

        given(albumService.findImageById(invalidId))
                .willThrow(new InsidesoundException(HttpStatus.BAD_REQUEST.value(),  Map.of("GET IMAGES", "El id del album no tiene imagen.")));



        mockMvc.perform(get("/albums/img/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.errors[\"GET IMAGES\"]").value("El id del album no tiene imagen."));
    }

    @Test
    public void testInsidesoundExceptionWithStatusCodeAndErrorMap() {
        // Arrange
        int expectedStatusCode = 400;
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("key1", "value1");
        errorMap.put("key2", "value2");

        // Act
        InsidesoundException exception = new InsidesoundException(expectedStatusCode, errorMap);

        // Assert
        assertEquals(expectedStatusCode, exception.getStatusCode());
        assertEquals("{key1=value1, key2=value2}", exception.getMessage());
        assertEquals(errorMap, exception.getErrorMap());
    }




}
