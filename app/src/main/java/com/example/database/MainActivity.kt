package com.example.database

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText

class MainActivity : Activity(), OnClickListener {
    private lateinit var Rollno: EditText
    private lateinit var Name: EditText
    private lateinit var Marks: EditText
    private lateinit var Insert: Button
    private lateinit var Delete: Button
    private lateinit var Update: Button
    private lateinit var View: Button
    private lateinit var ViewAll: Button
    private lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Rollno = findViewById(R.id.Rollno)
        Name = findViewById(R.id.Name)
        Marks = findViewById(R.id.Marks)
        Insert = findViewById(R.id.Insert)
        Delete = findViewById(R.id.Delete)
        Update = findViewById(R.id.Update)
        View = findViewById(R.id.View)
        ViewAll = findViewById(R.id.ViewAll)

        Insert.setOnClickListener(this)
        Delete.setOnClickListener(this)
        Update.setOnClickListener(this)
        View.setOnClickListener(this)
        ViewAll.setOnClickListener(this)

        // Creating database and table
        db = openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null)
        db.execSQL("CREATE TABLE IF NOT EXISTS student(rollno VARCHAR, name VARCHAR, marks VARCHAR);")
    }

    override fun onClick(view: View) {
        // Inserting a record to the Student table
        if (view == Insert) {
            // Checking for empty fields
            if (Rollno.text.toString().trim { it <= ' ' }.isEmpty() ||
                Name.text.toString().trim { it <= ' ' }.isEmpty() ||
                Marks.text.toString().trim { it <= ' ' }.isEmpty()
            ) {
                showMessage("Error", "Please enter all values")
                return
            }
            db.execSQL(
                "INSERT INTO student VALUES('" + Rollno.text + "','" + Name.text +
                        "','" + Marks.text + "');"
            )
            showMessage("Success", "Record added")
            clearText()
        }
        // Deleting a record from the Student table
        if (view == Delete) {
            // Checking for empty roll number
            if (Rollno.text.toString().trim { it <= ' ' }.isEmpty()) {
                showMessage("Error", "Please enter Rollno")
                return
            }
            val c = db.rawQuery("SELECT * FROM student WHERE rollno='" + Rollno.text + "'", null)
            if (c.moveToFirst()) {
                db.execSQL("DELETE FROM student WHERE rollno='" + Rollno.text + "'")
                showMessage("Success", "Record Deleted")
            } else {
                showMessage("Error", "Invalid Rollno")
            }
            clearText()
        }
        // Updating a record in the Student table
        if (view == Update) {
            // Checking for empty roll number
            if (Rollno.text.toString().trim { it <= ' ' }.isEmpty()) {
                showMessage("Error", "Please enter Rollno")
                return
            }
            val c = db.rawQuery("SELECT * FROM student WHERE rollno='" + Rollno.text + "'", null)
            if (c.moveToFirst()) {
                db.execSQL(
                    "UPDATE student SET name='" + Name.text + "',marks='" + Marks.text +
                            "' WHERE rollno='" + Rollno.text + "'"
                )
                showMessage("Success", "Record Modified")
            } else {
                showMessage("Error", "Invalid Rollno")
            }
            clearText()
        }
        // Display a record from the Student table
        if (view == View) {
            // Checking for empty roll number
            if (Rollno.text.toString().trim { it <= ' ' }.isEmpty()) {
                showMessage("Error", "Please enter Rollno")
                return
            }
            val c = db.rawQuery("SELECT * FROM student WHERE rollno='" + Rollno.text + "'", null)
            if (c.moveToFirst()) {
                Name.setText(c.getString(1))
                Marks.setText(c.getString(2))
            } else {
                showMessage("Error", "Invalid Rollno")
                clearText()
            }
        }
        // Displaying all the records
        if (view == ViewAll) {
            val c = db.rawQuery("SELECT * FROM student", null)
            if (c.count == 0) {
                showMessage("Error", "No records found")
                return
            }
            val buffer = StringBuilder()
            while (c.moveToNext()) {
                buffer.append("Rollno: " + c.getString(0) + "\n")
                buffer.append("Name: " + c.getString(1) + "\n")
                buffer.append("Marks: " + c.getString(2) + "\n\n")
            }
            showMessage("Student Details", buffer.toString())
        }
    }

    private fun showMessage(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.show()
    }

    private fun clearText() {
        Rollno.text.clear()
        Name.text.clear()
        Marks.text.clear()
        Rollno.requestFocus()
    }
}
