package com.example.blog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blog.api.ApiService;
import com.example.blog.model.Login;
import com.example.blog.model.User;
import com.example.blog.model.UserRO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtName;
    private TextView errName;
    private EditText edtEmail;
    private TextView errEmail;
    private EditText edtPassword;
    private TextView errPassword;
    private TextView errGender;
    private RadioButton rdoMale;
    private RadioButton rdoFemale;
    private Button btnLogin;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });
    }

    private void createUser() {
        String name = edtName.getText().toString();
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        ApiService.apiService.registerUser(name, email, password, rdoMale.isChecked() ? 1 : 0).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User res = response.body();
                errName.setText("");
                errPassword.setText("");
                errEmail.setText("");
                if(res.getStatus() == 200) {
                    Toast.makeText(RegisterActivity.this, "Register success!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if(res.getStatus() == 422) {
                    UserRO data = res.getData();
                    if(data.get_name() != null && data.get_name().length > 0) errName.setText(data.get_name()[0]);
                    if(data.get_email() != null && data.get_email().length > 0) errEmail.setText(data.get_email()[0]);
                    if(data.get_password() != null && data.get_password().length > 0) errPassword.setText(data.get_password()[0]);
                    if(data.get_gender() != null && data.get_gender().length > 0) errGender.setText(data.get_gender()[0]);

                    Toast.makeText(RegisterActivity.this, "Register fail!, ", Toast.LENGTH_LONG).show();
                } else {
                    Log.d("TAG", res.getStatus() + "");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                createUser();
            }
        });
    }

    private void init() {
        edtName = (EditText) findViewById(R.id.edtName);
        errName = (TextView) findViewById(R.id.errName);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        errEmail = (TextView) findViewById(R.id.errEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        errPassword = (TextView) findViewById(R.id.errPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        rdoFemale = findViewById(R.id.rdoFemale);
        rdoMale = findViewById(R.id.rdoMale);
        errGender = findViewById(R.id.errGender);
    }
}