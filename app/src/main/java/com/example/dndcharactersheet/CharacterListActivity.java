package com.example.dndcharactersheet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dndcharactersheet.adapters.CharacterAdapter;
import com.example.dndcharactersheet.database.CharacterDatabaseHelper;
import com.example.dndcharactersheet.models.Character;

import java.util.ArrayList;
import java.util.List;


import android.widget.Toast;

public class CharacterListActivity extends AppCompatActivity {
    private CharacterAdapter characterAdapter;
    private List<Character> characterList;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewCharacterList);
        characterList = new ArrayList<>();
        characterAdapter = new CharacterAdapter(characterList, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(characterAdapter);

        loadCharacters();

    }

    private void loadCharacters() {

        // Update characterList and notify the adapter
        CharacterDatabaseHelper databaseHelper = new CharacterDatabaseHelper(this);
        List<Character> characters = databaseHelper.getAllCharacters();


        characterAdapter.setOnItemClickListener(new CharacterAdapter.OnItemClickListener()
        {

            @Override
            public void onItemClick ( int position){
                openCharacterDetails(characterList.get(position).getId());
        }
            @Override
            public void onItemLongClick(int position) {
                showOptionsDialog(position);
            }

        });

        characterList.clear();
        characterList.addAll(characters);
        characterAdapter.notifyDataSetChanged();
    }

    private void showOptionsDialog(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(new CharSequence[]{"Delete"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    deleteCharacter(position);
                }

            }
        });
        builder.setNegativeButton("Cancel",null);
        builder.show();
    }
    private void openCharacterDetails(long characterId){
        Intent intent = new Intent(CharacterListActivity.this, ViewCharacterDetailsActivity.class);
        intent.putExtra("characterId", characterId);
        startActivity(intent);
    }

    private void deleteCharacter(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Character");
        builder.setMessage("Are you sure you want to delete this character");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteCharacterFromDatabase(position);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void deleteCharacterFromDatabase(int position){
        long characterId = characterList.get(position).getId();

        CharacterDatabaseHelper databaseHelper = new CharacterDatabaseHelper(this);
        boolean isDeleted = databaseHelper.deleteCharacter(characterId);

        if (isDeleted){

            characterList.remove(position);
            characterAdapter.notifyItemRemoved(position);
            Toast.makeText(this, "Character deleted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to delete Character", Toast.LENGTH_SHORT).show();
        }
    }
}
