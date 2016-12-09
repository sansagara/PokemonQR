package com.leonelatencio.pokedexqr.models;

public class Pokemon {

    private int id;
    private String number;
    private String name2;
    private String filename;
    private int shiny;
    private int sex;
    private String location;
    private String type;
    private String abilities;
    private String stats;
    private String notes;

    public int getId() {
        return id;
    }
    public void setId(int id) {
         this.id = id;
    }
    public String getNumber() {
        return number;
    }
    public String getName() {
        return name2;
    }
    public String getFilename() {
        return filename;
    }
    public String getType() {
        return type;
    }
    public String getLocation() {
        return location;
    }
    public String getStats() {
        return stats;
    }
    public String getNotes() {
        return notes;
    }
    public String getType1() {
        if (type.indexOf('/') > 0) {
            return type.split("/")[0];
        } else {
            return type;
        }
    }
    public String getType2() {
        if (type.indexOf('/') > 0) {
            return type.split("/")[1];
        } else {
            return null;
        }
    }
    public int getShiny() {
        return shiny;
    }
    public int getSex() {
        return sex;
    }
    public String getAbilities() {
        return abilities;
    }
}
