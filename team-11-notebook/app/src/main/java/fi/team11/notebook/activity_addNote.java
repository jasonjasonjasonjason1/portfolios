package fi.team11.notebook;

import androidx.annotation.RequiresApi;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.time.LocalDateTime;

public class activity_addNote extends MainActivity {
    private String title;
    private String text;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Log.i(tag, "Add activity loaded");

        EditText giveTitle = findViewById(R.id.inputTitle);
        EditText giveText = findViewById(R.id.inputAddNote);

        giveTitle.addTextChangedListener(new TextWatcher() { //REACT TO TITLE
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override //REACT TO TITLE
            public void onTextChanged(CharSequence givenTitle, int start, int before, int count) {
                title = String.valueOf(givenTitle);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        giveText.addTextChangedListener(new TextWatcher() { //REACT TO TEXT
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override //REACT TO TEXT
            public void onTextChanged(CharSequence givenText, int start, int before, int count) {
                text = String.valueOf(givenText);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //Save button
        Button save = findViewById(R.id.saveButton);
        save.setOnClickListener(view ->{
            Log.i(tag, "Save button pressed");
            buttonPress();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void buttonPress(){
        //GIVE TITLE AND TEXT

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        if (title==null){
            CharSequence toastText = "Give a title";
            Toast toast = Toast.makeText(context, toastText, duration);
            toast.show();
        }else if (runCheck()){
            CharSequence toastText = "Give a unique title";
            Toast toast = Toast.makeText(context, toastText, duration);
            toast.show();
        }else{
            CharSequence toastText = "Note saved";
            Toast toast = Toast.makeText(context, toastText, duration);
            toast.show();
            LocalDateTime created = LocalDateTime.now();
            String now = dateConverter(created, false);
            String nowV = dateConverter(created, true);
            Log.i(tag, "date when created"+now);
            MainActivity.myNotes.add(new Note(title,text,now,now,nowV,nowV));
            save();
            finisher();
        }
    }

    private boolean runCheck(){
        int length=myNotes.size();
        for (int i = 0; i<length;i++){
            Note currentNote = myNotes.get(i);
            String pulledTitle = currentNote.getTitle();
            if (title.equals(pulledTitle)){
                return true;
            }
        }
        return false;
    }

}