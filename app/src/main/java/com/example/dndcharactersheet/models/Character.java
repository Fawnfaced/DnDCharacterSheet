package com.example.dndcharactersheet.models;

public class Character {
    private long id;
    private String name;
    private String race;
    private String characterClass;
    private int level;
    private int hp;
    private int[] abilityScores; //abilityScores are Strength, Dexterity, Constitution, Wisdom, Intelligence, Charisma
    private String inventory;
    private String hitDice;

    //  TODO dodati jos stvari ako je potrebno

    public int[] getAbilityScores(){
        return abilityScores;
    }

    public void setAbilityScores(int[] abilityScores) {
        this.abilityScores = abilityScores;
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

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public String getHitDice() {
        return hitDice;
    }

    public void setHitDice(String hitDice) {
        this.hitDice = hitDice;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

}
