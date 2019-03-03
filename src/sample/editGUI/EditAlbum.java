package sample.editGUI;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import sample.database.Album;
import sample.database.Artist;
import sample.database.DatabaseManager;

public class EditAlbum {
    @FXML
    private TextField albumName;
    @FXML
    private TextField artistName;

    public void setFields(Album album){
        albumName.setText(album.getName());
        artistName.setText(album.getArtistName());
    }

    public void setFieldFromArtist(Artist artist){
        artistName.setText(artist.getName());
    }

    public Album newAlbum(Album album ){
        Album newAlbum = new Album();
        newAlbum.setId(album.getId());
        newAlbum.setName(albumName.getText());
        int artistId = DatabaseManager.getInstance().getArtistId(artistName.getText());
        if (artistId == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No exist");
            alert.setHeaderText("Artist no exist!");
            alert.setContentText("The Artist name is not filled enough attention\n" +
                    " so we step back!\n" +
                    "If the Artist is new too, Please first Create a new Artist!");
            alert.showAndWait();
            return null;
        }
        newAlbum.setArtistId(artistId);
        return newAlbum;
    }

    public Album newAlbum(){
        Album newAlbum = new Album();
        newAlbum.setName(albumName.getText());
        int artistId = DatabaseManager.getInstance().getArtistId(artistName.getText());
        if (artistId == -1){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No exist");
            alert.setHeaderText("Artist no exist!");
            alert.setContentText("The Artist name is not filled enough attention\n" +
                    " so we step back!\n" +
                    "If the Artist is new too, Please first Create a new Artist!");
            alert.showAndWait();
            return null;
        }
        newAlbum.setArtistId(artistId);
        return newAlbum;
    }
}
