package com.example.dndcharactersheet.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.dndcharactersheet.models.Character;

import java.util.ArrayList;
import java.util.List;

public class CharacterDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "character_db";
    private static final int DATABASE_VERSION = 6;

    public static final String TABLE_CHARACTERS = "characters";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_RACE = "race";
    public static final String COLUMN_CLASS = "class";
    public static final String COLUMN_LEVEL = "level";
    public static final String COLUMN_ABILITY_SCORES = "ability_scores";
    // TODO: dodati jos potrebnih informacija


    private static final String CREATE_TABLE_CHARACTERS =
            "CREATE TABLE " + TABLE_CHARACTERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_RACE + " TEXT, " +
                    COLUMN_CLASS + " TEXT, " +
                    COLUMN_LEVEL + " INTEGER, " +
                    COLUMN_ABILITY_SCORES + " TEXT);";

    public CharacterDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CHARACTERS);
    }

    public long addCharacter(Character character){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, character.getName());
        values.put(COLUMN_RACE, character.getRace());
        values.put(COLUMN_CLASS, character.getCharacterClass());
        values.put(COLUMN_LEVEL, character.getLevel());
        values.put(COLUMN_ABILITY_SCORES, character.getAbilityScoresAsString());

        //TODO: Ostalo

        long characterId = db.insert(TABLE_CHARACTERS, null, values);
        db.close();

        return characterId;
    }

    public List<Character> getAllCharacters(){
        //dostavlja listu svih karaktera iz baze podataka
        List<Character> characterList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CHARACTERS, null);

        if (cursor != null && cursor.moveToFirst()){
            do {
                Character character = new Character();
                character.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                character.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                character.setRace(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RACE)));
                character.setCharacterClass(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLASS)));
                character.setLevel(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LEVEL)));
                character.setAbilityScoresFromString(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ABILITY_SCORES)));

                characterList.add(character);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return characterList;
    }

    public Character getCharacter(long characterId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_CHARACTERS,
                null,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(characterId)},
                null,
                null,
                null
        );

        Character character = null;

        if (cursor != null && cursor.moveToFirst()) {
            character = new Character();
            character.setId(cursor.getColumnIndex(COLUMN_ID));
            character.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
            character.setRace(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RACE)));
            character.setCharacterClass(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLASS)));
            character.setLevel(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LEVEL)));
            character.setAbilityScoresFromString(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ABILITY_SCORES)));
            cursor.close();
        }

        return character;

    }

    public int updateCharacter(Character character) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, character.getName());
        values.put(COLUMN_RACE, character.getRace());
        values.put(COLUMN_CLASS, character.getCharacterClass());
        values.put(COLUMN_LEVEL, character.getLevel());
        values.put(COLUMN_ABILITY_SCORES, character.getAbilityScoresAsString());
        // TODO: ostalo

        return db.update(TABLE_CHARACTERS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(character.getId())});

    }

    public boolean deleteCharacter(long characterId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_CHARACTERS, COLUMN_ID + " = ?", new String[]{String.valueOf(characterId)});
        db.close();
        return result > 0;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHARACTERS);
        onCreate(db);

    }
}
