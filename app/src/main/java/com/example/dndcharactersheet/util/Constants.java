package com.example.dndcharactersheet.util;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static final int REQUEST_EDIT_CHARACTER=101;
    public static final String CHARACTER_ID_KEY = "characterId";
    public static final String EDIT_MODE_KEY = "editMode";

    public static final int ABILITY_SCORE_MIN = 1;
    public static final int ABILITY_SCORE_MAX = 24;

    public static final int SPLASH_TIME_OUT=1600;
    public static final long CLICK_INTERVAL = 650;

    public static final String[] CLASSES = {"Barbarian","Bard", "Cleric", "Druid", "Fighter", "Monk",
            "Paladin", "Ranger", "Rogue", "Sorcerer","Warlock", "Wizard"};

    public static final Map<String, Integer> CLASSES_HP;
    static{
        CLASSES_HP = new HashMap<>();
        CLASSES_HP.put("Barbarian", 12);
        CLASSES_HP.put("Bard", 8);
        CLASSES_HP.put("Cleric", 8);
        CLASSES_HP.put("Druid", 8);
        CLASSES_HP.put("Fighter", 10);
        CLASSES_HP.put("Monk", 8);
        CLASSES_HP.put("Paladin", 10);
        CLASSES_HP.put("Ranger", 10);
        CLASSES_HP.put("Rogue", 8);
        CLASSES_HP.put("Sorcerer", 6);
        CLASSES_HP.put("Warlock", 8);
        CLASSES_HP.put("Wizard", 6);
    }

}
