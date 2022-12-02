package com.example.blog;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.blog.api.ApiService;
import com.example.blog.model.BlogDetail;
import com.example.blog.model.BlogRO;
import com.example.blog.model.CreateBlog;
import com.example.blog.model.User;
import com.example.blog.util.RealPathUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateBlogActivity extends AppCompatActivity {
    private static final int MY_REQUEST_CODE = 10;
    private ProgressDialog mProgressDialog;
    private EditText edtTitle;
    private TextView errTitle;
    private EditText edtContent;
    private TextView errContent;
    private TextView txtTitle;
    private Button btnCreateBlog;
    private ImageButton btnClose;
    private CheckBox cbxStatus;
    private Uri mUri;
    private ImageButton btnImage;
    private ImageView image;
    private String token;
    private int id_blog;

    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
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
                            image.setImageBitmap(bitmap);
                        } catch (IOException err) {
                            //
                        }
                    }
                }
            }
    );

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

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture !"));
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_blog);

        edtTitle = (EditText) findViewById(R.id.edtTitle);
        edtContent = (EditText) findViewById(R.id.edtContent);
        btnCreateBlog = (Button) findViewById(R.id.btnCreateBlog);
        errContent = findViewById(R.id.errContent);
        errTitle = findViewById(R.id.errTitle);
        txtTitle = findViewById(R.id.txtTitle);
        btnClose = findViewById(R.id.btnClose);
        btnImage = findViewById(R.id.btnImage);
        cbxStatus = findViewById(R.id.cbxStatus);
        image = findViewById(R.id.image);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        token = sharedPref.getString("token", null);
        if(token == null) return;

        Intent intent = getIntent();
        id_blog = intent.getIntExtra("id_blog", 0);

        if(id_blog > 0) {
            btnCreateBlog.setText("Edit");
            txtTitle.setText("Edit Blog");
            fetchData();
        }

        btnImage.setOnClickListener(view -> {
            onClickRequestPermission();
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errContent.setText("");
                errTitle.setText("");
                finish();
            }
        });

        btnCreateBlog.setOnClickListener(view -> {
            save();
        });
    }

    private void save() {
        String title = edtTitle.getText().toString();
        String content = edtContent.getText().toString();
        RequestBody requestTitle = RequestBody.create(MediaType.parse("multipart/form-data"), title);
        RequestBody requestContent = RequestBody.create(MediaType.parse("multipart/form-data"), content);
        if(id_blog > 0) {
            editBlog(requestTitle, requestContent, muPart());
        } else {
            createBlog(requestTitle, requestContent, muPart());
        }

    }

    private  MultipartBody.Part muPart () {
        if(mUri == null) return null;
        String path = RealPathUtil.getRealPath(CreateBlogActivity.this, mUri);
        File file = new File(path);
        RequestBody requestBodyAvatar = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part muPart = MultipartBody.Part.createFormData("_image", file.getName(), requestBodyAvatar);
        return muPart;
    }

    private void fetchData() {
        ApiService.apiService.getBlogDetail(id_blog, token).enqueue(new Callback<BlogDetail>() {
            @Override
            public void onResponse(Call<BlogDetail> call, Response<BlogDetail> response) {
                BlogDetail res = response.body();
                if(res.getStatus() == 200) {
                    BlogRO blog = res.getData();
                    edtTitle.setText(blog.getTitle());
                    edtContent.setText(blog.getContent());
                    cbxStatus.setChecked(blog.getStatus() == 1);
                    if(blog.getImage() != null && blog.getImage().length> 0) {
                        String url = blog.getImage()[0].getUrl().replace("http://127.0.0.1:8000", "http://10.0.2.2:8000");
                        Picasso.get().load(url).into(image);
                    }
                }
            }

            @Override
            public void onFailure(Call<BlogDetail> call, Throwable t) {
                fetchData();
            }
        });
    }

    private void createBlog(RequestBody title, RequestBody content, MultipartBody.Part muPart) {
        ApiService.apiService.createBlog(title, content, cbxStatus.isChecked()? 1 : 0, muPart, token).enqueue(new Callback<CreateBlog>() {
            @Override
            public void onResponse(Call<CreateBlog> call, Response<CreateBlog> response) {
                CreateBlog res = response.body();
                errContent.setText("");
                errTitle.setText("");
                if(res.getStatus() == 200) {
                    finish();
                } else if (res.getStatus() == 422) {
                    BlogRO data = res.getData();
                    if(data.get_title() != null && data.get_title().length > 0) errTitle.setText(data.get_title()[0]);
                    if(data.get_content() != null && data.get_content().length > 0) errContent.setText(data.get_content()[0]);
                } else {
                    Log.d(TAG, "sai url");
                }
            }

            @Override
            public void onFailure(Call<CreateBlog> call, Throwable t) {
                createBlog(title, content, muPart);
            }
        });
    }

    private void editBlog(RequestBody title, RequestBody content, MultipartBody.Part muPart) {
        ApiService.apiService.editBlog(id_blog, title, content, cbxStatus.isChecked()? 1 : 0, muPart, token).enqueue(new Callback<CreateBlog>() {
            @Override
            public void onResponse(Call<CreateBlog> call, Response<CreateBlog> response) {
                CreateBlog res = response.body();
                errContent.setText("");
                errTitle.setText("");
                if(res.getStatus() == 200) {
                    finish();
                } else if (res.getStatus() == 422) {
                    BlogRO data = res.getData();
                    if(data.get_title() != null && data.get_title().length > 0) errTitle.setText(data.get_title()[0]);
                    if(data.get_content() != null && data.get_content().length > 0) errContent.setText(data.get_content()[0]);
                } else {
                    Log.d(TAG, "sai url");
                }
            }

            @Override
            public void onFailure(Call<CreateBlog> call, Throwable t) {
                editBlog(title, content, muPart);
            }
        });
    }
}