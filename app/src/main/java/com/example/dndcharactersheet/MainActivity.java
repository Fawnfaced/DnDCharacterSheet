package com.example.dndcharactersheet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCharacterList = findViewById(R.id.btnCharacterList);
        Button btnCreateCharacter = findViewById(R.id.btnCreateCharacter);

        btnCharacterList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CharacterListActivity.class);
                startActivity(intent);

            }
        });

        btnCreateCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateCharacterActivity.class);
                startActivity(intent);

            }
        });


    }
}