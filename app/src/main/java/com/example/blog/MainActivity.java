package com.example.blog;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blog.api.ApiService;
import com.example.blog.model.Login;
import com.example.blog.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnLogin;
    private Button btnRegister;
    private TextView errUsername;
    private TextView errPassword;
    private TextView btnForgot;
    private Context mContext;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        btnLogin.setOnClickListener(view -> {
            onClickLogin();
        });

        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(MainActivity.this);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        Log.d(TAG, token);
                    }
                });
    }

    private void onClickLogin() {
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        errPassword.setText("");
        errUsername.setText("");

        if(username.trim().equals("")) errUsername.setText("Email field is required.");
        if(password.trim().equals("")) errPassword.setText("Password field is required.");
        if(username.trim().equals("") || password.trim().equals("")) return;
        ApiService.apiService.loginUser(username, password)
                .enqueue(new Callback<Login>() {
                    @Override
                    public void onResponse(Call<Login> call, Response<Login> response) {
                        Login res = response.body();
                        if(res.getStatus() == 200) {
                            getInfo(res.getData().getToken_type() + " " + res.getData().getAccess_token());
                        } else if(res.getStatus() == 401) {
                            Toast.makeText(MainActivity.this, "Incorrect account or password!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "An error occurred, please try again later!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Login> call, Throwable t) {
                        onClickLogin();
                    }
                });
    }

    private void showDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_dialog_forgot_password);

        Button btnSave = dialog.findViewById(R.id.btnSave);
        EditText edtEmail = dialog.findViewById(R.id.edtEmail);
        ImageButton btnClose = dialog.findViewById(R.id.btnClose);
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setMessage("Please wait...");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressDialog.show();
                ApiService.apiService.forgotPassword(edtEmail.getText().toString()).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response.body().getStatus() == 200) {
                            Toast.makeText(MainActivity.this, "Please check your email.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Email false!", Toast.LENGTH_LONG).show();
                        }
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Email false!", Toast.LENGTH_LONG).show();
                        mProgressDialog.dismiss();
                    }
                });
            }
        });

        btnClose.setOnClickListener(v ->
        {
            dialog.dismiss();
        });

        dialog.show();
    }

    private void getInfo(String token) {
        ApiService.apiService.getInfoUser(token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User res = response.body();
                if(response.body().getStatus() == 200) {
                    SharedPreferences sharedPref =  PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("token",token);
                    editor.putInt("id",res.getData().getId());
                    editor.apply();
                    Intent intent = new Intent(MainActivity.this, ListBlogActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                getInfo(token);
            }
        });
    }

    private void init() {
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        errUsername = (TextView) findViewById(R.id.errUsername);
        errPassword = (TextView) findViewById(R.id.errPassword);
        btnForgot = findViewById(R.id.btnForgot);
    }
}