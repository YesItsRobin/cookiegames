package cookielounge.cookiegames.objects;

import java.util.ArrayList;
import java.util.Map;

public class Player {
    private final String name;
    private final int district;
    private boolean alive = true;
    private ArrayList<Player> alliance;
    private ArrayList<String> weapons;
    private int deathDate = -1;
    private String color ="black";


    public Player(String name, int district) {
        this.name = name;
        this.district = district;
        this.alliance = new ArrayList<>();
        this.weapons = new ArrayList<>();

    }

    public void addWeapon(String weapon) {
        weapons.add(weapon);
    }

    public void removeWeapon(String weapon) {
        weapons.remove(weapon);
    }

    public boolean hasWeapon() {
    return !weapons.isEmpty();
    }

    public boolean hasWeapon(String weapon) {
        return weapons.contains(weapon);
    }

    public void addAlly(Player player) {
        alliance.add(player);
    }

    public void removeAlly(Player player) {
        this.setColor("black");
        alliance.remove(player);
    }

    public Player clone() {
        String newName = name + "2";
        return new Player(newName,  district);
    }

    public String getName() {
        return name;
    }

    public int getDistrict() {
        return district;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public ArrayList<Player> getAlliance() {
        return alliance;
    }

    public ArrayList<String> getWeapons() {
        return weapons;
    }

    public String getWeaponsList() {
        StringBuilder weaponList = new StringBuilder();
        for (String weapon : weapons) {
            weaponList.append(weapon).append(", ");
        }
        //remove the last comma
        if (weaponList.length() > 2){
            weaponList.deleteCharAt(weaponList.length()-2);
        }
        return weaponList.toString();
    }

    public int getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(int deathDate) {
        this.deathDate = deathDate;
    }
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
