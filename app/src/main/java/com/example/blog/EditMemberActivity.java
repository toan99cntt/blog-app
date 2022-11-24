package com.example.blog;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blog.api.ApiService;
import com.example.blog.model.User;
import com.example.blog.model.UserRO;
import com.example.blog.util.RealPathUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditMemberActivity extends AppCompatActivity {
    private static final int MY_REQUEST_CODE = 10;
    private EditText edtName;
    private EditText edtPhone;
    private EditText edtDob;
    private EditText edtEmail;
    private CircleImageView imgAvatar;
    private RadioButton rdoMale;
    private RadioButton rdoFemale;
    private Button btnSave;
    private ImageButton btnEditImage;
    private Uri mUri;
    private ProgressDialog mProgressDialog;
    private int id;
    private TextView errEmail;
    private TextView errName;
    private TextView errPhone;
    private TextView errDob;
    private EditText edtPassword;
    private TextView errPassword;

    private ActivityResultLauncher <Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if(data == null) {
                            return;
                        }
                        Uri uri = data.getData();
                        mUri = uri;
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            imgAvatar.setImageBitmap(bitmap);
                        } catch (IOException err) {
                            //
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_member);
        init();
        mProgressDialog = new ProgressDialog(EditMemberActivity.this);
        mProgressDialog.setMessage("Please wait...");
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String token = sharedPref.getString("token", null);
        if(token == null) return;
        fetchData(token);
        btnEditImage.setOnClickListener(view -> {
                  onClickRequestPermission();
        });

        btnSave.setOnClickListener(view -> {
            if(mUri != null) {
                saveAvatar(token);
            } else {
                saveInputText(token);
            }
        });
    }

    private void saveAvatar(String token) {
        mProgressDialog.show();
        String path = RealPathUtil.getRealPath(EditMemberActivity.this, mUri);
        File file = new File(path);
        RequestBody requestBodyAvatar = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part muPart = MultipartBody.Part.createFormData("avatar", file.getName(), requestBodyAvatar);
        ApiService.apiService.editAvatarUser(id, muPart, token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User res = response.body();
                if(res.getStatus() == 200) {
                    saveInputText(token);
                } else {
                    Log.d("ssss", res.getStatus() + res.getError_messages());
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("error update avatar", t.toString());
            }
        });
        mProgressDialog.dismiss();
    }

    private void saveInputText(String token) {
        ApiService.apiService.editInfoUser(id, edtName.getText().toString(), edtPassword.getText().toString(), edtPhone.getText().toString(), edtDob.getText().toString(), rdoMale.isChecked() ? 1: 0 , token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User res = response.body();
                errName.setText("");
                errPhone.setText("");
                errDob.setText("");
                errPassword.setText("");
                if(res.getStatus() == 200) {
                    Toast.makeText(EditMemberActivity.this, "Update profile success!", Toast.LENGTH_LONG).show();
                } else if(res.getStatus() == 422) {
                    UserRO data = res.getData();
                    Toast.makeText(EditMemberActivity.this, "Update profile fail!", Toast.LENGTH_LONG).show();
                    if(data.get_name() != null && data.get_name().length > 0) errName.setText(data.get_name()[0]);
                    if(data.get_dob() != null && data.get_dob().length > 0) errDob.setText(data.get_dob()[0]);
                    if(data.get_phone_number() != null && data.get_phone_number().length > 0) errPhone.setText(data.get_phone_number()[0]);
                    if(data.get_password() != null && data.get_password().length > 0) errPassword.setText(data.get_password()[0]);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("edit_member", t.toString());
                saveInputText(token);
            }
        });
    }

    private void onClickRequestPermission() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openGallery();
            return;
        }
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            String [] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission, MY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture !"));
    }

    private void fetchData(String token) {
        ApiService.apiService.getInfoUser(token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User res = response.body();
                if(res.getStatus() == 200) {
                    UserRO user = res.getData();
                    if(user.getAvatar() != null && user.getAvatar().length> 0) {
                        String url = user.getAvatar()[0].getUrl().replace("http://127.0.0.1:8000", "http://10.0.2.2:8000");
                        Picasso.get().load(url).into(imgAvatar);
                    }
                    edtEmail.setText(user.getEmail());
                    edtName.setText(user.getName());
                    edtPhone.setText(user.getPhone_number());
                    edtDob.setText(user.getDob());
                    id = user.getId();
                    if(user.getGender() == 1) rdoMale.setChecked(true);
                    else rdoFemale.setChecked(true);
                } else {
                    Toast.makeText(EditMemberActivity.this, "Error!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                fetchData(token);
            }
        });

    }

    public void openDateDialog(View view) {
        @SuppressLint("SetTextI18n") DatePickerDialog.OnDateSetListener listener = (datePicker, year, month, day) -> {
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);
            String d = day < 10 ? "0" + day : day + "";
            String m = month < 10 ? "0" + month : month + "";
            ((EditText) view).setText(d + "-" + m + "-"+ year); // Format hiển thị lên textbox
        };
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(this, listener, year, month, day);
        dpd.show(); // Hiển thị dialog chọn ngày tháng
    }

    private void init() {
        imgAvatar = (CircleImageView) findViewById(R.id.imgAvatar);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtName = (EditText) findViewById(R.id.edtName);
        edtDob = (EditText) findViewById(R.id.edtDob);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        rdoMale = (RadioButton) findViewById(R.id.rdoMale);
        rdoFemale = (RadioButton) findViewById(R.id.rdoFemale);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnEditImage = (ImageButton) findViewById(R.id.btnEditImage);
        errEmail = (TextView) findViewById(R.id.errEmail);
        errName = (TextView) findViewById(R.id.errName);
        errPhone = (TextView) findViewById(R.id.errPhone);
        errDob = (TextView) findViewById(R.id.errDob);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        errPassword = (TextView) findViewById(R.id.errPassword);
    }
}