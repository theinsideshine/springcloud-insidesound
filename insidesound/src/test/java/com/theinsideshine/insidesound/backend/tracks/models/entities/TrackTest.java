package com.theinsideshine.insidesound.backend.tracks.models.entities;

import com.theinsideshine.insidesound.backend.albums.models.entity.Album;
import com.theinsideshine.insidesound.backend.datas.AlbumData;
import com.theinsideshine.insidesound.backend.datas.TrackData;
import com.theinsideshine.insidesound.backend.tracks.models.entity.Track;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.theinsideshine.insidesound.backend.datas.AlbumData.getFakeImageBytes;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TrackTest {



    @Test
    void testConstructorTrack(){
        Track track =
                new Track(1L, "German", "Abre",  getFakeImageBytes(), getFakeImageBytes(),0L );
       assertNotNull(track.getId(), () -> "El Id no puede ser nula");
    }
    @Test
    void testIdTrack() {
        Track track = TrackData.getTrack();
        Long expectedId = 65L;
        track.setId(expectedId);
        Long realId = track.getId();
        assertNotNull(realId, () -> "El Id no puede ser nula");
        assertTrue(realId.equals(expectedId), () -> "Id debe ser igual a la expectedId");
    }

        @Test
        void testUsernameTrack() {
            Track track = TrackData.getTrack();
            String expectedUsername = "Sonia";
            track.setUsername(expectedUsername);
            String realUsername = track.getUsername();
            assertNotNull(realUsername, () -> "El Username no puede ser nula");
            assertTrue(realUsername.equals(expectedUsername), () -> "Username debe ser igual a la expectedUsername");
        }

    @Test
    void testTitleTrack() {
        Track track = TrackData.getTrack();
        String expectedTitle = "Imagina";
        track.setTitle(expectedTitle);
        String realTitle = track.getTitle();
        assertNotNull(realTitle, () -> "El Title no puede ser nula");
        assertTrue(realTitle.equals(expectedTitle), () -> "Title debe ser igual a la expectedTitle");
    }


    @Test
    void testImageAndMp3Track() {
        Track track = TrackData.getTrack();
        byte[] expectedImage = TrackData.getFakeImageBytes();
        byte[] expectedMp3 = TrackData.getFakeImageBytes() ;
        track.setImage(expectedImage);
        track.setMp3(expectedMp3);
        byte[] realImage = track.getImage();
        assertNotNull(realImage, () -> "El Image no puede ser nula");
        assertTrue(realImage.equals(expectedImage), () -> "Image debe ser igual a la expectedImage");
        byte[] realMp3 = track.getMp3();
        assertNotNull(realImage, () -> "El Mp3 no puede ser nula");
        assertTrue(realImage.equals(expectedImage), () -> "Mp3 debe ser igual a la expectedMp3");
    }


    @Test
    void testAlbumIdTrack() {
        Track track = TrackData.getTrack();
        Long expectedAlbumId = 8L;
        track.setAlbum_id(expectedAlbumId);
        Long realAlbumId = track.getAlbum_id();
        assertNotNull(realAlbumId, () -> "El AlbumId no puede ser nula");
        assertTrue(realAlbumId.equals(expectedAlbumId), () -> "AlbumId debe ser igual a la expectedId");
    }
    @Test
    void testImageHashcodeTrack() {
        Track track = TrackData.getTrack();
        byte[] expectedImage = getFakeImageBytes() ;
        track.setImage(expectedImage);
        Integer realImageHashcode = track.getImageHashCode();
        assertNotNull(realImageHashcode, () -> "El HashCode no puede ser nulo");
        track.setImage(null);
        realImageHashcode = track.getImageHashCode();
        assertNull(realImageHashcode, () -> "El HashCode debe ser nulo");
    }

    @Test
    void testMp3HashcodeTrack() {
        Track track = TrackData.getTrack();
        byte[] expectedMp3 = getFakeImageBytes() ;
        track.setMp3(expectedMp3);
        Integer realMp3Hashcode = track.getMp3HashCode();
        assertNotNull(realMp3Hashcode, () -> "El HashCode no puede ser nulo");
        track.setMp3(null);
        realMp3Hashcode = track.getMp3HashCode();
        assertNull(realMp3Hashcode, () -> "El HashCode debe ser nulo");
    }

}


