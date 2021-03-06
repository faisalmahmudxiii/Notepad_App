package com.example.beginnersqlite.async;

import android.os.AsyncTask;

import com.example.beginnersqlite.models.Note;
import com.example.beginnersqlite.persistence.NoteDao;

public class UpdateAsyncTask extends AsyncTask<Note, Void, Void> {

    private NoteDao mNoteDao;
    public UpdateAsyncTask(NoteDao dao) {
        mNoteDao= dao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        mNoteDao.update(notes);
        return  null;
    }
}
