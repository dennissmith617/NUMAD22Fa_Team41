package com.example.a7atyourservice;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a7atyourservice.model.LiftInfo;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DayActivity extends AppCompatActivity {
    private RecyclerView rvDay;
    private Button backButton;
    private List<LiftInfo> infoList = new ArrayList<>();
    private long time;
    private LiftInfoAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        rvDay = findViewById(R.id.day);
        backButton = findViewById(R.id.back_button);
        adapter = new LiftInfoAdapter(this,infoList);
        rvDay.setLayoutManager(new LinearLayoutManager(this));
        rvDay.setAdapter(adapter);
         time = getIntent().getLongExtra("day", 0L);
        Log.e("time",time+"");
        Helpers.getCollectionReferenceForLifting().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                infoList.clear();
                Log.e("list",value.toString());
                List<DocumentSnapshot> datas = value.getDocuments();
                for (int i = 0; i < datas.size(); i++) {
                    LiftInfo liftInfo = datas.get(i).toObject(LiftInfo.class);
                    Log.e("liftName",liftInfo.getLiftName());
                    long afterDay = time+24*60*60*1000;
                    if (liftInfo.getTimestamp().getSeconds()*1000>time&&liftInfo.getTimestamp().getSeconds()*1000<afterDay){
                        infoList.add(liftInfo);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // close out activity
                finish();
            }
        });
    }
}
