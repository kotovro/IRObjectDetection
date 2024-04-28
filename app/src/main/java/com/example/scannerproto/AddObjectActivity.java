package com.example.scannerproto;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scannerproto.anlaysis.helpers.IObjectInfoAdder;
import com.example.scannerproto.anlaysis.helpers.IObjectInfoGetter;
import com.example.scannerproto.anlaysis.helpers.filedb.FileObjectGetter;
import com.example.scannerproto.anlaysis.helpers.filedb.FileUtils;
import com.example.scannerproto.anlaysis.helpers.filedb.Filethings;
import com.example.scannerproto.anlaysis.helpers.mockdb.SimpleObjectAdder;
import com.example.scannerproto.anlaysis.helpers.mockdb.SimpleObjectInfoGetter;
import com.example.scannerproto.anlaysis.helpers.mockdb.Thing;

import java.io.File;
import java.io.IOException;

public class AddObjectActivity extends AppCompatActivity {

    private IObjectInfoGetter infoGetter = new SimpleObjectInfoGetter();
    private IObjectInfoAdder<Thing> adder = new SimpleObjectAdder();
    private EditText nameEditText;
    private EditText infoEditText;

    private String receivedString;
    private ITextValidator validator = new SimpleTextValidator();

    public void onReturn(View view) {
        MainActivity.isNewObjectFound.set(false);
        finish();
    }

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
        }
    }

    public void onAdd(View view) throws IOException {
        String name = nameEditText.getText().toString();
        String info = infoEditText.getText().toString();
        if (validator.validate(info)) {
            if (receivedString != null) {
                FileObjectGetter.things.addLast(new Filethings(receivedString, name, info));
                MainActivity.isNewObjectFound.set(false);
                FileUtils.writeListToFile(Environment.getExternalStorageDirectory() + "/" + File.separator + "base.txt", FileObjectGetter.things);
                finish();
                clearEditTexts();
            }
            else {
                finish();
            }
        } else {
            Toast.makeText(this, "Not enough info for object", Toast.LENGTH_SHORT).show();
        }

    }
    public void clearEditTexts() {
        nameEditText.getText().clear();
        infoEditText.getText().clear();
    }

}
