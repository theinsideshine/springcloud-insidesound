package com.theinsideshine.insidesound.backend.albums.models.entities;

import com.theinsideshine.insidesound.backend.albums.models.entity.Album;
import com.theinsideshine.insidesound.backend.datas.AlbumData;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.theinsideshine.insidesound.backend.datas.AlbumData.getFakeImageBytes;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AlbumTest {


    @Test
    void testConstructorsAlbum(){
        Album album =
                new Album(1L, "German", "Abre", "Fito Paez",
                        "2022", true, getFakeImageBytes());
        Album album1 =
                new Album( "German", "Abre", "Fito Paez",
                        "2022", true, getFakeImageBytes());
        assertNotNull(album.getId(), () -> "El Id no puede ser nula");
        assertNull(album1.getId(), () -> "El Id debe ser nula");
    }


    @Test
    void testIdAlbum() {
        Album album = AlbumData.getAlbum();
        Long expectedId = 65L;
        album.setId(expectedId);
        Long realId = album.getId();
        assertNotNull(realId, () -> "El Id no puede ser nula");
        assertTrue(realId.equals(expectedId), () -> "Id debe ser igual a la expectedId");
    }

        @Test
        void testUsernameAlbum() {
            Album album = AlbumData.getAlbum();
            String expectedUsername = "Lucio";
            album.setUsername(expectedUsername);
            String realUsername = album.getUsername();
            assertNotNull(realUsername, () -> "El Username no puede ser nula");
            assertTrue(realUsername.equals(expectedUsername), () -> "Username debe ser igual a la expectedUsername");
        }

    @Test
    void testTitleAlbum() {
        Album album = AlbumData.getAlbum();
        String expectedTitle = "Que sea Rock";
        album.setTitle(expectedTitle);
        String realTitle = album.getTitle();
        assertNotNull(realTitle, () -> "El Title no puede ser nula");
        assertTrue(realTitle.equals(expectedTitle), () -> "Title debe ser igual a la expectedTitle");
    }

    @Test
    void testArtistAlbum() {
        Album album = AlbumData.getAlbum();
        String expectedArtist = "Pappo";
        album.setArtist(expectedArtist);
        String realArtist = album.getArtist();
        assertNotNull(realArtist, () -> "El Artist no puede ser nula");
        assertTrue(realArtist.equals(expectedArtist), () -> "Artist debe ser igual a la expectedArtist");
    }

    @Test
    void testAgeAlbum() {
        Album album = AlbumData.getAlbum();
        String expectedAge= "1977";
        album.setAge(expectedAge);
        String realAge = album.getAge();
        assertNotNull(realAge, () -> "El Age no puede ser nula");
        assertTrue(realAge.equals(expectedAge), () -> "Age debe ser igual a la expectedAge");
    }

    @Test
    void testAlbumprivateAlbum() {
        Album album = AlbumData.getAlbum();
        boolean expectedAlbumprivate = true ;
        album.setAlbumprivate(expectedAlbumprivate);
        boolean realAlbumprivate = album.isAlbumprivate();
        assertNotNull(realAlbumprivate, () -> "El Albumprivate no puede ser nula");
        assertTrue(realAlbumprivate, () -> "Albumprivate debe ser igual a la expectedAlbumprivate");
    }

    @Test
    void testImageAlbum() {
        Album album = AlbumData.getAlbum();
        byte[] expectedImage = getFakeImageBytes() ;
        album.setImage(expectedImage);
        byte[] realImage = album.getImage();
        assertNotNull(realImage, () -> "El Image no puede ser nula");
        assertTrue(realImage.equals(expectedImage), () -> "Image debe ser igual a la expectedImage");
    }

    @Test
    void testImageHashcodeAlbum() {
        Album album = AlbumData.getAlbum();
        byte[] expectedImage = getFakeImageBytes() ;
        album.setImage(expectedImage);
        Integer realImageHashcode = album.getImageHashCode();
        assertNotNull(realImageHashcode, () -> "El HashCode no puede ser nula");
        album.setImage(null);
        realImageHashcode = album.getImageHashCode();
        assertNull(realImageHashcode, () -> "El HashCode debe ser nulo");
    }


}


