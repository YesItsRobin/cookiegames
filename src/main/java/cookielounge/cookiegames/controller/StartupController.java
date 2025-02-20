package cookielounge.cookiegames.controller;

import com.github.cliftonlabs.json_simple.JsonException;
import cookielounge.cookiegames.main.Game;
import cookielounge.cookiegames.objects.Player;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.util.ArrayList;
import java.util.Arrays;

public class StartupController {
    public TextField nrOfPeopleInput;
    public Rectangle backgroundRectangle;
    public CheckBox teamsBox;
    public Button listButton;
    public FlowPane peopleList;


    public void initialize() {
        //Initialize the nrOfPeopleInput textfield
        nrOfPeopleInput.setPromptText("Number of people");
        //max 19 people in one column, so when there are more, it will go to the next column
        peopleList.setVgap(30);
        peopleList.setOrientation(Orientation.VERTICAL);


        // Add a listener to the textfield to only allow numbers under 45
        nrOfPeopleInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                nrOfPeopleInput.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (newValue.length() > 2) {
                nrOfPeopleInput.setText(oldValue);
            }
        });

        nrOfPeopleInput.setText("2");

        // Set the horizontal gap between elements in the FlowPane
        peopleList.setHgap(10);
        // Set the vertical gap between elements in the FlowPane
        peopleList.setVgap(10);





        //Resize the window to size of the screen
        Platform.runLater(() -> {
            Scene scene = nrOfPeopleInput.getScene();
            Stage stage = (Stage) scene.getWindow();
            stage.setMaximized(true);

            backgroundRectangle.widthProperty().bind(scene.widthProperty().subtract(80));
            backgroundRectangle.heightProperty().bind(scene.heightProperty().subtract(80));

        });
    }

    public void updateList(ActionEvent actionEvent) {
        int nrOfPeople = Integer.parseInt(nrOfPeopleInput.getText());
        if (nrOfPeople < 1) {
            return;
        }
        if (!peopleList.getChildren().isEmpty()) {
            peopleList.getChildren().clear();
        }
        for (int i = 0; i < nrOfPeople; i++) {
            //Add a textfield for each person's name and a dropdown for the pronouns
            TextField textField = new TextField();
            textField.setPromptText("Name");
            textField.setMinWidth(200);
            textField.setMinHeight(30);

            //Put the text in the peopleList
            peopleList.getChildren().add(textField);

            //for testing purposes, add some names
            textField.setText("Alice"+i);


        }
        peopleList.setVisible(true);

    }

    public void goClick(ActionEvent actionEvent) throws JsonException {
        //First, check if everything is filled in correctly
        int disctricts;
        ArrayList<Integer> districtList = new ArrayList<>();
        if (nrOfPeopleInput.getText().isEmpty()) {
            return;
        }
        int nrOfPeople = Integer.parseInt(nrOfPeopleInput.getText());
        if (teamsBox.isSelected()) {
            disctricts = nrOfPeople / 2;
            for (int i = 1; i < disctricts+1; i++) {
                districtList.add(i);
                districtList.add(i);
            }

        } else {
            disctricts = nrOfPeople;
            for (int i = 1; i < disctricts+1; i++) {
                districtList.add(i);
            }
        }
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < peopleList.getChildren().size(); i++) {
            TextField textField = (TextField) peopleList.getChildren().get(i);
            if (textField.getText().isEmpty()) {
                break;
            }
            //Get a random district
            int district = districtList.remove((int) (Math.random() * districtList.size()));
            players.add(new Player(textField.getText(), district));
        }

        //Now we have all the players
        Game game = Game.getInstance();
        game.addPlayers(players);

        Stage stage = (Stage) listButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cookielounge/cookiegames/game.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(true); // Change this line
        stage.show();
    }
}
