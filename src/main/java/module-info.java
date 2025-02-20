module cookielounge.cookiegames {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.jsobject;
    requires json.simple;


    opens cookielounge.cookiegames to javafx.fxml;
    exports cookielounge.cookiegames;
    exports cookielounge.cookiegames.controller;
    opens cookielounge.cookiegames.controller to javafx.fxml;
    exports cookielounge.cookiegames.main;
    opens cookielounge.cookiegames.main to javafx.fxml;
}