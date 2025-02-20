package cookielounge.cookiegames.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;

public class StartController {
    public Rectangle colorblock;
    public AnchorPane background;
    public ImageView startImage;
    public Button startButton;
    @FXML
    private Label welcomeText;

    @FXML
    protected void StartApp() throws IOException {
        //Change to the next scene. A scene is a window in JavaFX
        //The scene is changed by changing the root of the stage
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cookielounge/cookiegames/startup.fxml"));        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        Stage stage = (Stage) startButton.getScene().getWindow();
        stage.setTitle("Cookies?");
        stage.setScene(scene);
        stage.show();
    }

    public void initialize() {


        colorblock.setFill(Paint.valueOf("#F5DAD2"));
        colorblock.widthProperty().bind(background.widthProperty().subtract(40)); // subtracting 40 to account for the AnchorPane's padding
        colorblock.heightProperty().bind(background.heightProperty().subtract(40)); // subtracting 40 to account for the AnchorPane's padding

    }
}