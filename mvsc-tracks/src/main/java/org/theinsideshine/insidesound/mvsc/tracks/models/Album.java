package org.theinsideshine.insidesound.mvsc.tracks.models;



public class Album {


    private Long id;

    private String username;

    private String title;

    private String artist;


    private String age;

    private boolean albumprivate;



    private byte[]  image;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public boolean isAlbumprivate() {
        return albumprivate;
    }

    public void setAlbumprivate(boolean albumprivate) {
        this.albumprivate = albumprivate;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }


    public Integer getImageHashCode() {
        return ( this.image != null ) ? this.image.hashCode() : null ;
    }
}
