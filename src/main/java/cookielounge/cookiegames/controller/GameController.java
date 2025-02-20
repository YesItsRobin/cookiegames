package cookielounge.cookiegames.controller;

import cookielounge.cookiegames.main.Game;
import cookielounge.cookiegames.objects.Player;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import java.util.ArrayList;

public class GameController {

    public Rectangle backgroundRectangle;
    public FlowPane eventList;
    public Button nextButton;
    public Text dayText;
    public Button restartButton;
    public boolean intermission = false;

    public void initialize() {
        addEvent("Day 1: The game has started");
        eventList.setVgap(40); // Change this value to increase or decrease the gap
        eventList.setOrientation(Orientation.VERTICAL);


        dayText.setText("Day " + Game.getInstance().getDay());
        //fill the event list with players and their districts
        ArrayList< Player> players = Game.getInstance().getPlayers();
        //sort on district ascending
        players.sort((p1, p2) -> p1.getDistrict() - p2.getDistrict());

        for (Player player : players) {
            addEvent(player.getName() + " from district " + player.getDistrict());
        }

        Platform.runLater(() -> {
            Scene scene = backgroundRectangle.getScene();
            backgroundRectangle.widthProperty().bind(scene.widthProperty().subtract(80));
            backgroundRectangle.heightProperty().bind(scene.heightProperty().subtract(80));
            eventList.prefWidthProperty().bind(scene.widthProperty().subtract(800));
            eventList.prefHeightProperty().bind(scene.heightProperty().subtract(800));
            eventList.setHgap(10);
        });
    }

    public void addEvent(String event) {
        this.addEvent(event, "black");
    }
    public void addEvent(String event, String color) {
        Text text = new Text(event);
        text.setStyle("-fx-font-size: xx-large; -fx-fill: " + color);
        text.setTextAlignment(TextAlignment.CENTER); // Center the text within its own bounds
        text.setWrappingWidth(eventList.getPrefWidth() - 100); // Set the wrapping width to the preferred width of the FlowPane minus some padding

        eventList.getChildren().add(text);
    }
    public void addEvent(String event, String color, String size) {
        Text text = new Text(event);
        text.setStyle("-fx-font-size: "+size+"; -fx-fill: " + color);
        text.setTextAlignment(TextAlignment.CENTER); // Center the text within its own bounds
        text.setWrappingWidth(eventList.getPrefWidth() - 400); // Set the wrapping width to the preferred width of the FlowPane minus some padding

        eventList.getChildren().add(text);
    }

    public void next(ActionEvent actionEvent) {
        //check if the playerstodo list is empty
        if (Game.getInstance().getPlayers().size() <=1){
            //end the game
            eventList.getChildren().clear();
            addEvent("Game Over");
            addEvent("The winner is: " + Game.getInstance().getPlayers().getFirst().getName());
            restartButton.setVisible(true);

        }
        else if (intermission | Game.getInstance().getDay()==0){
            intermission = false;
            Game.getInstance().nextDay();
            if (Game.getInstance().dayType.equals("normal")){
                dayText.setText("Day " + Game.getInstance().getDay());
            }
            else {
                dayText.setText(Game.getInstance().dayType);
            }

            //empty the event list
            eventList.getChildren().clear();
        }
        else if (Game.getInstance().getPlayersToDoToday().isEmpty() & !intermission & Game.getInstance().getDay()!=0){
            intermission = true;
            eventList.getChildren().clear();

            addEvent("Deaths today:");
            for (Player player : Game.getInstance().getDeadPlayers() ){
                if (!player.isAlive() & player.getDeathDate()==Game.getInstance().getDay()){
                    addEvent(player.getName() + " from district " + player.getDistrict(), "large");
                }
            }
            addEvent("Remaining players:");
            for (Player player : Game.getInstance().getPlayers() ){
                if (player.isAlive()){
                    addEvent(player.getName() + " from district " + player.getDistrict() + "\n Items: " + player.getWeaponsList(), player.getColor(), "large");
                }
            }
            intermission = true;
        }
        else{
            //if 10 events have been shown, clear screen
            if (eventList.getChildren().size() > 6){
                eventList.getChildren().clear();
            }
            String event = Game.getInstance().getEvent();
            addEvent(event);
        }


    }

    public void restart(ActionEvent actionEvent) {
        Game.getInstance().restart();
        eventList.getChildren().clear();
        dayText.setText("Start of new game");
        restartButton.setVisible(false);
    }
}
