package com.theinsideshine.insidesound.backend.tracks.models.dtos;

import com.theinsideshine.insidesound.backend.albums.models.dto.AlbumRequestDto;
import com.theinsideshine.insidesound.backend.albums.models.entity.Album;
import com.theinsideshine.insidesound.backend.datas.AlbumData;
import com.theinsideshine.insidesound.backend.datas.TrackData;
import com.theinsideshine.insidesound.backend.tracks.models.dto.TrackRequestDto;
import com.theinsideshine.insidesound.backend.tracks.models.entity.Track;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TrackRequestDtoTest {

    @Test
    void testTrackRequestDtoMapperDtoToEntity() throws IOException {
        TrackRequestDto expectedTrackRequestDto = TrackData.getTrackRequestDto();
        Track realTrack= TrackRequestDto.TrackRequestDtoMapperDtoToEntity(expectedTrackRequestDto);
        assertEquals(expectedTrackRequestDto.id(), realTrack.getId());
        assertEquals(expectedTrackRequestDto.username(), realTrack.getUsername());
        assertEquals(expectedTrackRequestDto.title(), realTrack.getTitle());
        assertEquals(expectedTrackRequestDto.imageFile().getBytes(), realTrack.getImage());
        assertEquals(expectedTrackRequestDto.mp3File().getBytes(), realTrack.getMp3());
        assertEquals(expectedTrackRequestDto.album_id(), realTrack.getAlbum_id());
    }
}
