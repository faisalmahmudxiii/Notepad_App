package com.example.beginnersqlite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.beginnersqlite.adapters.NotesRecyclerAdapter;
import com.example.beginnersqlite.models.Note;
import com.example.beginnersqlite.persistence.NoteRepository;
import com.example.beginnersqlite.utils.VerticalSpacingItemDecorator;

import java.util.ArrayList;
import java.util.List;

public class NotesListActivity extends AppCompatActivity implements
        NotesRecyclerAdapter.OnNoteListener,
        View.OnClickListener{

    private static final String TAG = "NotesListActivity";
    // ui components
    RecyclerView mRecyclerView;

    // vars
    private ArrayList<Note> mNotes= new ArrayList<>();
    private NotesRecyclerAdapter mRecyclerAdapter;
    private NoteRepository mNoteRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noteslist);
        setSupportActionBar((Toolbar) findViewById(R.id.notes_toolbar));
        setTitle("Notes");
        mRecyclerView= findViewById(R.id.recyclerView);
        findViewById(R.id.fab).setOnClickListener(this);
        mNoteRepository= new NoteRepository(this);

        initRecyclerView();
       // addFakeNotes();

        retrieveNotes();
    }

    private void retrieveNotes(){
        mNoteRepository.retrieveNotesTask().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
               if (mNotes.size() > 0){
                   mNotes.clear();
               }

               if (notes != null){
                   mNotes.addAll(notes);
               }
               mRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    private void addFakeNotes(){

        for (int i=0; i<100; i++){
            Note note= new Note();
            note.setTitle("title #" +i);
            note.setContent("content #i" +i);
            note.setTimeStamp("jan 2021");

            mNotes.add(note);
        }

        mRecyclerAdapter.notifyDataSetChanged();
    }

    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator= new VerticalSpacingItemDecorator(10);
        mRecyclerView.addItemDecoration(itemDecorator);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
        mRecyclerAdapter= new NotesRecyclerAdapter(mNotes, this);
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    @Override
    public void onNoteClick(int position) {
        Intent intent= new Intent(this, NoteActivity.class);
        intent.putExtra("selected_note",mNotes.get(position));
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, NoteActivity.class));
    }

    private void deleteNote(Note note){
        mNotes.remove(note);
        mRecyclerAdapter.notifyDataSetChanged();
        mNoteRepository.deleteNote(note);
    }

    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback= new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            deleteNote(mNotes.get(viewHolder.getAdapterPosition()));
        }
    };
}
