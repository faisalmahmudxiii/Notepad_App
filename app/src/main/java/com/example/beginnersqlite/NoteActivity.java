package com.example.beginnersqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.beginnersqlite.models.Note;
import com.example.beginnersqlite.persistence.NoteRepository;
import com.example.beginnersqlite.utils.Utility;

public class NoteActivity extends AppCompatActivity implements
        View.OnTouchListener,
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener,
        View.OnClickListener,
        TextWatcher {

    private static final String TAG = "NoteActivity";
    private static final int EDIT_MODE_ENABLED=1;
    private static final int EDIT_MODE_DISABLED=0;

    //ui components
    private LinedEditText mLinedEditText;
    private EditText mEditNoteTitle;
    private TextView mViewNoteTitle;
    private RelativeLayout viewLayout, editLayout;
    private ImageView backArraow, checkMark;

    //vars
    private boolean isNewNote;
    private Note mInitialNote;
    private GestureDetector mGestureDetector;
    private int mMode;
    private NoteRepository mNoteRepository;
    private Note mFinalNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        mLinedEditText= findViewById(R.id.note_text);
        mEditNoteTitle= findViewById(R.id.toolbar_notetitle_editText);
        mViewNoteTitle= findViewById(R.id.toolbar_note_title);
        editLayout= findViewById(R.id.check_container);
        viewLayout= findViewById(R.id.backarrow_container);
        backArraow= findViewById(R.id.toolbar_back_arrow);
        checkMark= findViewById(R.id.toolbar_check_button);
        mEditNoteTitle.addTextChangedListener(this);

        mNoteRepository= new NoteRepository(this);

       if(getIncomingIntent()){
           // it is a new note, goto editmode
            setNewNoteProperties();
           setEditModeEnabled();

       }else {
           // its not a new note goto viewmode
           setNoteProperties();
           disableContentInteraction();
       }

        setListeners();
    }

    public void setEditModeEnabled(){
        editLayout.setVisibility(View.VISIBLE);
        viewLayout.setVisibility(View.GONE);

        mEditNoteTitle.setVisibility(View.VISIBLE);
        mViewNoteTitle.setVisibility(View.GONE);

        mMode= EDIT_MODE_ENABLED;

        enableContentInteraction();
    }

    public void setEditModeDisabled(){
        editLayout.setVisibility(View.GONE);
        viewLayout.setVisibility(View.VISIBLE);

        mEditNoteTitle.setVisibility(View.GONE);
        mViewNoteTitle.setVisibility(View.VISIBLE);

        mMode= EDIT_MODE_DISABLED;
        disableContentInteraction();

        String temp= mLinedEditText.getText().toString();
        temp= temp.replace("\n","");
        temp= temp.replace(" ", "");
        if (temp.length()>0){
            mFinalNote.setTitle(mEditNoteTitle.getText().toString());
            mFinalNote.setContent(mLinedEditText.getText().toString());
            String timeStamp= Utility.getCurrentTimestamp();
            mFinalNote.setTimeStamp(timeStamp);
            if ( !(mFinalNote.getContent().equals(mInitialNote.getContent())) || !(mFinalNote.getTitle().equals(mInitialNote.getTitle()))){
                saveChanges();
            }
        }
    }

    public void disableContentInteraction(){
       mLinedEditText.setKeyListener(null);
       mLinedEditText.setFocusable(false);
       mLinedEditText.setFocusableInTouchMode(false);
       mLinedEditText.clearFocus();
    }

    public void enableContentInteraction(){
        mLinedEditText.setKeyListener(new EditText(this).getKeyListener());
        mLinedEditText.setFocusable(true);
        mLinedEditText.setFocusableInTouchMode(true);
        mLinedEditText.requestFocus();
    }

    private void hideSoftKeyboard(){
        InputMethodManager imm= (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view= this.getCurrentFocus();
        if (view == null){
            view= new View(this);
        }

        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    public boolean getIncomingIntent(){
        if (getIntent().hasExtra("selected_note")){
             mInitialNote= getIntent().getParcelableExtra("selected_note");

             mFinalNote= new Note();
             mFinalNote.setTitle(mInitialNote.getTitle());
             mFinalNote.setContent(mInitialNote.getContent());
             mFinalNote.setTimeStamp(mInitialNote.getTimeStamp());
             mFinalNote.setId(mInitialNote.getId());

            mMode= EDIT_MODE_DISABLED;
            isNewNote= false;
            return false;
        }

        mMode= EDIT_MODE_ENABLED;
        isNewNote=true;
        return true;
    }

    private void saveChanges(){
        if(isNewNote){
            saveNewNote();
        }else {
            updateNote();
        }
    }

    private void updateNote(){
        mNoteRepository.upadateNote(mFinalNote);
    }

    private void saveNewNote(){
        mNoteRepository.insertNoteTask(mFinalNote);
    }

    public void setNoteProperties(){
        mViewNoteTitle.setText(mInitialNote.getTitle());
        mEditNoteTitle.setText(mInitialNote.getTitle());
        mLinedEditText.setText(mInitialNote.getContent());
    }

    public void setNewNoteProperties(){
        mEditNoteTitle.setText("note title");
        mViewNoteTitle.setText("note title");

        mInitialNote= new Note();
        mFinalNote= new Note();
        mInitialNote.setTitle("Note Title");
        mFinalNote.setTitle("Note Title");
    }

    public void setListeners(){
        mLinedEditText.setOnTouchListener(this);
        mGestureDetector= new GestureDetector(this, this);
        checkMark.setOnClickListener(this);
        mViewNoteTitle.setOnClickListener(this);
        backArraow.setOnClickListener(this);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.d(TAG, "onDoubleTap: double tapped");
        setEditModeEnabled();
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.toolbar_check_button:
                hideSoftKeyboard();
                setEditModeDisabled();
                break;
            case R.id.toolbar_note_title:
                setEditModeEnabled();
                mEditNoteTitle.requestFocus();
                mEditNoteTitle.setSelection(mEditNoteTitle.length());
                break;

                case R.id.toolbar_back_arrow:
                    finish();
                    break;
        }
    }

    @Override
    public void onBackPressed() {

        if(mMode == EDIT_MODE_ENABLED){
            onClick(checkMark);
        }else {
            super.onBackPressed();
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mViewNoteTitle.setText(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
