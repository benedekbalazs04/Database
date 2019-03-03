package sample.editGUI;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import sample.database.Album;
import sample.database.DatabaseManager;
import sample.database.Song;

public class EditSong {
    @FXML
    private TextField songTitle;
    @FXML
    private Spinner<Integer> songTrack;
    @FXML
    private TextField albumName;

    public void initialize(){
        SpinnerValueFactory<Integer> spinnerValueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1,99,1);
        songTrack.setValueFactory(spinnerValueFactory);
    }

    public void setFields(Song song){
        songTitle.setText(song.getTitle());
        SpinnerValueFactory<Integer> spinnerValueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1,99,song.getTrack());
        songTrack.setValueFactory(spinnerValueFactory);
        albumName.setText(song.getAlbumName());
    }

    public void setFieldFromAlbum(Album album){
        albumName.setText(album.getName());
        int albumHasTrack = DatabaseManager.getInstance().getCount(album);
        SpinnerValueFactory<Integer> spinnerValueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(albumHasTrack + 1,99,albumHasTrack + 1);
        songTrack.setValueFactory(spinnerValueFactory);
    }

    public Song newSong(Song song){
        Song newSong = new Song();
        newSong.setId(song.getId());
        newSong.setTitle(songTitle.getText());
        newSong.setTrack(songTrack.getValue());
        int albumId = DatabaseManager.getInstance().getAlbumId(albumName.getText());
        if (albumId == -1){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No exist");
            alert.setHeaderText("Album no exist!");
            alert.setContentText("The Album name is not filled enough attention\n" +
                    " so we step back!\n" +
                    "If the Album is new too, Please first Create a new Album!");
            alert.showAndWait();
            return null;
        }
        newSong.setAlbumId(albumId);
        return newSong;
    }

    public Song newSong(){
        Song newSong = new Song();
        newSong.setTrack(songTrack.getValue());
        newSong.setTitle(songTitle.getText());
        int albumId = DatabaseManager.getInstance().getAlbumId(albumName.getText());
        if (albumId == -1){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No exist");
            alert.setHeaderText("Album no exist!");
            alert.setContentText("The Album name is not filled enough attention\n" +
                    " so we step back!\n" +
                    "If the Album is new too, Please first Create a new Album!");
            alert.showAndWait();
            return null;
        }
        newSong.setAlbumId(albumId);
        return newSong;
    }
}
