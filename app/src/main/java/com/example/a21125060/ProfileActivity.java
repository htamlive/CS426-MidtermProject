package com.example.a21125060;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class ProfileActivity extends AppCompatActivity {
    EditText etProfileAddress, etProfileFullName, etProfilePhoneNumber, etProfileEmail;
    List<EditText> editTextList;
    List<ImageButton> buttonList;
    ImageButton btnProfileFullNameEdit, btnProfilePhoneNumberEdit, btnProfileEmailEdit, btnProfileAddressEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        initTextViews();

        initButtons();

        readData();

        findViewById(R.id.clProfile).setOnClickListener(v -> {
            for(EditText editText : editTextList){
                editText.setEnabled(false);
                editText.setBackground(null);
            }
        });

        ImageButton btnProfileDetailBack = findViewById(R.id.btnProfileDetailBack);
        btnProfileDetailBack.setOnClickListener(v -> {
            saveProfileData();
            finish();
        });

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initTextViews() {
        etProfileFullName = findViewById(R.id.etProfileFullName);
        etProfilePhoneNumber = findViewById(R.id.etProfilePhoneNumber);
        etProfileEmail = findViewById(R.id.tvProfileEmail);
        etProfileAddress = findViewById(R.id.etProfileAddress);

        editTextList = new ArrayList<>();

        editTextList.add(etProfileFullName);
        editTextList.add(etProfilePhoneNumber);
        editTextList.add(etProfileEmail);
        editTextList.add(etProfileAddress);



        for(EditText editText : editTextList){
            editText.setEnabled(false);
            editText.setBackground(null);
            editText.setOnFocusChangeListener((v, hasFocus) -> {
                if(!hasFocus){
                    editText.setEnabled(false);
                    // set background to default
                    editText.setBackground(null);
                }
            });
        }

    }

    private void readData() {
        SharedPreferences preferences = getSharedPreferences("profile", MODE_PRIVATE);

        etProfileFullName.setText(preferences.getString("fullName", "User"));
        etProfilePhoneNumber.setText(preferences.getString("phoneNumber", ""));
        etProfileEmail.setText(preferences.getString("email", ""));
        etProfileAddress.setText(preferences.getString("address", ""));



    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initButtons() {
        btnProfileFullNameEdit = findViewById(R.id.btnProfileFullNameEdit);
        btnProfilePhoneNumberEdit = findViewById(R.id.btnProfilePhoneNumberEdit);
        btnProfileEmailEdit = findViewById(R.id.btnProfileEmailEdit);
        btnProfileAddressEdit = findViewById(R.id.btnProfileAddressEdit);

        buttonList = new ArrayList<>();

        buttonList.add(btnProfileFullNameEdit);
        buttonList.add(btnProfilePhoneNumberEdit);
        buttonList.add(btnProfileEmailEdit);
        buttonList.add(btnProfileAddressEdit);

        for(int i = 0; i < buttonList.size(); i++){
            ImageButton button = buttonList.get(i);
            EditText editText = editTextList.get(i);

            button.setOnClickListener(v -> {
                editText.setEnabled(true);
                editText.requestFocus();
                EditText et = new EditText(this);
                editText.setBackground(et.getBackground());
                editText.setBackground(getDrawable(android.R.drawable.edit_text));
            });
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        saveProfileData();

    }

    private void saveProfileData() {
        SharedPreferences preferences = getSharedPreferences("profile", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("fullName", etProfileFullName.getText().toString());
        editor.putString("phoneNumber", etProfilePhoneNumber.getText().toString());
        editor.putString("email", etProfileEmail.getText().toString());
        editor.putString("address", etProfileAddress.getText().toString());

        editor.apply();


    }


}