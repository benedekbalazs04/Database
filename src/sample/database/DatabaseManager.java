package sample.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_NAME = "music.db";
    private static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Java\\Programok\\GitHub\\DatabaseExample\\" + DB_NAME;

    private static final String TABLE_ARTISTS = "artists";
    private static final String COLUMN_ARTIST_ID = "_id";
    private static final String COLUMN_ARTIST_NAME = "name";

    private static final String TABLE_ALBUMS = "albums";
    private static final String COLUMN_ALBUM_NAME = "name";
    private static final String COLUMN_ALBUM_ID = "_id";
    private static final String COLUMN_ALBUM_ARTIST_ID = "artist";

    private static final String TABLE_SONGS = "songs";
    private static final String COLUMN_SONG_ID = "_id";
    private static final String COLUMN_SONG_TRACK = "track";
    private static final String COLUMN_SONG_TITLE = "title";
    private static final String COLUMN_SONG_ALBUM_ID = "album";

    private static final String SHOW_ARTISTS = "SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID +
            ", " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + " FROM " + TABLE_ARTISTS;

    //    select albums._id, albums.name, albums.artist, artists.name from albums
    //inner join artists on albums.artist = artists._id
    private static final String SHOW_ALBUMS = "SELECT " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID +
            ", " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME +
            ", " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST_ID +
            ", " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME +
            " FROM " + TABLE_ALBUMS + " INNER JOIN " +
            TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST_ID + " = " +
            TABLE_ARTISTS + "." + COLUMN_ARTIST_ID;

    //    select songs._id, songs.track, songs.title, songs.album, albums.name from songs
//    inner join albums on songs.album = albums._id
    private static final String SHOW_SONGS = "SELECT " + TABLE_SONGS + "." + COLUMN_SONG_ID +
            ", " + TABLE_SONGS + "." + COLUMN_SONG_TRACK +
            ", " + TABLE_SONGS + "." + COLUMN_SONG_TITLE +
            ", " + TABLE_SONGS + "." + COLUMN_SONG_ALBUM_ID +
            ", " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME +
            " FROM " + TABLE_SONGS + " INNER JOIN " +
            TABLE_ALBUMS + " ON " + TABLE_SONGS + "." + COLUMN_SONG_ALBUM_ID +
            " = " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID;

    //    select albums._id, albums.name, albums.artist, artists.name from albums
//    inner join artists on albums.artist = artists._id
//    where artists.name = ?
    private static final String QUERY_ALBUMS = "SELECT " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID +
            ", " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME +
            ", " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST_ID +
            ", " + TABLE_ARTISTS +"." + COLUMN_ARTIST_NAME +
            " FROM " + TABLE_ALBUMS +
            " INNER JOIN " + TABLE_ARTISTS + " ON " +
            TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST_ID +
            " = " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID +
            " WHERE " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID + " = ? ";

    //    select songs._id, songs.track, songs.title, songs.album, albums.name from songs
//   inner join albums on songs.album = albums._id
//   where albums.name = ?
    private static final String QUERY_SONGS_FROM_ALBUM = "SELECT " + TABLE_SONGS + "." + COLUMN_SONG_ID +
            ", " + TABLE_SONGS + "." + COLUMN_SONG_TRACK +
            ", " + TABLE_SONGS + "." + COLUMN_SONG_TITLE +
            ", " + TABLE_SONGS + "." + COLUMN_SONG_ALBUM_ID +
            ", " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME +
            " FROM " + TABLE_SONGS +
            " INNER JOIN " + TABLE_ALBUMS + " ON " + TABLE_SONGS + "." + COLUMN_SONG_ALBUM_ID +
            " = " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID +
            " WHERE " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID + " = ? ";

    //    select songs._id, songs.track, songs.title, songs.album, albums.name from songs
//   inner join albums on songs.album = albums._id
//   inner join artists on albums.artist = artists._id
//   where artists.name =
    private static final String QUERY_SONGS_FROM_ARTIST = "SELECT " + TABLE_SONGS + "." + COLUMN_SONG_ID +
            ", " + TABLE_SONGS + "." + COLUMN_SONG_TRACK +
            ", " + TABLE_SONGS + "." + COLUMN_SONG_TITLE +
            ", " + TABLE_SONGS + "." + COLUMN_SONG_ALBUM_ID +
            ", " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME +
            " FROM " + TABLE_SONGS +
            " INNER JOIN " + TABLE_ALBUMS + " ON " +
            TABLE_SONGS + "." + COLUMN_SONG_ALBUM_ID +
            " = " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID +
            " INNER JOIN " + TABLE_ARTISTS + " ON " +
            TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST_ID +
            " = " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID +
            " WHERE " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID + " = ? ";

    // delete from artists where artists._id = ?
    private static final String DELETE_ARTIST = "DELETE FROM " + TABLE_ARTISTS + " WHERE " +
            TABLE_ARTISTS + "." + COLUMN_ARTIST_ID + " = ? ";

    // delete from albums where albums._id = ?
    private static final String DELETE_ALBUM = "DELETE FROM " + TABLE_ALBUMS + " WHERE " +
            TABLE_ALBUMS + "." + COLUMN_ALBUM_ID + " = ? ";

    // delete from songs where songs._id = ?
    private static final String DELETE_SONG = "DELETE FROM " + TABLE_SONGS + " WHERE " +
            TABLE_SONGS + "." + COLUMN_SONG_ID + " = ? ";

    // update artists set name = ? where artists._id = ?
    private static final String EDIT_ARTIST = "UPDATE " + TABLE_ARTISTS + " SET " + COLUMN_ARTIST_NAME + " = ? WHERE " +
            TABLE_ARTISTS + "." + COLUMN_ARTIST_ID + " = ? ";

    // update albums set name = ? , artist = ? where albums._id = ?
    private static final String EDIT_ALMUM = "UPDATE " + TABLE_ALBUMS + " SET " + COLUMN_ALBUM_NAME + " = ? , " +
            COLUMN_ALBUM_ARTIST_ID + " = ? WHERE " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID + " = ? ";

    // update songs set track = ? , title = ? , album = ? where songs._id = ?
    private static final String EDIT_SONG = "UPDATE " + TABLE_SONGS + " SET " + COLUMN_SONG_TRACK + " = ? , " +
            COLUMN_SONG_TITLE + " = ? , " + COLUMN_SONG_ALBUM_ID + " = ? WHERE " + TABLE_SONGS + "." + COLUMN_SONG_ID + " = ? ";

    // select artists._id from artists where artists.name like ?
    private static final String GET_ARTIST_ID = "SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID + " FROM " +
            TABLE_ARTISTS + " WHERE " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + " LIKE ? ";

    // select albums._id from albums where albums.name like ?
    private static final String GET_ALBUM_ID = "SELECT " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID + " FROM " +
            TABLE_ALBUMS + " WHERE " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " LIKE ? ";

    // select count(*) from songs
// inner join albums on songs.album = albums._id
// where albums._id = ?
    private static final String GET_COUNT = "SELECT COUNT(*) FROM " + TABLE_SONGS +
            " INNER JOIN " + TABLE_ALBUMS + " ON " +
            TABLE_SONGS + "." + COLUMN_SONG_ALBUM_ID + " = " +
            TABLE_ALBUMS + "." + COLUMN_ALBUM_ID +
            " WHERE " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID + " = ? ";

    // insert into artists (name) values (?)
    private static final String ADD_ARTIST = "INSERT INTO " + TABLE_ARTISTS +
            "(" + COLUMN_ARTIST_NAME + ") VALUES (?) ";

    // insert into albums (name, artist) values (? , ?)
    private static final String ADD_ALBUM = "INSERT INTO " + TABLE_ALBUMS +
            "(" + COLUMN_ALBUM_NAME + ", " + COLUMN_ALBUM_ARTIST_ID + ") VALUES (? , ?) ";

    // insert into songs (track, title, album) values (? , ? , ?)
    private static final String ADD_SONG = "INSERT INTO " + TABLE_SONGS +
            "(" + COLUMN_SONG_TRACK +
            ", " + COLUMN_SONG_TITLE +
            ", " + COLUMN_SONG_ALBUM_ID +
            ") VALUES (? , ? , ?) ";

    private PreparedStatement queryAlbums;
    private PreparedStatement querySongsFromAlbum;
    private PreparedStatement querySongsFromArtist;
    private PreparedStatement deleteArtist;
    private PreparedStatement deleteAlbum;
    private PreparedStatement deleteSong;
    private PreparedStatement editArtist;
    private PreparedStatement editAlbum;
    private PreparedStatement editSong;
    private PreparedStatement getArtistId;
    private PreparedStatement getAlbumId;
    private PreparedStatement getCount;
    private PreparedStatement addArtist;
    private PreparedStatement addAlbum;
    private PreparedStatement addSong;

    private Connection connection;

    private static DatabaseManager instance = new DatabaseManager();

    public static DatabaseManager getInstance() {
        return instance;
    }

    public void open() {
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING);
            queryAlbums = connection.prepareStatement(QUERY_ALBUMS);
            querySongsFromAlbum = connection.prepareStatement(QUERY_SONGS_FROM_ALBUM);
            querySongsFromArtist = connection.prepareStatement(QUERY_SONGS_FROM_ARTIST);
            deleteArtist = connection.prepareStatement(DELETE_ARTIST);
            deleteAlbum = connection.prepareStatement(DELETE_ALBUM);
            deleteSong = connection.prepareStatement(DELETE_SONG);
            editArtist = connection.prepareStatement(EDIT_ARTIST);
            editAlbum = connection.prepareStatement(EDIT_ALMUM);
            editSong = connection.prepareStatement(EDIT_SONG);
            getArtistId = connection.prepareStatement(GET_ARTIST_ID);
            getAlbumId = connection.prepareStatement(GET_ALBUM_ID);
            getCount = connection.prepareStatement(GET_COUNT);
            addArtist = connection.prepareStatement(ADD_ARTIST);
            addAlbum = connection.prepareStatement(ADD_ALBUM);
            addSong = connection.prepareStatement(ADD_SONG);
        } catch (SQLException e) {
            System.out.println("Couldn't connection to database \n" + e.getMessage());
        }
    }

    public void close() {
        try {
            if (addSong != null){
                addSong.close();
            }
            if (addAlbum != null){
                addAlbum.close();
            }
            if (addArtist != null){
                addArtist.close();
            }
            if (getCount != null){
                getCount.close();
            }
            if (getAlbumId != null){
                getAlbumId.close();
            }
            if (getArtistId != null) {
                getArtistId.close();
            }
            if (editSong != null) {
                editSong.close();
            }
            if (editAlbum != null) {
                editAlbum.close();
            }
            if (editArtist != null) {
                editArtist.close();
            }
            if (deleteSong != null) {
                deleteSong.close();
            }
            if (deleteAlbum != null) {
                deleteAlbum.close();
            }
            if (deleteArtist != null) {
                deleteArtist.close();
            }
            if (queryAlbums != null) {
                queryAlbums.close();
            }
            if (querySongsFromAlbum != null) {
                querySongsFromAlbum.close();
            }
            if (querySongsFromArtist != null) {
                querySongsFromArtist.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't disconnection from database \n" + e.getMessage());
        }
    }

    public List<Artist> showArtist() {
        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(SHOW_ARTISTS)) {
            List<Artist> artists = new ArrayList<>();
            while (result.next()) {
                Artist artist = new Artist();
                artist.setId(result.getInt(1));
                artist.setName(result.getString(2));
                artists.add(artist);
            }
            return artists;

        } catch (SQLException e) {
            System.out.println("Couldn't load artists: " + e.getMessage());
            return null;
        }
    }

    public List<Album> showAlbums() {
        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(SHOW_ALBUMS)) {

            List<Album> albums = new ArrayList<>();
            while (result.next()) {
                Album album = new Album();
                album.setId(result.getInt(1));
                album.setName(result.getString(2));
                album.setArtistId(result.getInt(3));
                album.setArtistName(result.getString(4));
                albums.add(album);
            }
            return albums;
        } catch (SQLException e) {
            System.out.println("Couldn't load Albums: " + e.getMessage());
            return null;
        }
    }

    public List<Song> showSongs() {
        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(SHOW_SONGS)) {
            List<Song> songs = new ArrayList<>();
            while (result.next()) {
                Song song = new Song();
                song.setId(result.getInt(1));
                song.setTrack(result.getInt(2));
                song.setTitle(result.getString(3));
                song.setAlbumId(result.getInt(4));
                song.setAlbumName(result.getString(5));
                songs.add(song);
            }
            return songs;
        } catch (SQLException e) {
            System.out.println("Couldn't load Songs: " + e.getMessage());
            return null;
        }
    }

    public List<Album> showArtistAlbums(int artistId) {
        try {
            queryAlbums.setInt(1, artistId);
            ResultSet result = queryAlbums.executeQuery();
            List<Album> albums = new ArrayList<>();
            while (result.next()) {
                Album album = new Album();
                album.setId(result.getInt(1));
                album.setName(result.getString(2));
                album.setArtistId(result.getInt(3));
                album.setArtistName(result.getString(4));
                albums.add(album);
            }
            return albums;
        } catch (SQLException e) {
            System.out.println("Couldn't load albums: " + e.getMessage());
            return null;
        }
    }

    public List<Song> showArtistSongs(int artist) {
        try {
            querySongsFromArtist.setInt(1, artist);
            ResultSet result = querySongsFromArtist.executeQuery();
            List<Song> songs = new ArrayList<>();
            while (result.next()) {
                Song song = new Song();
                song.setId(result.getInt(1));
                song.setTrack(result.getInt(2));
                song.setTitle(result.getString(3));
                song.setAlbumId(result.getInt(4));
                song.setAlbumName(result.getString(5));
                songs.add(song);
            }
            return songs;
        } catch (SQLException e) {
            System.out.println("Couldn't load songs: " + e.getMessage());
            return null;
        }
    }

    public List<Song> showAlbumSongs(int album) {
        try {
            querySongsFromAlbum.setInt(1, album);
            ResultSet result = querySongsFromAlbum.executeQuery();
            List<Song> songs = new ArrayList<>();
            while (result.next()) {
                Song song = new Song();
                song.setId(result.getInt(1));
                song.setTrack(result.getInt(2));
                song.setTitle(result.getString(3));
                song.setAlbumId(result.getInt(4));
                song.setAlbumName(result.getString(5));
                songs.add(song);
            }
            return songs;
        } catch (SQLException e) {
            System.out.println("Couldn't load songs: " + e.getMessage());
            return null;
        }
    }

    public boolean deleteArtist(int artistsId) {
        try {
            deleteArtist.setInt(1, artistsId);
            deleteArtist.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't delete artist: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteAlbum(int albumId) {
        try {
            deleteAlbum.setInt(1, albumId);
            deleteAlbum.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't delete album: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteSong(int songId) {
        try {
            deleteSong.setInt(1, songId);
            deleteSong.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't delete song: " + e.getMessage());
            return false;
        }
    }

    public boolean editArtist(Artist artist) {
        if (artist == null){
            return false;
        }
        try {
            editArtist.setString(1, artist.getName());
            editArtist.setInt(2, artist.getId());
            int affectedRecords = editArtist.executeUpdate();
            return affectedRecords == 1;
        } catch (SQLException e) {
            System.out.println("Couldn't edit Artist: " + e.getMessage());
            return false;
        }
    }

    public boolean editAlbum(Album album) {
        if (album == null){
            return false;
        }
        try {
            editAlbum.setString(1, album.getName());
            editAlbum.setInt(2, album.getArtistId());
            editAlbum.setInt(3, album.getId());
            int affectRecords = editAlbum.executeUpdate();
            return affectRecords == 1;
        } catch (SQLException e) {
            System.out.println("Couldn't edit Album: " + e.getMessage());
            return false;
        }
    }

    public boolean editSong(Song song) {
        if (song == null){
            return false;
        }
        try {
            editSong.setInt(1, song.getTrack());
            editSong.setString(2, song.getTitle());
            editSong.setInt(3, song.getAlbumId());
            editSong.setInt(4, song.getId());
            int affectedRecords = editSong.executeUpdate();
            return affectedRecords == 1;
        } catch (SQLException e) {
            System.out.println("Couldn't edit Song: " + e.getMessage());
            return false;
        }
    }

    public int getArtistId(String artistName) {
        try {
            getArtistId.setString(1, artistName);
            ResultSet result = getArtistId.executeQuery();
            return result.getInt(1);
        }catch (SQLException e){
            System.out.println("Couldn't found " + artistName + ": " + e.getMessage());
            return -1;
        }
    }

    public int getAlbumId(String albumName){
        try {
            getAlbumId.setString(1,albumName);
            ResultSet result = getAlbumId.executeQuery();
            return result.getInt(1);
        }catch (SQLException e){
            System.out.println("Couldn't found " + albumName + ": " + e.getMessage());
            return -1;
        }
    }

    public int getCount(Album album){
        try {
            getCount.setInt(1, album.getId());
            ResultSet result = getCount.executeQuery();
            return result.getInt(1);
        }catch (SQLException e){
            System.out.println("Couldn't not count " + album.getName() + " album tracks: " +e.getMessage());
            return -1;
        }
    }

    public boolean addArtist(Artist artist){
        if (artist == null){
            return false;
        }
        try {
            addArtist.setString(1,artist.getName());
            int result = addArtist.executeUpdate();
            return result == 1;
        }catch (SQLException e){
            System.out.println("Couldn't add Artist: " + e.getMessage());
            return false;
        }

    }

    public boolean addAlbum(Album album){
        if (album == null){
            return false;
        }
        try {
            addAlbum.setString(1,album.getName());
            addAlbum.setInt(2,album.getArtistId());
            int result = addAlbum.executeUpdate();
            return result == 1;
        }catch (SQLException e){
            System.out.println("Couldn't add Album: " + e.getMessage());
            return false;
        }
    }

    public boolean addSong(Song song){
        if (song == null){
            return false;
        }
        try {
            addSong.setInt(1,song.getTrack());
            addSong.setString(2,song.getTitle());
            addSong.setInt(3,song.getAlbumId());
            int result = addSong.executeUpdate();
            return result == 1;
        }catch (SQLException e){
            System.out.println("Couldn't add Song: " + e.getMessage());
            return false;
        }
    }
}
