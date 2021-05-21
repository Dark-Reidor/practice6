package ru.mirea.linnik.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private EditText Note;
    private EditText Name;
    final String SAVED_TEXT = "saved_text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Note = findViewById(R.id.note);
        Name = findViewById(R.id.name);
        preferences = getPreferences(MODE_PRIVATE);
        if (!preferences.getString(SAVED_TEXT, "Empty").equals("Empty"))
            Name.setText(preferences.getString(SAVED_TEXT, "Empty"));
        Note.setText(getTextFromFile());
    }
    //сохранение текста
    public void onSaveText(View view) {
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(Name.getText().toString(), Context.MODE_PRIVATE);
            outputStream.write(Note.getText().toString().getBytes());
            outputStream.close();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(SAVED_TEXT, Name.getText().toString());
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getTextFromFile() {
        String fileName = preferences.getString(SAVED_TEXT, "Empty");
        if (fileName.equals("Empty"))
            return null;
        FileInputStream file = null;
        try {
            file = openFileInput(fileName);
            byte[] bytes = new byte[file.available()];
            file.read(bytes);
            String text = new String(bytes);
            Log.d(LOG_TAG, text);
            return text;
        } catch (IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (file != null)
                    file.close();
            } catch (IOException ex) {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        return null;
    }
}
