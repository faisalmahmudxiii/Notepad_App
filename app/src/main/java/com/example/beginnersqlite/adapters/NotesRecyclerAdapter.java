package com.example.beginnersqlite.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beginnersqlite.R;
import com.example.beginnersqlite.models.Note;
import com.example.beginnersqlite.utils.Utility;

import java.util.ArrayList;

public  class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.ViewHolder> {

    private static final String TAG = "NotesRecyclerAdapter";

    private ArrayList<Note> mNotes= new ArrayList<>();
    private  OnNoteListener mOnNoteListener;

    public NotesRecyclerAdapter(ArrayList<Note> nNotes,  OnNoteListener onNoteListener) {
        this.mNotes = nNotes;
        this.mOnNoteListener= onNoteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_note_list_item, parent, false);
        return new ViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try{
            String month= mNotes.get(position).getTimeStamp().substring(0,2);
            month= Utility.getMonthFromNumber(month);
            String year= mNotes.get(position).getTimeStamp().substring(3);
            String timeStamp= month+" "+year;

            holder.timeStamp.setText(timeStamp);
            holder.title.setText(mNotes.get(position).getTitle());

        }catch (NullPointerException e){
            Log.d(TAG, "onBindViewHolder: NullPointerException"+e.getMessage());
        }
    }


    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, timeStamp;
        NotesRecyclerAdapter.OnNoteListener onNoteListener;

        public ViewHolder(@NonNull View itemView, NotesRecyclerAdapter.OnNoteListener onNoteListener) {
            super(itemView);

            title= itemView.findViewById(R.id.note_title);
            timeStamp= itemView.findViewById(R.id.note_timeStamp);
            this.onNoteListener= onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }



    public interface OnNoteListener{
        void onNoteClick(int position);
    }


}














