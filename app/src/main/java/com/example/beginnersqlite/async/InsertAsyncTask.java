package com.example.beginnersqlite.async;

import android.os.AsyncTask;

import com.example.beginnersqlite.models.Note;
import com.example.beginnersqlite.persistence.NoteDao;

public class InsertAsyncTask extends AsyncTask<Note, Void, Void> {

    private NoteDao mNoteDao;
    public InsertAsyncTask(NoteDao dao) {
        mNoteDao= dao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        mNoteDao.insertNotes(notes);
        return  null;
    }
}
