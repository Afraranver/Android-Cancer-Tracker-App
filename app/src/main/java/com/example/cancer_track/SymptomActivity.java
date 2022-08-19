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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class SymptomActivity extends AppCompatActivity {

    private static final String TAG = "Symptom";
    private SeekBar sBar;
    private TextView txtSeverity;
    private String temp = "";
    final Calendar myCalendar = Calendar.getInstance();
    Button btn_time_symptom, btnSymSave;
    TextView tv_time_symptom;
    EditText edtExplainSymp, edtNotFound, edtSymComment, edtSymptoms;

    Spinner spinnerSymptoms;
    final Calendar c = Calendar.getInstance();
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);

    private FirebaseAuth firebaseAuth;
    String UserID;
    FirebaseFirestore fStore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom);
        Objects.requireNonNull(getSupportActionBar()).hide();
        int pval = 0;

        fStore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        btn_time_symptom = findViewById(R.id.btn_time_symptom);
        edtSymptoms= (EditText) findViewById(R.id.edtSymptoms);
        tv_time_symptom = findViewById(R.id.tv_time_symptom);
        spinnerSymptoms = findViewById(R.id.spinnerSymptoms);
        edtExplainSymp = findViewById(R.id.edtExplainSymp);
        edtNotFound = findViewById(R.id.edtNotFound);
        edtSymComment = findViewById(R.id.edtSymComment);
        btnSymSave = findViewById(R.id.btnSymSave);

        sBar = (SeekBar) findViewById(R.id.seekBar1);
        txtSeverity = (TextView) findViewById(R.id.txtSeverity);
        txtSeverity.setText("Mild");

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


        btnSymSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserID = firebaseAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("symptoms").document(UserID);
                Map<String,Object> user = new HashMap<>();
                user.put("Date",edtSymptoms.getText().toString());
                user.put("Time",tv_time_symptom.getText().toString());
                user.put("Doctor",spinnerSymptoms.getSelectedItem().toString());
                user.put("Explain Symptoms",edtExplainSymp.getText().toString());
                user.put("Not Found",edtNotFound.getText().toString());
                user.put("Severity",txtSeverity.getText().toString());
                user.put("Comment",edtSymComment.getText().toString());

                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: user Profile is created for "+ UserID);
                        Toast.makeText(SymptomActivity.this, "Successfully Saved to Database!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SymptomActivity.this, MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                        Toast.makeText(SymptomActivity.this, "Failed to Save in Database!", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });


        sBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int pval = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pval = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //write custom code to on start progress
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(pval <= 30){
                    temp = "Mild";
                }else if(pval <= 60){
                    temp = "Moderate";
                }else{
                    temp = "Severe";
                }
                txtSeverity.setText(temp);
            }
        });



        edtSymptoms.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(SymptomActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btn_time_symptom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(SymptomActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                tv_time_symptom.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, false);
                timePickerDialog.show();
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

        edtSymptoms.setText(sdf.format(myCalendar.getTime()));
    }
}