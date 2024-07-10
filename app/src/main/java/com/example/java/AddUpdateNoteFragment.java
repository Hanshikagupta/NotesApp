package com.example.java;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.Nullable;


public class AddUpdateNoteFragment extends Fragment {

    private static final String ARG_NOTE = "note";
    private EditText titleEditText;
    private EditText contentEditText;
    private FirebaseAuth auth;
    private DatabaseHelper databaseHelper;
    private Note note;

    public static AddUpdateNoteFragment newInstance(Note note) {
        AddUpdateNoteFragment fragment = new AddUpdateNoteFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_update_note, container, false);

        titleEditText = view.findViewById(R.id.title_edit_text);
        contentEditText = view.findViewById(R.id.content_edit_text);
        auth = FirebaseAuth.getInstance();
        databaseHelper = new DatabaseHelper(getActivity());

        if (getArguments() != null) {
            note = (Note) getArguments().getSerializable(ARG_NOTE);
            if (note != null) {
                titleEditText.setText(note.getTitle());
                contentEditText.setText(note.getContent());
            }
        }

        Button saveButton = view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(v -> {
            saveOrUpdateNote();
            ((MainActivity) getActivity()).replaceFragment(new NotesFragment());
        });

        return view;
    }

    private void saveOrUpdateNote() {
        String title = titleEditText.getText().toString().trim();
        String content = contentEditText.getText().toString().trim();
        String userId = auth.getCurrentUser().getUid();

        if (note == null) {
            note = new Note(userId, title, content);
            databaseHelper.addNote(note);
        } else {
            note.setTitle(title);
            note.setContent(content);
            databaseHelper.updateNote(note);
        }
    }
}