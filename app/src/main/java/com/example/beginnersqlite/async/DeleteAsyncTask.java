package com.example.beginnersqlite.async;

import android.os.AsyncTask;

import com.example.beginnersqlite.models.Note;
import com.example.beginnersqlite.persistence.NoteDao;

public class DeleteAsyncTask extends AsyncTask<Note, Void, Void> {

    private NoteDao mNoteDao;
    public DeleteAsyncTask(NoteDao dao) {
        mNoteDao= dao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        mNoteDao.delete(notes);
        return  null;
    }
}
