package com.example.scannerproto;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.example.scannerproto.anlaysis.helpers.IObjectInfoSetter;
import com.example.scannerproto.anlaysis.helpers.db.SQLiteInfoGetter;
import com.example.scannerproto.anlaysis.helpers.db.ThingWithId;

public class AddObjectActivity extends AppCompatActivity {

    private IObjectInfoSetter infoSetter = new SQLiteInfoGetter(AddObjectActivity.this);
    private EditText nameEditText;
    private EditText infoEditText;

    private String receivedString;
    private ITextValidator validator = new SimpleTextValidator();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_addobject);

        nameEditText = findViewById(R.id.nameEditText);
        infoEditText = findViewById(R.id.objectInfo);

        receivedString = getIntent().getStringExtra("ObjectName");
        if (receivedString != null) {
            Log.println(Log.VERBOSE, TAG, receivedString);
            nameEditText.setText(receivedString);
        }
    }

    public void onAdd(View view) {
        String name = nameEditText.getText().toString();
        String info = infoEditText.getText().toString();
        if (validator.validate(info)) {
            if (receivedString != null) {
                ThingWithId newThing = new ThingWithId(receivedString, name, info);
                infoSetter.setObjectInfo(newThing);
                finish();
                MainActivity.isNewObjectFound.set(false);
            }
            else {
                MainActivity.isNewObjectFound.set(false);
                finish();
            }
        } else {
            Toast.makeText(this, "Not enough info for object", Toast.LENGTH_SHORT).show();
        }

    }

}