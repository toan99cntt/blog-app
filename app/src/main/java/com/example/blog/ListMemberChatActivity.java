package com.example.blog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.blog.adapter.AdapterListChat;
import com.example.blog.adapter.OnMemberChatClickListener;
import com.example.blog.api.ApiService;
import com.example.blog.model.Member;
import com.example.blog.model.MemberRO;
import com.example.blog.model.Message;
import com.example.blog.model.MessageRO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListMemberChatActivity extends AppCompatActivity implements OnMemberChatClickListener {
    private RecyclerView recyclerView;
    private List<MessageRO> mLst;
    private AdapterListChat mAdapter;
    private EditText edtSearch;
    private TextView btnSearch;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_friend);
        btnSearch = findViewById(R.id.btnSearch);
        edtSearch = findViewById(R.id.edtSearch);
        recyclerView = findViewById(R.id.recyclerView);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<MessageRO> list = new ArrayList<>();
                for (MessageRO ls: mLst) {
                    if (ls.getReceiver() != null && ls.getSender() != null &&
                            (ls.getReceiver().getName().contains(edtSearch.getText().toString()) ||
                            ls.getSender().getName().contains(edtSearch.getText().toString()))) {
                        list.add(ls);
                    }
                }
                setDataAdapter(list);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ListMemberChatActivity.this);
        String token = sharedPref.getString("token", null);
        id = sharedPref.getInt("id", 0);
        mLst = new ArrayList<>();
        fetchData(token);
    }

    private void setDataAdapter(List<MessageRO> data) {
        mAdapter = new AdapterListChat(data, ListMemberChatActivity.this, id);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ListMemberChatActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    private void fetchData(String token) {
        ApiService.apiService.getListMemeberChat(token).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                Message res = response.body();
                if(res.getStatus() == 200) {
                    mLst = res.getData();
                    setDataAdapter(mLst);
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                fetchData(token);
            }
        });
    }

    @Override
    public void onMemberChatClickListener(MemberRO f) {
        Intent intent = new Intent(ListMemberChatActivity.this, ChatActivity.class);
        intent.putExtra("id_receiver", f.getId());
        intent.putExtra("name", f.getName());
        intent.putExtra("avatar", f.getAvatar().length> 0 ? f.getAvatar()[0].getUrl().replace("http://127.0.0.1:8000", "http://10.0.2.2:8000") : "");
        startActivity(intent);
    }
}