package cookielounge.cookiegames.objects;
import cookielounge.cookiegames.main.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Condition {
    private String type;
    private int[] playerIndexes;
    private String condition;
    private static Random random = new Random();
    private boolean done = false;

    public Condition() {
        this.type = "";
        this.playerIndexes = new int[0];
        this.condition = "";
    }

    public Condition(String type, int[] playerIndexes, String condition) {
        this.type = type;
        this.playerIndexes = playerIndexes;
        this.condition = condition;
    }


    // Checks if the condition is satisfied based on the type and specific condition
    public boolean isSatisfied(List<Player> players, List<String> weapons) {
        switch (type) {
            case "weapon":
                return handleWeaponCondition(players, weapons);
            case "life":
                return handleLifeCondition(players);
            case "alliance":
                return handleAllianceCondition(players);
            default:
                return false;
        }
    }

    private boolean handleWeaponCondition(List<Player> players, List<String> weapons) {
        for (int index : playerIndexes) {
            Player player = players.get(index - 1);
            switch (condition) {
                case "lost":
                    if (player.getWeapons().isEmpty()) return false;
                    break;
            }
        }
        return true;
    }

    private boolean handleLifeCondition(List<Player> players) {
        for (int index : playerIndexes) {
            Player player = players.get(index - 1);
            switch (condition) {
                case "dead":
                    if (!player.isAlive()) return false;
                    break;
            }
        }
        return true;
    }

    private boolean handleAllianceCondition(List<Player> players) {
        Player player1 = players.get(playerIndexes[0] - 1);
        Player player2 = players.get(playerIndexes[1] - 1);
        switch (condition) {
            case "start":
                if (player1.getAlliance().contains(player2) || player2.getAlliance().contains(player1)) {
                    return false; // Already allied
                }
                break;
            case "broken":
                if (!player1.getAlliance().contains(player2) || !player2.getAlliance().contains(player1)) {
                    return false; // Not allied
                }
                break;
            case "active":
                return player1.getAlliance().contains(player2) && player2.getAlliance().contains(player1);
        }
        return true;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int[] getPlayerIndexes() {
        return playerIndexes;
    }

    public void setPlayerIndexes(int[] playerIndexes) {
        this.playerIndexes = playerIndexes;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public static Random getRandom() {
        return random;
    }

    public static void setRandom(Random random) {
        Condition.random = random;
    }

    public void apply(ArrayList<Player> selectedPlayers, String weapon) {
        if (done) return;
        done = true;
        if (Objects.equals(this.getType(), "alliance")) {
            Player player1 = selectedPlayers.get(playerIndexes[0] - 1);
            Player player2 = selectedPlayers.get(playerIndexes[1] - 1);
            switch (condition) {
                case "start":
                    player1.addAlly(player2);
                    player2.addAlly(player1);
                    player1.setColor(generateUniqueColor());
                    player2.setColor(player1.getColor());
                    break;
                case "broken":
                    player1.removeAlly(player2);
                    player2.removeAlly(player1);
                    player1.setColor("black");
                    player2.setColor("black");
                    break;

            }
            return;
        }
        for (int index : playerIndexes) {
            Player player = selectedPlayers.get(index - 1);
            switch (type) {
                case "weapon":
                    switch (condition) {
                        case "new":
                            player.addWeapon(weapon);
                            break;
                        case "lost":
                            if (player.hasWeapon(weapon)) {
                                // Remove the specific weapon from the player
                                player.removeWeapon(weapon);
                            }
                            break;
                    }
                    break;
                case "life":
                    switch (condition) {
                        case "dead":
                            Game.getInstance().killPlayer(player);
                            break;
                    }
                    break;

            }
        }
    }

    private String generateUniqueColor() {
        ArrayList<String> colors = new ArrayList<>();
        //add dark colors
        colors.add("darkred");
        colors.add("darkblue");
        colors.add("darkgreen");
        colors.add("darkorange");
        colors.add("darkcyan");
        colors.add("darkmagenta");
        colors.add("darkviolet");
        colors.add("darkturquoise");

        //remove the colors that are already in use
        for (Player player : Game.getInstance().getPlayers()) {
            colors.remove(player.getColor());
        }


        //return a random color from the list
        return colors.get(random.nextInt(colors.size()));
    }

    public boolean hasNewWeapon(){
        //if the condition is a weapon condition and the condition is new
        return type.equals("weapon") && condition.equals("new");
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}

