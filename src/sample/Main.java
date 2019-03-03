package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.database.DatabaseManager;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("My Database Manager Example");
        primaryStage.setScene(new Scene(root, 800, 400));
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();
        DatabaseManager.getInstance().open();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        DatabaseManager.getInstance().close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
