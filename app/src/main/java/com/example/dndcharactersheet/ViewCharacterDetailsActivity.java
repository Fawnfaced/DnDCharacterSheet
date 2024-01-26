package com.example.dndcharactersheet;

import static com.example.dndcharactersheet.util.Constants.CHARACTER_ID_KEY;
import static com.example.dndcharactersheet.util.Constants.EDIT_MODE_KEY;
import static com.example.dndcharactersheet.util.Constants.REQUEST_EDIT_CHARACTER;

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

public class ViewCharacterDetailsActivity extends AppCompatActivity {
    private TextView textViewName, textViewRace, textViewCharacterClass,textViewLevel;
    private Button btnEditCharacter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_character_details);

        long characterId = getIntent().getLongExtra(CHARACTER_ID_KEY, -1);

        textViewName = findViewById(R.id.textViewName);
        textViewRace = findViewById(R.id.textViewRace);
        textViewCharacterClass = findViewById(R.id.textViewCharacterClass);
        textViewLevel = findViewById(R.id.textViewLevel);

        btnEditCharacter = findViewById(R.id.btnEditCharacter);
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

        if (character != null) {
            textViewName.setText(String.format("Name: %s", character.getName()));
            textViewRace.setText(String.format("Race: %s", character.getRace()));
            textViewCharacterClass.setText(String.format("Class: %s", character.getCharacterClass()));
            textViewLevel.setText(String.format("Level: %d", character.getLevel()));
            // TODO: ostalo
        } else {
            Toast.makeText(this, "Character not found", Toast.LENGTH_SHORT).show();
        }

    }

    private Character getCharacterFromDatabase(long characterId){
        CharacterDatabaseHelper databaseHelper = new CharacterDatabaseHelper(this);
        return databaseHelper.getCharacter(characterId);

    }

    private void openEditCharacterActivity(long characterId){
        Intent intent = new Intent(ViewCharacterDetailsActivity.this, CreateCharacterActivity.class);
        intent.putExtra(EDIT_MODE_KEY,true);//oznacava rezim editovanja
        intent.putExtra(CHARACTER_ID_KEY, characterId);
        startActivityForResult(intent, REQUEST_EDIT_CHARACTER);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_EDIT_CHARACTER && resultCode == RESULT_OK){
            //ako je rezultat ok, azurira UI
            long characterId = getIntent().getLongExtra(CHARACTER_ID_KEY, -1);
            Intent updateIntent = new Intent();

            updateIntent.putExtra("updatedCharacterId", characterId);
            setResult(RESULT_OK,updateIntent);


            updateUI(characterId);
        }
    }

}
