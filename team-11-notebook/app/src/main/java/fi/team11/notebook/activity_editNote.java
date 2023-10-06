package fi.team11.notebook;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;

import static fi.team11.notebook.MainActivity.myNotes;
import static fi.team11.notebook.MainActivity.tag;

public class activity_editNote extends MainActivity {
    private String title;
    private String text;
    private LocalDateTime updated;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Log.i(tag, "Edit activity loaded");
        //getting position in array
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", -1);

        EditText editTitle = findViewById(R.id.editTitle);
        EditText editText = findViewById(R.id.editText);
        if (position != -1){//SET TEXT
            Note currentNote = MainActivity.myNotes.get(position);
            editTitle.setText(currentNote.getTitle());
            editText.setText(currentNote.getText());
            title = currentNote.getTitle();
            text = currentNote.getText();
        }
        editTitle.addTextChangedListener(new TextWatcher() { //REACT TO CHANGES
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence changedTitle, int start, int before, int count) {
                Log.i(tag, "title changed");
                title = String.valueOf(changedTitle);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        editText.addTextChangedListener(new TextWatcher() { //REACT TO CHANGES
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence changedText, int start, int before, int count) {
                Log.i(tag, "text changed");
                text = String.valueOf(changedText);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //Button workings
        Button save = findViewById(R.id.saveButton);
        save.setOnClickListener(view ->{
            Log.i(tag,"Save button clicked");
            Note currentNote = MainActivity.myNotes.get(position);
            Log.i(tag, "title is:"+title+".text is: "+text);
            currentNote.setTitle(String.valueOf(title));
            currentNote.setText(String.valueOf(text));
            currentNote.setUpdated(dateConverter(java.time.LocalDateTime.now(), false));
            currentNote.setUpdatedV(dateConverter(java.time.LocalDateTime.now(),true));
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            if (title.equals("")){
                CharSequence toastText = "Give a title";
                Toast toast = Toast.makeText(context, toastText, duration);
                toast.show();
            }else if(runCheck()){
                CharSequence toastText = "Give a unique title";
                Toast toast = Toast.makeText(context, toastText, duration);
                toast.show();
            }else{
                CharSequence toastText = "Note modified";
                Toast toast = Toast.makeText(context, toastText, duration);
                toast.show();
                save();
                finisher();
            }
        });
    }

    private boolean runCheck(){
        int length=myNotes.size();
        int sames = 0;
        for (int i = 0; i<length;i++){
            Note currentNote = myNotes.get(i);
            String pulledTitle = currentNote.getTitle();
            if (title.equals(pulledTitle)){
                sames++;
            }
        }
        if (sames<=1){
            return false;
        }else{
            return true;
        }

    }

}