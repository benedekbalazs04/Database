package sample.editGUI;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import sample.database.Artist;
import sample.database.DatabaseManager;

public class EditArtist {
    @FXML
    private TextField artistName;

    public void setField(Artist artist){
        artistName.setText(artist.getName());
    }

    public Artist newArtist(Artist artist){
        Artist newArtist = new Artist();
        newArtist.setId(artist.getId());
        newArtist.setName(artistName.getText());
        return newArtist;
    }

    public Artist newArtist(){
        Artist newArtist = new Artist();
        int existArtist = DatabaseManager.getInstance().getArtistId(artistName.getText());
        if (!(existArtist == -1)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Is exist");
            alert.setHeaderText("Artist is exist!");
            alert.setContentText("The Artist is exist\n" +
                    " so we step back!");
            alert.showAndWait();
            return null;
        }
        newArtist.setName(artistName.getText());
        return newArtist;
    }
}
