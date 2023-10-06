package fi.team11.notebook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //LOGGING
    public static final String tag = "MYTAG";
    //LOGGING

    //--ordering--
    public static List<Note> orderNotes = new ArrayList<>();
    private final String order = "Ordering";
    private SharedPreferences sharedPreferences;
    private int orderState = 0;
    //--ordering--

    //Listview&Adapter
    public static List<Note> myNotes = new ArrayList<>();
    public static ArrayAdapter<Note> arrayAdapter;
    //Listview&Adapter

    //FILE STORAGE
    Gson gson = new Gson();
    private final String storingData = "data";
    private String json;
    //FILE STORAGE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(tag, "MainActivity created");
        setContentView(R.layout.activity_main);

        //--ordering--
        sharedPreferences = getSharedPreferences("preferences", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        orderState = sharedPreferences.getInt(order, 0);
        editor.putInt(order, orderState);
        editor.apply(); //write pref to disk
        Log.i(tag,order+" preference is in state "+sharedPreferences.getInt(order,0)+" at start/refresh.");
        order();
        //--ordering--

        load(); //--initial storage load--


        //Listview&Adapter
        ListView listView = findViewById(R.id.listView);
        arrayAdapter = new myListAdpt();
        listView.setAdapter(arrayAdapter);
        //Listview&Adapter

        //Find add floating action button
        FloatingActionButton addfAB = findViewById(R.id.floatingActionButton2);
        //Add click listener to floating action button
        addfAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote();
            }
        });
}

    //------NOTE FUNCTIONS------
    private void addNote(){ //ADD
        Intent intent = new Intent(this, activity_addNote.class);
        startActivity(intent);
        //code to create new note
        Log.i(tag,"'Add note' has been pressed");
    }
    private void openView(int position){ //OPEN
        Intent intent = new Intent(this, activity_viewNote.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }
    private void editView(int position){ //EDIT
        Intent intent = new Intent(this, activity_editNote.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }
    private void delete(int position){ //DELETE
        myNotes.remove(position);
        save();
        finisher();
    }
    public void finisher(){
        finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
    private void getInfo(){
        Intent intent = new Intent(this,info.class);
        startActivity(intent);
    }
    private void order(){
        if (orderState==0){
            orderAlpha();
        }else if (orderState==1){
            orderLatest();
        }else{
            orderNewest();
        }
    }
    private void orderAlpha(){
        orderNotes.clear();
        int length = myNotes.size();
        Log.i(tag, "length is: "+ length);
        if (length==0){
            return;
        }
        String[] titles = new String[length];
        for (int i = 0; i<length;i++){ //suck out titles
            Note currentNote = myNotes.get(i);
            String titleNote = currentNote.getTitle();
            titles[i]=titleNote;
            Log.i(tag, i+". title is: "+ titles[i]);
        }
        Arrays.sort(titles); //sort titles
        Log.i(tag, "first after ordering: "+titles[0]);
        for (int i = 0; i<length;i++){
            for (int e = 0; e<length;e++){
                Note currentNote = myNotes.get(e);
                String titleNote = currentNote.getTitle();
                String textNote = currentNote.getText();
                String createNote = currentNote.getCreated();
                String updateNote = currentNote.getUpdated();
                String createNoteV = currentNote.getCreatedV();
                String updateNoteV = currentNote.getUpdatedV();
                if (titles[i].equals(titleNote)){
                    Log.i(tag, i+". is the "+e+"th note");
                    orderNotes.add(new Note(titleNote,textNote, createNote, updateNote, createNoteV, updateNoteV));
                }
            }
        }//put into sorted list
        myNotes = orderNotes;
        Note currentNote = myNotes.get(0);
        String titleNote = currentNote.getTitle();
        Log.i(tag, "notes sorted into alphabetical order. First: "+titleNote);
        save();
    }
    private void orderNewest(){
        Log.i(tag, "debug point 1");
        orderNotes.clear();
        int length = myNotes.size();
        Log.i(tag, "length is: "+ length);
        Log.i(tag, "debug point 2");
        if (length==0){
        return;
        }
        Log.i(tag, "debug point 3");
        String[] created = new String[length];
        String[] createdDesc = new String[length];
        Log.i(tag, "debug point 4");
        for (int i = 0; i<length;i++){ //suck out titles
            Note currentNote = myNotes.get(i);
            String createdNote = currentNote.getCreated();
            created[i]=createdNote;
            Log.i(tag, i+". create date is: "+ created[i]);
        }
        Arrays.sort(created); //sort titles
        int top = length-1;//sort descending
        for (int i = 0; i<length;i++){
            createdDesc[top]=created[i];
            top--;
        }
        for (int i = 0; i<length;i++){
            for (int e = 0; e<length;e++){
                Note currentNote = myNotes.get(e);
                String titleNote = currentNote.getTitle();
                String textNote = currentNote.getText();
                String createNote = currentNote.getCreated();
                String updateNote = currentNote.getUpdated();
                String createNoteV = currentNote.getCreatedV();
                String updateNoteV = currentNote.getUpdatedV();
                if (createdDesc[i].equals(createNote)){
                    Log.i(tag, i+". is the "+e+"th note");
                    orderNotes.add(new Note(titleNote,textNote, createNote, updateNote, createNoteV, updateNoteV));
                }
            }
        }//put into sorted list
        myNotes = orderNotes;
        Note currentNote = myNotes.get(0);
        String titleNote = currentNote.getTitle();
        Log.i(tag, "notes sorted into creation order. First: "+titleNote);
        save();
    }
    private void orderLatest(){
        orderNotes.clear();
        int length = myNotes.size();
        Log.i(tag, "length is: "+ length);
        if (length==0){
            return;
        }
        String[] updated = new String[length];
        String[] updatedDesc = new String[length];
        for (int i = 0; i<length;i++){ //suck out titles
            Note currentNote = myNotes.get(i);
            String createdNote = currentNote.getUpdated();
            updated[i]=createdNote;
        }
        Arrays.sort(updated); //sort titles
        //sort descending
        int top = length-1;
        for (int i = 0; i<length;i++){
            updatedDesc[top]=updated[i];
            top--;
        }
        for (int i = 0; i<length;i++){
            for (int e = 0; e<length;e++){
                Note currentNote = myNotes.get(e);
                String titleNote = currentNote.getTitle();
                String textNote = currentNote.getText();
                String createNote = currentNote.getCreated();
                String updateNote = currentNote.getUpdated();
                String createNoteV = currentNote.getCreatedV();
                String updateNoteV = currentNote.getUpdatedV();
                if (updatedDesc[i].equals(updateNote)){
                    Log.i(tag, i+". is the "+e+"th note");
                    orderNotes.add(new Note(titleNote,textNote, createNote, updateNote, createNoteV, updateNoteV));
                }
            }
        }//put into sorted list
        myNotes = orderNotes;
        Note currentNote = myNotes.get(0);
        String titleNote = currentNote.getTitle();
        Log.i(tag, "notes sorted into update order. First: "+titleNote);
        save();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String dateConverter(LocalDateTime localDateTime, boolean isVisible) {
        if (isVisible){
            String hours = DateTimeFormatter.ofPattern("hh:mm").format(localDateTime);
            String date = DateTimeFormatter.ofPattern("dd.MM.yyyy").format(localDateTime);
            return date+" "+hours;
        }
        return DateTimeFormatter.ofPattern("yyyy.MM.dd.hh:mm:ss").format(localDateTime);
    } //reference: https://stackoverflow.com/questions/47886158/how-convert-localdatetime-to-format-dd-mm-yyyy
    //------NOTE FUNCTIONS------

    //STORAGE
    public void save() {
        Log.i(tag, "Save running");
        this.json = gson.toJson(myNotes);
        Log.i(tag, "json after saving: "+this.json);
        sharedPreferences = getSharedPreferences("preferences", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPreferences.edit();
        editor2.putString(storingData, json);
        editor2.apply();
    }
    private void load(){
        Log.i(tag, "Load running");
        json = sharedPreferences.getString(storingData, "");
        Log.i(tag, "json before loading: "+this.json);
        TypeToken<List<Note>> token = new TypeToken<List<Note>>() {};
        if(json != ""){
            myNotes = gson.fromJson(json, token.getType());
        }
    }
    //STORAGE

    public class myListAdpt extends ArrayAdapter<Note>{
            public myListAdpt(){
                super(MainActivity.this,R.layout.note_listing, myNotes);
            }
        @RequiresApi(api = Build.VERSION_CODES.O)
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.note_listing, parent, false);
            }
            Note currentNote = myNotes.get(position);
            TextView title = itemView.findViewById(R.id.listName);
            Button clickEdit = itemView.findViewById(R.id.editButton);
            Button clickDel = itemView.findViewById(R.id.deleteButton);
            title.setText(currentNote.getTitle());
            title.setOnLongClickListener(v ->{
                String tit = currentNote.getTitle();
                String cre = currentNote.getCreatedV();
                String upd = currentNote.getUpdatedV();
                int length;
                if (currentNote.getText()==null){
                    length=0;
                }else{
                    length = currentNote.getText().length();
                }

                onButtonShowPopupWindowClick(v, tit, cre, upd, length);
                return true;
            }); //LONGPRESS ON TITLE
            title.setOnClickListener(v -> {
                Log.i(tag, "View was pressed");
                openView(position);
            }); //PRESS ON TITLE
            clickEdit.setOnClickListener(view ->{
                Log.i(tag,"Edit has been pressed.");
                editView(position);
            });
            clickDel.setOnClickListener(view ->{
                //code to delete NOTE
                Log.i(tag,"Delete has been pressed.");
                delete(position);
            });
            return itemView;
        }
    } //Adapter


    public void onButtonShowPopupWindowClick(View view, String tit, String cr, String up, int length) {
        String title = tit;
        String created = "Created on: "+cr;
        String updated = "Last updated on: "+up;
        String characters = "Length: "+length+" characters";


        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        TextView tvTitle = popupWindow.getContentView().findViewById(R.id.popTitle);
        TextView tvText1 = popupWindow.getContentView().findViewById(R.id.popText1);
        TextView tvText2 = popupWindow.getContentView().findViewById(R.id.popText2);
        TextView tvText3 = popupWindow.getContentView().findViewById(R.id.popText3);
        tvTitle.setText(title);
        tvText1.setText(created);
        tvText2.setText(updated);
        tvText3.setText(characters);


        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    } //INFO POPUP (reference: https://stackoverflow.com/questions/5944987/how-to-create-a-popup-window-popupwindow-in-android)


    //ORDER AND ADD BUTTON IN TITLE ON TOP
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (itemID){
            case R.id.alphaOrder: //ORDERING
                orderState=0;
                //ADD FUNCTION IF CLICK A-Z IN ORDER
                editor.putInt(order, orderState);
                editor.apply();
                Log.i(tag,order+" preference is in state "+sharedPreferences.getInt(order,0)+" at pressing order button.");
                finisher();
                break;
            case R.id.useTimeOrder:
                orderState=1;
                //ADD FUNCTION IF CLICK LAST USED BUTTON IN ORDER
                editor.putInt(order, orderState);
                editor.apply();
                Log.i(tag,order+" preference is in state "+sharedPreferences.getInt(order,0)+" at pressing order button.");
                finisher();
                break;
            case R.id.creatTimeOrder:
                orderState=2;
                //ADD FUNCTION IF CLICK LAST CREATED BUTTON IN ORDER
                editor.putInt(order, orderState);
                editor.apply();
                Log.i(tag,order+" preference is in state "+sharedPreferences.getInt(order,0)+" at pressing order button.");
                finisher();
                break; //ORDERING END
            case R.id.infoBut:
                //ADD FUNCTION IF CLICK INFO BUTTON
                getInfo();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}