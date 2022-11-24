package com.example.blog;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.blog.adapter.AdapterChat;
import com.example.blog.adapter.AdapterComment;
import com.example.blog.api.ApiService;
import com.example.blog.model.CommentRO;
import com.example.blog.model.Message;
import com.example.blog.model.MessageDetail;
import com.example.blog.model.MessageRO;
import com.example.blog.model.User;
import com.example.blog.util.RealPathUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    private CircleImageView imgAvatar;
    private TextView txtName;
    private ImageView btnBack;
    private RecyclerView recyclerView;
    private List<MessageRO> mLst;
    private AdapterChat mAdapterChat;
    private EditText edtMessage;
    private ImageButton btnSend;
    private ImageButton btnImage;
    private FirebaseFirestore db;
    private int id;
    private ProgressBar progressBar2;
    private int id_receiver;
    private String token;
    private static final int MY_REQUEST_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        id_receiver = intent.getIntExtra("id_receiver", 0);
        String name = intent.getStringExtra("name");
        String avatar = intent.getStringExtra("avatar");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        token = sharedPref.getString("token", null);
        id = sharedPref.getInt("id", 0);

        txtName.setText(name);
        if(!avatar.equals("")) Picasso.get().load(avatar).into(imgAvatar);

        fetchData();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(id_receiver,id, token);
            }
        });

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();
            }
        });

        final DocumentReference docRef = db.collection("" + id).document( "" + id_receiver);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "snapshot=============================================");
                    fetchData();
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    private void sendMessage(int id_receiver,int id, String token) {
        ApiService.apiService.sendMessageText(id_receiver, edtMessage.getText().toString(), token).enqueue(new Callback<MessageDetail>() {
            @Override
            public void onResponse(Call<MessageDetail> call, Response<MessageDetail> response) {
                MessageDetail data = response.body();
                if (response.body().getStatus() == 200) {
                    sendDataToFirebase(data.getData().getSender().getId(), data.getData().getReceiver().getId(), data.getData().getId(), token);
                    mLst.add(data.getData());
                    setAdapter();
                    edtMessage.setText("");
                } else {
                    Log.d(TAG, "onResponse: ");
                }
            }

            @Override
            public void onFailure(Call<MessageDetail> call, Throwable t) {
                sendMessage(id_receiver,id, token);
            }
        });
    }

    private void fetchData() {
        ApiService.apiService.getMessagesByReceiver(id_receiver, token).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                Message res = response.body();
                if(res.getStatus() == 200) {
                    progressBar2.setVisibility(View.GONE);
                    mLst = res.getData();
                    setAdapter();
                } else {
                    Log.d(TAG, "onResponse: errr");
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                fetchData();
            }
        });
    }

    private void sendDataToFirebase(int idSender, int idReceiver, int idMessage, String token) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", idMessage+"");
        db.collection("" + idReceiver).document("" + idSender)
        .set(data)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//               fetchData(idReceiver, token);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error writing document", e);
            }
        });
    }

    private void setAdapter() {
        mAdapterChat = new AdapterChat(mLst, id_receiver);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapterChat);
        recyclerView.scrollToPosition(mLst.size() - 1);
    }

    private void init() {
        imgAvatar = (CircleImageView) findViewById(R.id.imgAvatar);
        txtName = (TextView) findViewById(R.id.txtName);
        btnBack = findViewById(R.id.btnBack);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        edtMessage = (EditText) findViewById(R.id.edtMessage);
        btnSend = (ImageButton) findViewById(R.id.btnSend);
        btnImage = findViewById(R.id.btnImage);
        progressBar2 = findViewById(R.id.progressBar2);
    }

    //open dialog choose image

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
                        if(uri != null) {
                            sendMessageImage(uri);
                        }
                    }
                }
            }
    );

    private void sendMessageImage(Uri uri) {
        String path = RealPathUtil.getRealPath(ChatActivity.this, uri);
        File file = new File(path);
        RequestBody requestBodyAvatar = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part muPart = MultipartBody.Part.createFormData("images[]", file.getName(), requestBodyAvatar);
        ApiService.apiService.sendMessageImage(id_receiver, muPart, token).enqueue(new Callback<MessageDetail>() {
            @Override
            public void onResponse(Call<MessageDetail> call, Response<MessageDetail> response) {
                MessageDetail res = response.body();
                if(res.getStatus() == 200) {
                    MessageRO data = res.getData();
                    sendDataToFirebase(data.getSender().getId(), data.getReceiver().getId(), data.getId(), token);
                    mLst.add(data);
                    setAdapter();
                } else {
                    Log.d(TAG, "onResponse: " + res.getStatus());
                }
            }

            @Override
            public void onFailure(Call<MessageDetail> call, Throwable t) {
                sendMessageImage(uri);
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
}