package cookielounge.cookiegames.objects;
import com.github.cliftonlabs.json_simple.JsonException;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private String type;
    private String prompt;
    private int players;
    private boolean requiresWeapon;
    private ArrayList<Condition> conditions;

    public Event(){
        this.type = "";
        this.prompt = "";
        this.players = 0;
        this.requiresWeapon = false;
        this.conditions = new ArrayList<>();
    }

    public Event(String type, String prompt, int players, boolean requiresWeapon, ArrayList<Condition> conditions) {
        this.type = type;
        this.prompt = prompt;
        this.players = players;
        this.requiresWeapon = requiresWeapon;
        this.conditions = conditions;
    }

    public String getType() {
        return type;
    }

    public String getPrompt() {
        return prompt;
    }

    public int getPlayers() {
        return players;
    }

    public boolean requiresWeapon() {
        return requiresWeapon;
    }

    public ArrayList<Condition> getConditions() {
        return conditions;
    }

    // Check if all conditions for the event are satisfied
    public boolean checkConditions(ArrayList<Player> selectedPlayers, List<String> weapons) {
        for (Condition condition : conditions) {
            if (!condition.isSatisfied(selectedPlayers, weapons)) {
                return false;
            }
        }
        //check if needs weapon and the player has one
        if (requiresWeapon && !selectedPlayers.get(0).hasWeapon()) {
            return false;
        }
        return true;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public boolean isRequiresWeapon() {
        return requiresWeapon;
    }

    public void setRequiresWeapon(boolean requiresWeapon) {
        this.requiresWeapon = requiresWeapon;
    }

    public void setConditions(ArrayList<Condition> conditions) {
        this.conditions = conditions;
    }

    public void applyConditions(ArrayList<Player> selectedPlayers, String weapon) throws JsonException {
        for (Condition condition : conditions) {
            condition.apply(selectedPlayers, weapon);
            condition.setDone(false);
        }
    }
}
