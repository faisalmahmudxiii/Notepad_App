package com.example.beginnersqlite.persistence;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.beginnersqlite.async.DeleteAsyncTask;
import com.example.beginnersqlite.async.InsertAsyncTask;
import com.example.beginnersqlite.async.UpdateAsyncTask;
import com.example.beginnersqlite.models.Note;

import java.util.List;

public class NoteRepository {

    private NoteDatabase mNoteDatabase;

    public NoteRepository(Context context) {
        mNoteDatabase= NoteDatabase.getInstance(context);
    }

    public void insertNoteTask(Note note){
        new InsertAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }

    public void upadateNote(Note note){
        new UpdateAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }

    public LiveData<List<Note>> retrieveNotesTask(){
        return mNoteDatabase.getNoteDao().getNotes();
    }

    public void deleteNote(Note note){
        new DeleteAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }
}
