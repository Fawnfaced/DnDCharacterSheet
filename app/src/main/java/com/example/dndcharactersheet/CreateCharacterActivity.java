package com.example.dndcharactersheet;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dndcharactersheet.database.CharacterDatabaseHelper;
import com.example.dndcharactersheet.models.Character;
import com.example.dndcharactersheet.util.ClickHelper;
import com.example.dndcharactersheet.util.Constants;

import java.util.Arrays;

public class CreateCharacterActivity extends AppCompatActivity {
    private EditText editTextName, editTextRace;
    private EditText[] editTextAbilityScores = new EditText[6];
    private Spinner spinnerCharacterClass, spinnerLeveL;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_character);

        editTextName = findViewById(R.id.editTextName);
        editTextRace = findViewById(R.id.editTextRace);
        spinnerCharacterClass = findViewById(R.id.spinnerCharacterClass);
        spinnerLeveL = findViewById(R.id.spinnerLevel);

        editTextAbilityScores[0] = findViewById(R.id.editTextStr);
        editTextAbilityScores[1] = findViewById(R.id.editTextDex);
        editTextAbilityScores[2] = findViewById(R.id.editTextCon);
        editTextAbilityScores[3] = findViewById(R.id.editTextInt);
        editTextAbilityScores[4] = findViewById(R.id.editTextWis);
        editTextAbilityScores[5] = findViewById(R.id.editTextCha);

        for (int i = 0; i < editTextAbilityScores.length; i++){
            setUpAbilityScoreEditText(editTextAbilityScores[i],i);
        }

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
                if (ClickHelper.isSingleClick()) {
                    saveCharacter();
                }
            }
        });
    }
    private  void setUpAbilityScoreEditText(EditText editText, final int index){
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //nepotrebno za sad
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //nepotrebno za sad
            }

            @Override
            public void afterTextChanged(Editable editable) {
                validateAbilityScore(editable, index);
            }
        });
    }

    private void validateAbilityScore(Editable editable, int index){
        if (!editable.toString().isEmpty()){
            int value = Integer.parseInt(editable.toString());
            if (value < Constants.ABILITY_SCORE_MIN){
                editTextAbilityScores[index].setText(String.valueOf(Constants.ABILITY_SCORE_MIN));
                Toast.makeText(this, "Ability Score too low! Setting it to 1.", Toast.LENGTH_SHORT).show();
            } else if (value > Constants.ABILITY_SCORE_MAX) {
                editTextAbilityScores[index].setText(String.valueOf(Constants.ABILITY_SCORE_MAX));
                Toast.makeText(this, "Ability Score too high! Setting it to 24.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void populateCharacterDetails(long characterId) {
        //popunjava CreateCharacterActivity predjasnjim podatcima u slucaju da smo dosli da promenimo podatke
        CharacterDatabaseHelper databaseHelper = new CharacterDatabaseHelper(this);
        Character character = databaseHelper.getCharacter(characterId);

        if (character !=null){
            editTextName.setText(character.getName());
            editTextRace.setText(character.getRace());
            spinnerCharacterClass.setSelection(getIndexForSpinner(spinnerCharacterClass, character.getCharacterClass()));
            spinnerLeveL.setSelection(getIndexForSpinner(spinnerLeveL, "Level " + character.getLevel()));

            for (int i = 0; i< editTextAbilityScores.length; i++){
                editTextAbilityScores[i].setText(character.getAbilityScores()[i]);
            }
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
        String name = editTextName.getText().toString().trim();
        String race = editTextRace.getText().toString().trim();
        String selectClass = spinnerCharacterClass.getSelectedItem().toString();
        int level;
        try {
            level = Integer.parseInt(spinnerLeveL.getSelectedItem().toString().replaceAll("[^0-9]",""));
        }catch (NumberFormatException e) {
            Toast.makeText(this, "Please select a valid level", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] abilityScores = new String[6];

        for (int i = 0; i < abilityScores.length; i++){
            abilityScores[i] = editTextAbilityScores[i].getText().toString().trim();
        }

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
        updateCharacter.setAbilityScoresFromString(Arrays.toString(abilityScores));

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
            if (characterId != -1){
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
