package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import sample.database.*;
import sample.editGUI.EditAlbum;
import sample.editGUI.EditArtist;
import sample.editGUI.EditSong;

import java.io.IOException;
import java.util.Optional;

public class MainController {
    @FXML
    public BorderPane borderPane;
    @FXML
    public TableView mainTableView;
    @FXML
    public TextField searchField;
    @FXML
    public Label labelTopOnTable;
    @FXML
    public Button bArtistList;
    @FXML
    public Button bAlbumsList;
    @FXML
    public Button bSongsList;
    @FXML
    public Button bArtistAlbums;
    @FXML
    public Button bShowSelectedSongs;
    @FXML
    public Button bSearch;

    private static final int COLUMNS_FOR_ARTISTS = 1;
    private static final int COLUMNS_FOR_ALBUMS = 2;
    private static final int COLUMNS_FOR_SONGS = 3;

    private CurrentList currentList;

    // if currentList = ARTIST_ALBUMS -> currentListId = artists._id
    // if currentList = ARTIST_SONGS -> currentListId = artists._id
    // if currentList = ALBUM_SONGS -> currentListId = albums._id
    private int currentListId;
    private boolean artistOrAlbum = true;

    private static final String ABOUT = " It is the best Database Manager application! \n I don't give you any useful information.";
    private static final String REMOVE_ITEM = "remove";
    private static final String EDIT_ITEM = "Edit ";
    private static final String EDIT_ERROR_HEAD = "Error while edit!";
    private static final String EDIT_ERROR_CONTENT = "Got bug while editing.\nItem has not been edited!";
    private static final String ADD_ERROR_HEAD = "Error while add!";
    private static final String ADD_ERROR_CONTENT = "Got bug when added.\nItem has not been added!";

    public void initialize() {
        setTooltip();
        showArtistsList();
    }

    private void setTooltip() {
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Show Albums of Artist");
        bArtistAlbums.setTooltip(tooltip);
        tooltip = new Tooltip();
        tooltip.setText("Show Songs of Artist");
        bShowSelectedSongs.setTooltip(tooltip);
        tooltip = new Tooltip();
        tooltip.setText("Show Details of Song");
        tooltip = new Tooltip();
        tooltip.setText("Show Artists list");
        bArtistList.setTooltip(tooltip);
        tooltip = new Tooltip();
        tooltip.setText("Search from List");
        bSearch.setTooltip(tooltip);
        tooltip = new Tooltip();
        tooltip.setText("Write here what you want from list");
        searchField.setTooltip(tooltip);
        tooltip = new Tooltip();
        tooltip.setText("Show Albums list");
        bAlbumsList.setTooltip(tooltip);
        tooltip = new Tooltip();
        tooltip.setText("Show Songs list");
        bSongsList.setTooltip(tooltip);
    }

    @FXML
    public void showArtistsList() {
        setColumn(COLUMNS_FOR_ARTISTS);
        Task<ObservableList<Artist>> task = new Task<ObservableList<Artist>>() {
            @Override
            protected ObservableList<Artist> call() {
                return FXCollections.observableArrayList(DatabaseManager.getInstance().showArtist());
            }
        };
        mainTableView.itemsProperty().bind(task.valueProperty());
        currentList = CurrentList.ARTISTS_LIST;
        currentListId = -1;
        new Thread(task).start();
    }

    @FXML
    public void showAlbumsList() {
        setColumn(COLUMNS_FOR_ALBUMS);
        Task<ObservableList<Album>> task = new Task<ObservableList<Album>>() {
            @Override
            protected ObservableList<Album> call() {
                return FXCollections.observableArrayList(DatabaseManager.getInstance().showAlbums());
            }
        };
        mainTableView.itemsProperty().bind(task.valueProperty());
        currentList = CurrentList.ALBUMS_LIST;
        currentListId = -1;
        new Thread(task).start();
    }

    @FXML
    public void showSongsList() {
        setColumn(COLUMNS_FOR_SONGS);
        Task<ObservableList<Song>> task = new Task<ObservableList<Song>>() {
            @Override
            protected ObservableList<Song> call() {
                return FXCollections.observableArrayList(DatabaseManager.getInstance().showSongs());
            }
        };
        mainTableView.itemsProperty().bind(task.valueProperty());
        currentList = CurrentList.SONGS_LIST;
        currentListId = -1;
        new Thread(task).start();
    }

    @FXML
    public void showArtistAlbums() {
        final Artist artist = (Artist) mainTableView.getSelectionModel().getSelectedItem();
        if (artist == null) {
            notSelectError();
            return;
        }
        Task<ObservableList<Album>> task = new Task<ObservableList<Album>>() {
            @Override
            protected ObservableList<Album> call() {
                return FXCollections.observableArrayList(DatabaseManager.getInstance().showArtistAlbums(artist.getId()));
            }
        };
        setColumn(COLUMNS_FOR_ALBUMS);
        mainTableView.itemsProperty().bind(task.valueProperty());
        currentList = CurrentList.ARTIST_ALBUMS;
        currentListId = artist.getId();
        new Thread(task).start();
    }

    private void showArtistAlbums(int artistId) {
        Task<ObservableList<Album>> task = new Task<ObservableList<Album>>() {
            @Override
            protected ObservableList<Album> call() {
                return FXCollections.observableArrayList(DatabaseManager.getInstance().showArtistAlbums(artistId));
            }
        };
        setColumn(COLUMNS_FOR_ALBUMS);
        mainTableView.itemsProperty().bind(task.valueProperty());
        currentList = CurrentList.ARTIST_ALBUMS;
        currentListId = artistId;
        new Thread(task).start();
    }

    @FXML
    public void showArtistOrAlbumSongs() {
        Task<ObservableList<Song>> task;

        // ARTIST SONGS

        if (artistOrAlbum) {
            Artist artist = (Artist) mainTableView.getSelectionModel().getSelectedItem();
            if (artist == null) {
                notSelectError();
                return;
            }
            task = new Task<ObservableList<Song>>() {
                @Override
                protected ObservableList<Song> call() {
                    return FXCollections.observableArrayList(DatabaseManager.getInstance().showArtistSongs(artist.getId()));
                }
            };
            currentList = CurrentList.ARTIST_SONGS;
            currentListId = artist.getId();
        }

        // ALBUM SONGS

        else {
            Album album = (Album) mainTableView.getSelectionModel().getSelectedItem();
            if (album == null) {
                notSelectError();
                return;
            }
            task = new Task<ObservableList<Song>>() {
                @Override
                protected ObservableList<Song> call() {
                    return FXCollections.observableArrayList(DatabaseManager.getInstance().showAlbumSongs(album.getId()));
                }
            };
            currentList = CurrentList.ALBUM_SONGS;
            currentListId = album.getId();
        }
        setColumn(COLUMNS_FOR_SONGS);
        mainTableView.itemsProperty().bind(task.valueProperty());
        new Thread(task).start();
    }

    private void showArtistOrAlbumSongs(int idForList) {
        Task<ObservableList<Song>> task;

        // ARTIST SONGS

        if (artistOrAlbum) {
            task = new Task<ObservableList<Song>>() {
                @Override
                protected ObservableList<Song> call() {
                    return FXCollections.observableArrayList(DatabaseManager.getInstance().showArtistSongs(idForList));
                }
            };
            currentList = CurrentList.ARTIST_SONGS;
            currentListId = idForList;
        }

        // ALBUM SONGS

        else {
            task = new Task<ObservableList<Song>>() {
                @Override
                protected ObservableList<Song> call() {
                    return FXCollections.observableArrayList(DatabaseManager.getInstance().showAlbumSongs(idForList));
                }
            };
            currentList = CurrentList.ALBUM_SONGS;
            currentListId = idForList;
        }
        setColumn(COLUMNS_FOR_SONGS);
        mainTableView.itemsProperty().bind(task.valueProperty());
        new Thread(task).start();
    }

    @FXML
    public void clickedAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About?");
        alert.setHeaderText("I give you:");
        alert.setContentText(ABOUT);
        alert.showAndWait();
    }

    @FXML
    public void addArtist() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(borderPane.getScene().getWindow());
        dialog.setTitle("Creator");
        dialog.setHeaderText("New it's time to create!");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/sample/editGUI/editArtist.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog: " + e.getMessage());
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.APPLY);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        EditArtist editArtist = fxmlLoader.getController();

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.APPLY) {
            Artist newArtist = editArtist.newArtist();
            if (DatabaseManager.getInstance().addArtist(newArtist)) {
                return;
            }
            fatalError(ADD_ERROR_HEAD, ADD_ERROR_CONTENT);
        }
    }

    @FXML
    public void addAlbum() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(borderPane.getScene().getWindow());
        dialog.setTitle("Creator");
        dialog.setHeaderText("New it's time to create!");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/sample/editGUI/editAlbum.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog: " + e.getMessage());
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.APPLY);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        EditAlbum editAlbum = fxmlLoader.getController();

        Artist artist =(Artist) mainTableView.getSelectionModel().getSelectedItem();
        if (artist != null){
            editAlbum.setFieldFromArtist(artist);
        }
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.APPLY) {
            Album newAlbum = editAlbum.newAlbum();
            if (DatabaseManager.getInstance().addAlbum(newAlbum)) {
                return;
            }
            fatalError(ADD_ERROR_HEAD, ADD_ERROR_CONTENT);
        }
    }

    @FXML
    public void addSong() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(borderPane.getScene().getWindow());
        dialog.setTitle("Creator");
        dialog.setHeaderText("New it's time to create!");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/sample/editGUI/editSong.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog: " + e.getMessage());
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.APPLY);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        EditSong editSong = fxmlLoader.getController();

        Album album = (Album) mainTableView.getSelectionModel().getSelectedItem();
        if (album != null){
            editSong.setFieldFromAlbum(album);
        }
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.APPLY) {
            Song newSongs = editSong.newSong();
            if (DatabaseManager.getInstance().addSong(newSongs)) {
                return;
            }
            fatalError(ADD_ERROR_HEAD, ADD_ERROR_CONTENT);
        }
    }

    @FXML
    public void clickedExit() {
        Platform.exit();
    }

    @FXML
    public void editItem() {

        // ARTIST LIST

        if (currentList == CurrentList.ARTISTS_LIST) {
            Artist artist = (Artist) mainTableView.getSelectionModel().getSelectedItem();
            if (artist == null) {
                notSelectError();
                return;
            }
            if (areYouSure(artist.getName(), EDIT_ITEM)) {

                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.initOwner(borderPane.getScene().getWindow());
                dialog.setTitle(EDIT_ITEM.toUpperCase());
                dialog.setHeaderText(EDIT_ITEM + artist.getName());

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/sample/editGUI/editArtist.fxml"));

                try {
                    dialog.getDialogPane().setContent(fxmlLoader.load());
                } catch (IOException e) {
                    System.out.println("Couldn't load the dialog: " + e.getMessage());
                    return;
                }

                dialog.getDialogPane().getButtonTypes().add(ButtonType.APPLY);
                dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

                EditArtist editArtist = fxmlLoader.getController();
                editArtist.setField(artist);

                Optional<ButtonType> result = dialog.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.APPLY) {
                    Artist newArtist = editArtist.newArtist(artist);
                    if (DatabaseManager.getInstance().editArtist(newArtist)) {
                        showArtistsList();
                        return;
                    }
                    fatalError(EDIT_ERROR_HEAD, EDIT_ERROR_CONTENT);
                }
            }
        }

        // ALBUMS LIST

        if (currentList == CurrentList.ALBUMS_LIST) {
            Album album = (Album) mainTableView.getSelectionModel().getSelectedItem();
            if (album == null) {
                notSelectError();
                return;
            }
            if (areYouSure(album.getName(), EDIT_ITEM)) {

                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.initOwner(borderPane.getScene().getWindow());
                dialog.setTitle(EDIT_ITEM);
                dialog.setHeaderText(EDIT_ITEM + album.getName());

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/sample/editGUI/editAlbum.fxml"));

                try {
                    dialog.getDialogPane().setContent(fxmlLoader.load());
                } catch (IOException e) {
                    System.out.println("Couldn't load the dialog: " + e.getMessage());
                    return;
                }

                dialog.getDialogPane().getButtonTypes().add(ButtonType.APPLY);
                dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
                EditAlbum editAlbum = fxmlLoader.getController();
                editAlbum.setFields(album);

                Optional<ButtonType> result = dialog.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.APPLY) {
                    Album newalbum = editAlbum.newAlbum(album);
                    if (DatabaseManager.getInstance().editAlbum(newalbum)) {
                        showAlbumsList();
                        return;
                    }
                    fatalError(EDIT_ERROR_HEAD, EDIT_ERROR_CONTENT);
                }
            }
        }

        // SONGS LIST

        if (currentList == CurrentList.SONGS_LIST) {
            Song song = (Song) mainTableView.getSelectionModel().getSelectedItem();
            if (song == null) {
                notSelectError();
                return;
            }
            if (areYouSure(song.getTitle(), EDIT_ITEM)) {

                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.initOwner(borderPane.getScene().getWindow());
                dialog.setTitle(EDIT_ITEM);
                dialog.setHeaderText(EDIT_ITEM + song.getTitle());

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/sample/editGUI/editSong.fxml"));

                try {
                    dialog.getDialogPane().setContent(fxmlLoader.load());
                } catch (IOException e) {
                    System.out.println("Couldn't load the dialog: " + e.getMessage());
                    return;
                }

                dialog.getDialogPane().getButtonTypes().add(ButtonType.APPLY);
                dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
                EditSong editSong = fxmlLoader.getController();
                editSong.setFields(song);

                Optional<ButtonType> result = dialog.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.APPLY) {
                    Song newSong = editSong.newSong(song);
                    if (DatabaseManager.getInstance().editSong(newSong)) {
                        showSongsList();
                        return;
                    }
                    fatalError(EDIT_ERROR_HEAD, EDIT_ERROR_CONTENT);
                }
            }
        }

        // ARTIST ALBUMS

        if (currentList == CurrentList.ARTIST_ALBUMS) {
            Album album = (Album) mainTableView.getSelectionModel().getSelectedItem();
            if (album == null) {
                notSelectError();
                return;
            }
            if (areYouSure(album.getName(), EDIT_ITEM)) {

                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.initOwner(borderPane.getScene().getWindow());
                dialog.setTitle(EDIT_ITEM);
                dialog.setHeaderText(EDIT_ITEM + album.getName());

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/sample/editGUI/editAlbum.fxml"));

                try {
                    dialog.getDialogPane().setContent(fxmlLoader.load());
                } catch (IOException e) {
                    System.out.println("Couldn't load the dialog: " + e.getMessage());
                    return;
                }

                dialog.getDialogPane().getButtonTypes().add(ButtonType.APPLY);
                dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
                EditAlbum editAlbum = fxmlLoader.getController();
                editAlbum.setFields(album);

                Optional<ButtonType> result = dialog.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.APPLY) {
                    Album newAlbum = editAlbum.newAlbum(album);
                    if (DatabaseManager.getInstance().editAlbum(newAlbum)) {
                        showArtistAlbums(currentListId);
                        return;
                    }
                    fatalError(EDIT_ERROR_HEAD, EDIT_ERROR_CONTENT);
                }
            }
        }

        // ARTIST SONGS

        if (currentList == CurrentList.ARTIST_SONGS) {
            Song song = (Song) mainTableView.getSelectionModel().getSelectedItem();
            if (song == null) {
                notSelectError();
                return;
            }
            if (areYouSure(song.getTitle(), EDIT_ITEM)) {

                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.initOwner(borderPane.getScene().getWindow());
                dialog.setTitle(EDIT_ITEM);
                dialog.setHeaderText(EDIT_ITEM + song.getTitle());

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/sample/editGUI/editSong.fxml"));

                try {
                    dialog.getDialogPane().setContent(fxmlLoader.load());
                } catch (IOException e) {
                    System.out.println("Couldn't load the dialog: " + e.getMessage());
                    return;
                }

                dialog.getDialogPane().getButtonTypes().add(ButtonType.APPLY);
                dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
                EditSong editSong = fxmlLoader.getController();
                editSong.setFields(song);

                Optional<ButtonType> result = dialog.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.APPLY) {
                    Song newSong = editSong.newSong(song);
                    if (DatabaseManager.getInstance().editSong(newSong)) {
                        showArtistOrAlbumSongs(currentListId);
                        return;
                    }
                    fatalError(EDIT_ERROR_HEAD, EDIT_ERROR_CONTENT);
                }
            }
        }

        // ALBUM SONGS

        if (currentList == CurrentList.ALBUM_SONGS) {
            Song song = (Song) mainTableView.getSelectionModel().getSelectedItem();
            if (song == null) {
                notSelectError();
                return;
            }
            if (areYouSure(song.getTitle(), EDIT_ITEM)) {

                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.initOwner(borderPane.getScene().getWindow());
                dialog.setTitle(EDIT_ITEM);
                dialog.setHeaderText(EDIT_ITEM + song.getTitle());

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/sample/editGUI/editSong.fxml"));

                try {
                    dialog.getDialogPane().setContent(fxmlLoader.load());
                } catch (IOException e) {
                    System.out.println("Couldn't load the dialog: " + e.getMessage());
                    return;
                }

                dialog.getDialogPane().getButtonTypes().add(ButtonType.APPLY);
                dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
                EditSong editSong = fxmlLoader.getController();
                editSong.setFields(song);

                Optional<ButtonType> result = dialog.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.APPLY) {
                    Song newSong = editSong.newSong(song);
                    if (DatabaseManager.getInstance().editSong(newSong)) {
                        showArtistOrAlbumSongs(currentListId);
                        return;
                    }
                    fatalError(EDIT_ERROR_HEAD, EDIT_ERROR_CONTENT);
                }
            }
        }
    }

    @FXML
    public void deleteItem() {

        //ARTISTS LIST

        if (currentList == CurrentList.ARTISTS_LIST) {
            Artist artist = (Artist) mainTableView.getSelectionModel().getSelectedItem();
            if (artist == null) {
                notSelectError();
                return;
            }
            if (areYouSure(artist.getName(), REMOVE_ITEM)) {
                if (DatabaseManager.getInstance().deleteArtist(artist.getId())) {
                    showArtistsList();
                    return;
                }
                fatalError("Database error", "Something wrong with database!");
            }
        }

        //ALBUMS LIST

        if (currentList == CurrentList.ALBUMS_LIST) {
            Album album = (Album) mainTableView.getSelectionModel().getSelectedItem();
            if (album == null) {
                notSelectError();
                return;
            }
            if (areYouSure(album.getName(), REMOVE_ITEM)) {
                if (DatabaseManager.getInstance().deleteAlbum(album.getId())) {
                    showAlbumsList();
                    return;
                }
                fatalError("Database error", "Something wrong with database!");
            }
        }

        //SONGS LIST

        if (currentList == CurrentList.SONGS_LIST) {
            Song song = (Song) mainTableView.getSelectionModel().getSelectedItem();
            if (song == null) {
                notSelectError();
                return;
            }
            if (areYouSure(song.getTitle(), REMOVE_ITEM)) {
                if (DatabaseManager.getInstance().deleteSong(song.getId())) {
                    showSongsList();
                    return;
                }
                fatalError("Database error", "Something wrong with database!");
            }
        }

        // ARTIST ALBUMS

        if (currentList == CurrentList.ARTIST_ALBUMS) {
            Album album = (Album) mainTableView.getSelectionModel().getSelectedItem();
            if (album == null) {
                notSelectError();
                return;
            }
            if (areYouSure(album.getName(), REMOVE_ITEM)) {
                if (DatabaseManager.getInstance().deleteAlbum(album.getId())) {
                    showArtistAlbums(currentListId);
                    return;
                }
                fatalError("Database error", "Something wrong with database!");
            }
        }

        // ARTIST SONGS

        if (currentList == CurrentList.ARTIST_SONGS) {
            Song song = (Song) mainTableView.getSelectionModel().getSelectedItem();
            if (song == null) {
                notSelectError();
                return;
            }
            if (areYouSure(song.getTitle(), REMOVE_ITEM)) {
                if (DatabaseManager.getInstance().deleteSong(song.getId())) {
                    showArtistOrAlbumSongs(currentListId);
                    return;
                }
                fatalError("Database error", "Something wrong with database!");
            }
        }

        // ALBUMS SONGS

        if (currentList == CurrentList.ALBUM_SONGS) {
            Song song = (Song) mainTableView.getSelectionModel().getSelectedItem();
            if (song == null) {
                notSelectError();
                return;
            }
            if (areYouSure(song.getTitle(), REMOVE_ITEM)) {
                if (DatabaseManager.getInstance().deleteSong(song.getId())) {
                    showArtistOrAlbumSongs(currentListId);
                    return;
                }
                fatalError("Database error", "Something wrong with database!");
            }
        }
    }

    @FXML
    public void searchFromList() {

        // ARTISTS LIST

        if (currentList == CurrentList.ARTISTS_LIST) {
            ObservableList<Artist> artists = mainTableView.getItems();
            for (Artist artist : artists) {
                if (itIsIt(artist.getId(),searchField.getText()) ||
                        itIsIt(artist.getName(),searchField.getText())) {
                    mainTableView.scrollTo(artist);
                    mainTableView.getSelectionModel().select(artist);
                }
            }
            return;
        }

        // ALBUMS LIST || ARTIST ALBUMS

        if (currentList == CurrentList.ALBUMS_LIST ||
                currentList == CurrentList.ARTIST_ALBUMS){
            ObservableList<Album> albums = mainTableView.getItems();
            for (Album album : albums){
                if (itIsIt(album.getId(),searchField.getText()) ||
                        itIsIt(album.getName(),searchField.getText())||
                        itIsIt(album.getArtistId(),searchField.getText()) ||
                        itIsIt(album.getArtistName(),searchField.getText())){
                    mainTableView.scrollTo(album);
                    mainTableView.getSelectionModel().select(album);
                }
            }
            return;
        }

        // SONGS LIST || ARTIST SONGS || ALBUM SONGS

        if (currentList == CurrentList.SONGS_LIST ||
                currentList == CurrentList.ARTIST_SONGS ||
                currentList == CurrentList.ALBUM_SONGS){
            ObservableList<Song> songs = mainTableView.getItems();
            for (Song song : songs){
                if (itIsIt(song.getId(),searchField.getText()) ||
                        itIsIt(song.getTrack(),searchField.getText()) ||
                        itIsIt(song.getTitle(),searchField.getText()) ||
                        itIsIt(song.getAlbumId(),searchField.getText()) ||
                        itIsIt(song.getAlbumName(),searchField.getText())){
                    mainTableView.scrollTo(song);
                    mainTableView.getSelectionModel().select(song);
                }
            }
            return;
        }
    }

    private void setColumn(int type) {

        // ARTISTS COLUMNS

        if (type == 1) {
            TableColumn idCol = new TableColumn("id");
            idCol.setCellValueFactory(new PropertyValueFactory<Artist, Integer>("id"));
            idCol.setPrefWidth(50.0);
            TableColumn nameCol = new TableColumn("Name");
            nameCol.setCellValueFactory(new PropertyValueFactory<Artist, String>("name"));
            nameCol.setPrefWidth(200.0);

            mainTableView.getColumns().setAll(idCol, nameCol);
            labelTopOnTable.setText("Artists list");
            bArtistAlbums.setDisable(false);
            bShowSelectedSongs.setDisable(false);
            artistOrAlbum = true;
            bShowSelectedSongs.setText("Artist songs");
            Tooltip tooltip = new Tooltip();
            tooltip.setText("Show Songs of Artist");
            bShowSelectedSongs.setTooltip(tooltip);
        }

        // ALBUMS COLUMNS

        else if (type == 2) {
            TableColumn idCol = new TableColumn("id");
            idCol.setCellValueFactory(new PropertyValueFactory<Album, Integer>("id"));
            idCol.setPrefWidth(50.0);
            TableColumn nameCol = new TableColumn("Name");
            nameCol.setCellValueFactory(new PropertyValueFactory<Album, String>("name"));
            nameCol.setPrefWidth(200.0);
            TableColumn artistIdCol = new TableColumn("Artist id");
            artistIdCol.setCellValueFactory(new PropertyValueFactory<Album, Integer>("artistId"));
            artistIdCol.setPrefWidth(50.0);
            TableColumn artistNameCol = new TableColumn("Artist name");
            artistNameCol.setCellValueFactory(new PropertyValueFactory<Album, String>("artistName"));
            artistNameCol.setPrefWidth(200.0);

            mainTableView.getColumns().setAll(idCol, nameCol, artistIdCol, artistNameCol);
            labelTopOnTable.setText("Albums list");
            bShowSelectedSongs.setDisable(false);
            bShowSelectedSongs.setText("Albums songs");
            Tooltip tooltip = new Tooltip();
            tooltip.setText("Show Songs of Album");
            bShowSelectedSongs.setTooltip(tooltip);
            bArtistAlbums.setDisable(true);
            artistOrAlbum = false;
        }

        // SONGS COLUMNS

        else {
            TableColumn idCol = new TableColumn("id");
            idCol.setCellValueFactory(new PropertyValueFactory<Song, Integer>("id"));
            idCol.setPrefWidth(50.0);
            TableColumn trackCol = new TableColumn("Track");
            trackCol.setCellValueFactory(new PropertyValueFactory<Song, Integer>("track"));
            trackCol.setPrefWidth(50.0);
            TableColumn titleCol = new TableColumn("Title");
            titleCol.setCellValueFactory(new PropertyValueFactory<Song, String>("title"));
            titleCol.setPrefWidth(200.0);
            TableColumn albumIdCol = new TableColumn("Album id");
            albumIdCol.setCellValueFactory(new PropertyValueFactory<Song, Integer>("albumId"));
            albumIdCol.setPrefWidth(70.0);
            TableColumn albumNameCol = new TableColumn("Album name");
            albumNameCol.setCellValueFactory(new PropertyValueFactory<Song, String>("albumName"));
            albumNameCol.setPrefWidth(200.0);

            mainTableView.getColumns().setAll(idCol, trackCol, titleCol, albumIdCol, albumNameCol);
            labelTopOnTable.setText("Songs list");
            bArtistAlbums.setDisable(true);
            bShowSelectedSongs.setDisable(true);
        }
    }

    private void notSelectError() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText("No selected item!");
        alert.setContentText("Please select item to see more.");
        alert.showAndWait();
    }

    private boolean areYouSure(String name, String actionType) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(actionType.toUpperCase());
        alert.setContentText("Are you sure " + actionType.toLowerCase() + " " + name);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && (result.get() == ButtonType.OK)) {
            return true;
        }
        return false;
    }

    private void fatalError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("FATAL ERROR");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean itIsIt(int id, String fieldText){
        try {
            if (id == Integer.parseInt(fieldText)){
                return true;
            }
            return false;
        }catch (NumberFormatException e){
            return false;
        }
    }

    private boolean itIsIt(String name, String fieldText){
        if (name.toLowerCase().trim().equals(fieldText.toLowerCase().trim())){
            return true;
        }
        return false;
    }
}
