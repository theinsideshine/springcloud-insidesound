package com.theinsideshine.insidesound.backend.datas;

import com.theinsideshine.insidesound.backend.FakeImageProvider;
import com.theinsideshine.insidesound.backend.albums.models.dto.AlbumRequestDto;
import com.theinsideshine.insidesound.backend.albums.models.dto.AlbumResponseDto;
import com.theinsideshine.insidesound.backend.albums.models.entity.Album;
import com.theinsideshine.insidesound.backend.tracks.models.dto.TrackRequestDto;
import com.theinsideshine.insidesound.backend.tracks.models.dto.TrackResponseDto;
import com.theinsideshine.insidesound.backend.tracks.models.entity.Track;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TrackData {
    public static List<Track> getTrackList(){

        List<Track> tracks = Arrays.asList(
                new Track(1L, "German", "Abre",  getFakeImageBytes(), getFakeImageBytes(),0L ),
                new Track(2L, "Sol", "Vida", getFakeImageBytes(),getFakeImageBytes(),0L)
        );
        return tracks;
    }

    public static Track getTrack() {
        Track track = new Track(1L, "German", "Abre",  getFakeImageBytes(), getFakeImageBytes(),0L );
        return track;
    }

    public static List<TrackResponseDto> getTracksResponseDtoList(){

        List<Track> tracks = getTrackList();
        List<TrackResponseDto>  tracksResponseDto = tracks.stream()
                .map(TrackResponseDto::trackResponseDtoMapperEntityToDto)
                .collect(Collectors.toList());

         return tracksResponseDto;
    }

    public static TrackResponseDto getTrackResponseDto(){
        TrackResponseDto  trackResponseDto =
                new TrackResponseDto(1L, "German", "Abre",  null, null,8L );
        return trackResponseDto;
    }
    public static MockMultipartFile getMultipartFileImage() {

        MockMultipartFile multipartFile = new MockMultipartFile(
                "imageFile",                // Nombre del parámetro del archivo
                "image.jpg",                // Nombre del archivo
                MediaType.IMAGE_JPEG_VALUE, // Tipo de contenido de la imagen
                getFakeImageBytes()              // Bytes de la imagen
        );
        return multipartFile;
    }

    public static MockMultipartFile getMultipartFileMp3() {

        MockMultipartFile multipartFile = new MockMultipartFile(
                "mp3File",                // Nombre del parámetro del archivo
                "song.jpg",                // Nombre del archivo
                String.valueOf(MediaType.APPLICATION_OCTET_STREAM), // Tipo de contenido del mp3
                getFakeImageBytes()              // Bytes de la Fakeimagen
        );
        return multipartFile;
    }
    public static byte[] getFakeImageBytes(){
        return FakeImageProvider.getFakeImageBytes();
    }
    public static TrackRequestDto getTrackRequestDto() {
        MockMultipartFile multipartFileImage = getMultipartFileImage();
        MockMultipartFile multipartFileMp3 = getMultipartFileMp3();
        TrackRequestDto trackRequestDto = new TrackRequestDto(1L, "German", "Abre" ,multipartFileImage , multipartFileMp3, 0L);
        return trackRequestDto;
    }


}
