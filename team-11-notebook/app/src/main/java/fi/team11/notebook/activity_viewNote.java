package fi.team11.notebook;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class activity_viewNote extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        //getting position in array
        Bundle extras = getIntent().getExtras();
        int position = extras.getInt("position");
        loadNote(position);
        //getting position in array END
    }

    private void loadNote(int position){ //DISPLAY TEXT OF NOTE
        Log.i(tag, "and finally, it is: "+position);
        Note currentNote = myNotes.get(position);
        TextView tv = findViewById(R.id.viewText);
        tv.setText(currentNote.getText());
    }

}