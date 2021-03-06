package com.example.free_friend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText UserEmail, UserPassword, UserConfirmPassword;
    private Button CreateAccountButton;
    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        UserEmail = (EditText) findViewById(R.id.register_email);
        UserPassword = (EditText) findViewById(R.id.register_password);
        UserConfirmPassword = (EditText) findViewById(R.id.register_confirm_password);
        CreateAccountButton = (Button) findViewById(R.id.registe_create_account);
        loadingBar = new ProgressDialog(this);

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }
        });
    }

    private void CreateNewAccount() {
        String email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();
        String confirmPassword = UserConfirmPassword.getText().toString();

        //Validate data

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please write Your Email.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Write Password.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(confirmPassword)){
            Toast.makeText(this, "Please confirm Confirm Password.", Toast.LENGTH_SHORT).show();
        }
        else if(!password.equals (confirmPassword)){
            Toast.makeText(this, "Password Not Match", Toast.LENGTH_SHORT).show();
        }
        else{

            loadingBar.setTitle("Creating Account:) ");
            loadingBar.setMessage("Please wait a minute while creating your account: ");
            loadingBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            loadingBar.setIndeterminate(true);
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);


            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                           if(task.isSuccessful()){

                               sendUserToSetupActivity();

                               Toast.makeText(RegisterActivity.this, "Account Created.", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                           }
                           else{
                               String message = task.getException().getMessage(); //display error
                               Toast.makeText(RegisterActivity.this, "Error Occured! Check details Please"+ message , Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                           }
                        }
                    });
        }
    }

    private void sendUserToSetupActivity() {
        Intent setupIntent = new Intent(RegisterActivity.this, SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }
}