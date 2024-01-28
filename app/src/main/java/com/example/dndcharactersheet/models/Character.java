package com.example.dndcharactersheet.models;

import java.lang.reflect.Array;
import java.util.Arrays;

public class Character {
    private long id;
    private String name;
    private String race;
    private String characterClass;
    private int level;
    private String[] abilityScores;//abilityScores su Strength, Dexterity, Constitution, Intelligence, Wisdom, Charisma
    private String inventory;

    //  TODO dodati jos stvari ako je potrebno

    public String[] getAbilityScores(){
        if (this.abilityScores == null){
            this.abilityScores = new String[6];
        }
        return abilityScores;
    }

    public String getAbilityScoresAsString(){
        return Arrays.toString(abilityScores);
    }

    public void setAbilityScores(String[] abilityScores) {
        this.abilityScores = abilityScores;
    }

    public void setAbilityScoresFromString(String abilityScoresString){
        if (abilityScoresString != null && !abilityScoresString.isEmpty()){

            String[] scoresArray = abilityScoresString.substring(1,abilityScoresString.length()-1).split(", ");
            String[] scores = new String[scoresArray.length];

            for (int i = 0; i <scoresArray.length; i++){
                scores[i] = scoresArray[i];
            }
            this.abilityScores = scores;
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getCharacterClass() {
        return characterClass;
    }

    public void setCharacterClass(String characterClass) {
        this.characterClass = characterClass;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

}
