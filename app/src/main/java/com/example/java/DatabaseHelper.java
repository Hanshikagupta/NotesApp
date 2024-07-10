package com.example.java;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

private static final String DATABASE_NAME = "notes.db";
private static final int DATABASE_VERSION = 1;

private static final String TABLE_NOTES = "notes";
private static final String COLUMN_ID = "id";
private static final String COLUMN_USER_ID = "user_id";
private static final String COLUMN_TITLE = "title";
private static final String COLUMN_CONTENT = "content";

public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

@Override
public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES + "("
        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
        + COLUMN_USER_ID + " TEXT,"
        + COLUMN_TITLE + " TEXT,"
        + COLUMN_CONTENT + " TEXT" + ")";
        db.execSQL(CREATE_NOTES_TABLE);
        }

@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
        }

public void addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, note.getUserId());
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_CONTENT, note.getContent());

        db.insert(TABLE_NOTES, null, values);
        db.close();
        }

public void updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_CONTENT, note.getContent());

        db.update(TABLE_NOTES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(note.getId())});
        db.close();
        }

public void deleteNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, COLUMN_ID + " = ?", new String[]{String.valueOf(note.getId())});
        db.close();
        }

public List<Note> getNotesForUser(String userId) {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTES, new String[]{COLUMN_ID, COLUMN_USER_ID, COLUMN_TITLE, COLUMN_CONTENT},
        COLUMN_USER_ID + " = ?", new String[]{userId}, null, null, null, null);

        if (cursor != null) {
        if (cursor.moveToFirst()) {
        do {
        Note note = new Note(
        cursor.getString(1),
        cursor.getString(2),
        cursor.getString(3)
        );
        note.setId(cursor.getInt(0));
        notes.add(note);
        } while (cursor.moveToNext());
        }
        cursor.close();
        }

        return notes;
        }
        }