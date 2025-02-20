package cookielounge.cookiegames.main;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import cookielounge.cookiegames.objects.Event;
import cookielounge.cookiegames.objects.Player;
import cookielounge.cookiegames.objects.Condition;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    private static Game instance;
    private ArrayList<Event> events;
    private ArrayList<Player> players;
    private int day = 0;
    private int maxEvents = 5;
    private ArrayList<String> weapons = new ArrayList<>();
    private ArrayList<Player> playersToDoToday;
    private ArrayList<String> specialDays = new ArrayList<>();
    private ArrayList<Player> deadPlayers = new ArrayList<>();
    public String dayType = "normal";
    private int deathsToday = 0;


    public String getEvent() throws JsonException {
        //check if only one player is left
        ArrayList<Event> possibleEvents = new ArrayList<>();
        // Filter events based on the current day and other criteria
        for (Event event : events) {
            if (event.getType().equals("cornucopia") && day == 1) {
                possibleEvents.add(event);
            }
            if (!dayType.equals("normal")) {
                if (event.getType().equals(this.dayType)) {
                    possibleEvents.add(event);
                }
            }
            else{
                if (event.getType().equals("survival")|event.getType().equals("confrontation")) {
                    possibleEvents.add(event);
                }
            }
            }


        Collections.shuffle(possibleEvents); // Shuffle for random selection

        for (Event event : possibleEvents) {
            System.out.println("Checking event: " + event.getPrompt());
            System.out.println("Requires weapon: " + event.requiresWeapon());

            ArrayList<Player> selectedPlayers = new ArrayList<>();
            List<Integer> usedIndices = new ArrayList<>();

            // Selecting the required number of unique, alive players for the event
            while (selectedPlayers.size() < event.getPlayers() && usedIndices.size() < playersToDoToday.size()) {
                int index = (int) (Math.random() * playersToDoToday.size());
                Player player = playersToDoToday.get(index);

                // Check if the player is alive and not already selected
                if (player.isAlive() && !usedIndices.contains(index)) {
                    selectedPlayers.add(player);
                    usedIndices.add(index);
                }
            }
            boolean validEvent = true;
            // Ensure we have enough players for the event
            if (selectedPlayers.size() < event.getPlayers()) {
                System.out.println("Not enough players");
                continue; // Not enough players
            }

            // no more than 3 deaths in one day

            //if the event has a condition that is of type life, and the condition is dead and there has been 3 deaths today, skip the event
            for (Condition condition : event.getConditions()) {
                if (condition.getType().equals("life")) {
                    if (condition.getCondition().equals("dead")) {
                        if (deathsToday>=3){
                            System.out.println("Too many deaths today");
                            validEvent = false;
                        }
                        else{
                            deathsToday++;

                        }
                    }
                }
            }

            // Check if the conditions of the event are satisfied
            if (!event.checkConditions(selectedPlayers, weapons)) {
                System.out.println("Conditions not met");
                continue; // Conditions not met
            }
            boolean canKill = true;
            //people in an alliance can't kill each other
            if (selectedPlayers.size()>1){
                if (selectedPlayers.get(0).getAlliance().contains(selectedPlayers.get(1))){

                    System.out.println("Allied players");
                    for (Condition condition : event.getConditions()) {
                        if (condition.getType().equals("life")) {
                            if (condition.getCondition().equals("dead")) {
                                System.out.println("Allied players can't kill each other");
                                validEvent = false;
                            }

                        }
                    }
                }
            }

            if (!validEvent){
                continue;
            }
            // Apply the event conditions
            String weapon ="";
            //if there's a _w in the prompt, the event requires a weapon. choose it randomly from the list of weapons of the player
            if (event.requiresWeapon()) {
                weapon = selectedPlayers.getFirst().getWeapons().get((int) (Math.random() * selectedPlayers.getFirst().getWeapons().size()));
            }
            //if there's a _nw in the prompt, the event requires a weapon. choose it randomly from the list of weapons of the player
            for (Condition condition : event.getConditions()) {
                if (condition.hasNewWeapon()) {
                    //get a random weapon from the list of weapons
                    weapon = weapons.get((int) (Math.random() * weapons.size()));
                }
            }
            //if needsweapon is true, and there's a new weapon the weapon moves to the second player
            if (event.requiresWeapon()) {
                for (Condition condition : event.getConditions()) {
                    if (condition.hasNewWeapon()) {
                        selectedPlayers.get(1).addWeapon(weapon);
                        selectedPlayers.get(0).removeWeapon(weapon);
                        break;
                    }
                }
            }
            event.applyConditions(selectedPlayers, weapon);
            playersToDoToday.remove(selectedPlayers.getFirst()); // Remove the selected players from the list of players to do events today

            return formatEvent(event, selectedPlayers, weapon); // Execute and format the event
        }

        return "No valid event found";
    }

    private String formatEvent(Event event, ArrayList<Player> players, String weapon) {
        String result = event.getPrompt();
        for (int i = 0; i < players.size(); i++) {
            result = result.replace("_" + (i + 1), players.get(i).getName());
        }
        result = result.replace("_w", weapon);
        result = result.replace("_nw", weapon);

        return result;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMaxEvents() {
        return maxEvents;
    }

    public void setMaxEvents(int maxEvents) {
        this.maxEvents = maxEvents;
    }

    public void nextDay() {
        day++;
        playersToDoToday.clear();
        playersToDoToday.addAll(players);
        deathsToday = 0;
        if (day>2 & (int) (Math.random() * 5) == 1) {
            System.out.println("Special day");

            //Choose between the special days
            String specialDay = specialDays.get((int) (Math.random() * specialDays.size()));
            this.dayType = specialDay;
        }
        else{
            this.dayType = "normal";
        }
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }
    public void addPlayers(ArrayList<Player> players) {
        this.players.addAll(players);
    }

    private Game() throws JsonException {
        playersToDoToday = new ArrayList<>();
        specialDays.add("special-cookieparty");
        specialDays.add("special-rat invation");
        specialDays.add("special-comic con");

        JsonObject jsonObjectEvents = (JsonObject) Jsoner.deserialize(new InputStreamReader(getClass().getResourceAsStream("/text/events.json")));
        events = new ArrayList<>();
        for (Object event : (List<Object>) jsonObjectEvents.get("events")) {
            Event newEvent = new Event();
            newEvent.setType((String) ((JsonObject) event).get("type"));
            newEvent.setPrompt((String) ((JsonObject) event).get("prompt"));
            Number playersNumber = (Number) ((JsonObject) event).get("players");
            newEvent.setPlayers(playersNumber.intValue());
            newEvent.setRequiresWeapon((boolean) ((JsonObject) event).get("requires weapon"));

            ArrayList<Condition> conditions = new ArrayList<>();
            if (((JsonObject) event).containsKey("conditions")) {
                for (Object condition : (List<Object>) ((JsonObject) event).get("conditions")) {
                    Condition newCondition = new Condition();
                    newCondition.setType((String) ((JsonObject) condition).get("type"));
                    List<Number> playersNumberList = (List<Number>) ((JsonObject) condition).get("players");
                    newCondition.setPlayerIndexes(playersNumberList.stream().mapToInt(Number::intValue).toArray());
                    newCondition.setCondition((String) ((JsonObject) condition).get("condition"));
                    conditions.add(newCondition);
                }
            }
            newEvent.setConditions(conditions);
            if (newEvent.requiresWeapon()){
                System.out.println("Event requires weapon");
            }
            events.add(newEvent);
        }

        //LoadResource loader = new LoadResource();
        //JsonObject jsonObjectWeapons = loader.loadResource("text\\weapons.json");
        JsonObject jsonObjectWeapons = (JsonObject) Jsoner.deserialize(new InputStreamReader(getClass().getResourceAsStream("/text/weapons.json")));
        for (Object weapon : (List<Object>) jsonObjectWeapons.get("weapons")) {
                weapons.add((String) weapon);
            }
    }

        public static Game getInstance() throws JsonException {
        if (instance == null) {
            instance = new Game();
            instance.players = new ArrayList<>();
        }
        return instance;
    }

    public ArrayList getPlayersToDoToday() {
        return playersToDoToday;
    }

    public void removePlayer(Player player){
        players.remove(player);
    }

    public void killPlayer(Player player){
        player.setAlive(false);
        player.setDeathDate(this.getDay());
        this.removePlayer(player);
        //if the player is in todays list, remove it
        if (playersToDoToday.contains(player)){
            playersToDoToday.remove(player);
        }
        //remove alliances
        for (Player player1 : player.getAlliance()){
            player1.removeAlly(player);
        }
        removePlayer(player);
        deadPlayers.add(player);
    }

    public void restart() {
        day = 0;
        players.addAll(deadPlayers);
        deadPlayers.clear();
        playersToDoToday.clear();
        for (Player player : players) {
            player.setAlive(true);
            player.setDeathDate(-1);
            player.getWeapons().clear();
        }
    }

    public ArrayList<Player> getDeadPlayers() {
        return deadPlayers;
    }


}
