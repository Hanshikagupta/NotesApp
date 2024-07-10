package com.example.java;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.Nullable;

import java.util.List;


public class NotesFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private FirebaseAuth auth;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        auth = FirebaseAuth.getInstance();
        dbHelper = new DatabaseHelper(getActivity());
        adapter = new NotesAdapter(getNotesForUser(auth.getCurrentUser().getUid()), this::onNoteClick, this::onNoteDelete);
        recyclerView.setAdapter(adapter);

        Button addButton = view.findViewById(R.id.add_button);
        addButton.setOnClickListener(v -> {
            ((MainActivity) getActivity()).replaceFragment(new AddUpdateNoteFragment());
        });

        return view;
    }

    private List<Note> getNotesForUser(String userId) {
        return dbHelper.getNotesForUser(userId);
    }

    private void onNoteClick(Note note) {
        AddUpdateNoteFragment fragment = AddUpdateNoteFragment.newInstance(note);
        ((MainActivity) getActivity()).replaceFragment(fragment);
    }

    private void onNoteDelete(Note note) {
        dbHelper.deleteNote(note);
        adapter.updateNotes(getNotesForUser(auth.getCurrentUser().getUid()));
        Toast.makeText(getActivity(), "Note deleted", Toast.LENGTH_SHORT).show();
    }
}