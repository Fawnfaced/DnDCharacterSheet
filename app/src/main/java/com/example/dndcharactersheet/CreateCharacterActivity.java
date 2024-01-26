package com.example.dndcharactersheet;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dndcharactersheet.database.CharacterDatabaseHelper;
import com.example.dndcharactersheet.models.Character;

public class CreateCharacterActivity extends AppCompatActivity {
    private EditText editTextName, editTextRace;
    private Spinner spinnerCharacterClass, spinnerLeveL;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_character);

        editTextName = findViewById(R.id.editTextName);
        editTextRace = findViewById(R.id.editTextRace);
        spinnerCharacterClass = findViewById(R.id.spinnerCharacterClass);
        spinnerLeveL = findViewById(R.id.spinnerLevel);

        Intent intent = getIntent();
        long characterId = intent.getLongExtra("characterId", -1);
        boolean editMode = intent.getBooleanExtra("editMode",false);

        if (editMode){
            populateCharacterDetails(characterId);
        }

        Button btnSaveCharacter = findViewById(R.id.btnSaveCharacter);
        btnSaveCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCharacter();
            }
        });
    }

    private void populateCharacterDetails(long characterId) {
        CharacterDatabaseHelper databaseHelper = new CharacterDatabaseHelper(this);
        Character character = databaseHelper.getCharacter(characterId);

        if (character !=null){
            editTextName.setText(character.getName());
            editTextRace.setText(character.getRace());
            spinnerCharacterClass.setSelection(getIndexForSpinner(spinnerCharacterClass, character.getCharacterClass()));
            spinnerLeveL.setSelection(getIndexForSpinner(spinnerLeveL, "Level " + character.getLevel()));
        } else {
            Toast.makeText(this, "Character not found", Toast.LENGTH_SHORT).show();
        }
    }

    private int getIndexForSpinner(Spinner spinner, String value){
        for (int i = 0; i< spinner.getCount(); i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)){
                return i;
            }
        }
        return 0;
    }

    private void saveCharacter(){

        String selectClass = spinnerCharacterClass.getSelectedItem().toString();
        String name = editTextName.getText().toString().trim();
        String race = editTextRace.getText().toString().trim();
        int level = Integer.parseInt(spinnerLeveL.getSelectedItem().toString().replaceAll("[^0-9]",""));


        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(race)){
            Toast.makeText(this, "Please fill in all Fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectClass.isEmpty() ||selectClass.equals("Select Class")) {
            Toast.makeText(this, "Please select a valid character class", Toast.LENGTH_SHORT).show();
            return;
        }
        if (spinnerLeveL.getSelectedItem().toString().isEmpty() ||
                spinnerLeveL.getSelectedItem().toString().equalsIgnoreCase("Select Level")){
            Toast.makeText(this, "Please select a valid level", Toast.LENGTH_SHORT).show();
            return;
        }

        Character updateCharacter = new Character();
        updateCharacter.setName(name);
        updateCharacter.setRace(race);
        updateCharacter.setCharacterClass(selectClass);
        updateCharacter.setLevel(level);
        //TODO: ostalo


        Intent intent = getIntent();
        long characterId = intent.getLongExtra("characterId", -1);
        boolean editMode = intent.getBooleanExtra("editMode",false);



        CharacterDatabaseHelper databaseHelper = new CharacterDatabaseHelper(this);

        if (editMode){
            Character existingCharacter = databaseHelper.getCharacter(characterId);

            if (existingCharacter != null){
                updateCharacter.setId(characterId);
                int updatedRows = databaseHelper.updateCharacter(updateCharacter);

                if (updatedRows > 0){
                    Toast.makeText(this, "Character updated successfully",Toast.LENGTH_SHORT).show();

                    setResult(RESULT_OK);

                    finish();
                } else {
                    Toast.makeText(this,"Failed to update character", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Character not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            // kada nije edit mode
            characterId = saveCharacterToDatabase(updateCharacter);
            if (characterId !=-1){
                Toast.makeText(this, "Character saved successfully",Toast.LENGTH_SHORT).show();
                Intent mainIntent = new Intent(CreateCharacterActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            } else {
                Toast.makeText(this,"Failed to save character", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private long saveCharacterToDatabase(Character character){
        CharacterDatabaseHelper databaseHelper = new CharacterDatabaseHelper(this);
        return databaseHelper.addCharacter(character);
    }
}
