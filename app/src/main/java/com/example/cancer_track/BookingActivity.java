package com.example.cancer_track;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class BookingActivity extends AppCompatActivity {
    private static final String TAG = "Booking";
    final Calendar myCalendar = Calendar.getInstance();
    Button btntime, btnBookingSave;
    EditText edittext;
    TextView tv_time;
    Spinner spinnerdoc;
    final Calendar c = Calendar.getInstance();
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);
    private FirebaseAuth firebaseAuth;
    String UserID;
    FirebaseFirestore fStore;
    String am_pm = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        Objects.requireNonNull(getSupportActionBar()).hide();


        fStore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        spinnerdoc = findViewById(R.id.spinnerDoc);
        edittext= (EditText) findViewById(R.id.edtBookingDate);
        tv_time = findViewById(R.id.tv_time);
        btntime = findViewById(R.id.btn_time);
        btnBookingSave = findViewById(R.id.btnBookingSave);

        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(BookingActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btntime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(BookingActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                tv_time.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, false);
                timePickerDialog.show();
            }
        });

        btnBookingSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserID = firebaseAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("booking").document(UserID);
                Map<String,Object> user = new HashMap<>();
                user.put("Date",edittext.getText().toString());
                user.put("Time",tv_time.getText().toString());
                user.put("Doctor",spinnerdoc.getSelectedItem().toString());

                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: user Profile is created for "+ UserID);
                        Toast.makeText(BookingActivity.this, "Successfully Saved to Database!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(BookingActivity.this, MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                        Toast.makeText(BookingActivity.this, "Failed to Save in Database!", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        BottomNavigationView bnv = findViewById(R.id.bot_nav);
        bnv.setSelectedItemId(R.id.home);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return  true;
                    case R.id.help:
                        startActivity(new Intent(getApplicationContext(),QuestionActivity.class));
                        overridePendingTransition(0,0);
                        return  true;
                    case R.id.info:
                        startActivity(new Intent(getApplicationContext(),InfoActivity.class));
                        overridePendingTransition(0,0);
                        return  true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return  true;


                }
                return false;
            }
        });

    }


    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {


        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateLabel();
        }

    };


    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//        if (myCalendar.get(Calendar.AM_PM) == Calendar.AM)
//            am_pm = "AM";
//        else if (myCalendar.get(Calendar.AM_PM) == Calendar.PM)
//            am_pm = "PM";
        edittext.setText(sdf.format(myCalendar.getTime()));
    }



}