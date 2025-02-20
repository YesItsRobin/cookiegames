package cookielounge.cookiegames;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //FXML loader to start a window with the first fxml, start is the opening screen.
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("start.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("COOKIES!!!!!!!!!!");
        //Stage is the window we work with, the scene is the specific ui we want
        stage.setScene(scene);
        //set icon of window, images are found in the resources folder in images folder
        stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResource("/images/cookie.png")).toExternalForm()));
        //opens the window
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
