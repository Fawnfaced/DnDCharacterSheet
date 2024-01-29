package com.example.dndcharactersheet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dndcharactersheet.database.CharacterDatabaseHelper;
import com.example.dndcharactersheet.models.Character;
import com.example.dndcharactersheet.util.Constants;

import java.util.Locale;

public class ViewCharacterDetailsActivity extends AppCompatActivity {
    private TextView textViewName, textViewRace, textViewCharacterClass,textViewLevel,textViewHP,textViewHD;
    private TextView textViewStr, textViewDex, textViewCon, textViewInt, textViewWis, textViewCha;
    private TextView textViewStrMod, textViewDexMod, textViewConMod, textViewIntMod, textViewWisMod, textViewChaMod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_character_details);

        long characterId = getIntent().getLongExtra(Constants.CHARACTER_ID_KEY, -1);
        //inicijalizacija textView
        textViewName = findViewById(R.id.textViewName);
        textViewRace = findViewById(R.id.textViewRace);
        textViewCharacterClass = findViewById(R.id.textViewCharacterClass);
        textViewLevel = findViewById(R.id.textViewLevel);
        textViewHP = findViewById(R.id.textViewHP);
        textViewHD = findViewById(R.id.textViewHD);

        textViewStr = findViewById(R.id.textViewStr);
        textViewDex = findViewById(R.id.textViewDex);
        textViewCon = findViewById(R.id.textViewCon);
        textViewInt = findViewById(R.id.textViewInt);
        textViewWis = findViewById(R.id.textViewWis);
        textViewCha = findViewById(R.id.textViewCha);

        textViewStrMod = findViewById(R.id.textViewStrMod);
        textViewDexMod = findViewById(R.id.textViewDexMod);
        textViewConMod = findViewById(R.id.textViewConMod);
        textViewIntMod = findViewById(R.id.textViewIntMod);
        textViewWisMod = findViewById(R.id.textViewWisMod);
        textViewChaMod = findViewById(R.id.textViewChaMod);

        Button btnEditCharacter = findViewById(R.id.btnEditCharacter);
        btnEditCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditCharacterActivity(characterId);
            }
        });

        updateUI(characterId);

    }
    private void updateUI(long characterId){
        // poziva se da bi se update ui
        Character character = getCharacterFromDatabase(characterId);

        if (character != null && character.getAbilityScores() != null) {
            textViewName.setText(String.format("Name: %s", character.getName()));
            textViewRace.setText(String.format("Race: %s", character.getRace()));
            textViewCharacterClass.setText(String.format("Class: %s", character.getCharacterClass()));
            textViewLevel.setText(String.format(Locale.US, "Level: %d", character.getLevel()));

            textViewStr.setText(String.format("%s", character.getAbilityScores()[0]));
            textViewDex.setText(String.format("%s", character.getAbilityScores()[1]));
            textViewCon.setText(String.format("%s", character.getAbilityScores()[2]));
            textViewInt.setText(String.format("%s", character.getAbilityScores()[3]));
            textViewWis.setText(String.format("%s", character.getAbilityScores()[4]));
            textViewCha.setText(String.format("%s", character.getAbilityScores()[5]));

            textViewStrMod.setText(String.format("%s", modifierFromAbilityScore(character.getAbilityScores()[0])));
            textViewDexMod.setText(String.format("%s", modifierFromAbilityScore(character.getAbilityScores()[1])));
            textViewConMod.setText(String.format("%s", modifierFromAbilityScore(character.getAbilityScores()[2])));
            textViewIntMod.setText(String.format("%s", modifierFromAbilityScore(character.getAbilityScores()[3])));
            textViewWisMod.setText(String.format("%s", modifierFromAbilityScore(character.getAbilityScores()[4])));
            textViewChaMod.setText(String.format("%s", modifierFromAbilityScore(character.getAbilityScores()[5])));

            textViewHP.setText(String.format(Locale.US, "%d", hPFromClasses(character.getCharacterClass(),
                    character.getLevel(),character.getAbilityScores()[2])));

            textViewHD.setText(String.format(Locale.US, "%dd%s",character.getLevel(), Constants.CLASSES_HP.get(character.getCharacterClass())));

            // TODO: ostalo
        } else {
            Toast.makeText(this, "Character not found", Toast.LENGTH_SHORT).show();
        }

    }
    private int hPFromClasses(String characterClass, int level, String constitutionString){
        int hp = 0;
        //hp = (classHP/2)*(level-1)+ classHP + conMod*leve
        String constitutionModString = modifierFromAbilityScore(constitutionString);
        int constitutionMod = Integer.parseInt(constitutionModString.replaceAll("[^\\d.]",""));
            if (Constants.CLASSES_HP.containsKey(characterClass)){
                hp = (int)Math.ceil((float)Constants.CLASSES_HP.get(characterClass)*(level-1)/2) +
                        Constants.CLASSES_HP.get(characterClass) + constitutionMod * level;
            }
            if (hp<=0){
                hp = 1;
            }

        return hp;
    }

    private String modifierFromAbilityScore(String abilityScore){
        String modifier;
        int modFromAScore;
        modFromAScore = (Integer.parseInt(abilityScore)-10)/2;
        if (modFromAScore >0){
            modifier = "+" + modFromAScore;
        } else {
            modifier = String.valueOf(modFromAScore);
        }

        return modifier;
    }

    private Character getCharacterFromDatabase(long characterId){
        CharacterDatabaseHelper databaseHelper = new CharacterDatabaseHelper(this);
        return databaseHelper.getCharacter(characterId);

    }

    private void openEditCharacterActivity(long characterId){
        Intent intent = new Intent(ViewCharacterDetailsActivity.this, CreateCharacterActivity.class);
        intent.putExtra(Constants.EDIT_MODE_KEY,true);//oznacava rezim editovanja
        intent.putExtra(Constants.CHARACTER_ID_KEY, characterId);
        startActivityForResult(intent, Constants.REQUEST_EDIT_CHARACTER);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_EDIT_CHARACTER && resultCode == RESULT_OK){
            //ako je rezultat ok, azurira UI
            long characterId = getIntent().getLongExtra(Constants.CHARACTER_ID_KEY, -1);
            Intent updateIntent = new Intent();

            updateIntent.putExtra("updatedCharacterId", characterId);
            setResult(RESULT_OK,updateIntent);


            updateUI(characterId);
        }
    }

}
