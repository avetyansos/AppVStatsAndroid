package com.example.vardanmkrtchyan.screenrecorder;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.screenrecordercore.VideoLytics;

import java.util.ArrayList;

/**
 * Created by Vardan Mkrtchyan on 4/14/2018.
 */

public class MainHolderActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MyDemoAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holder);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        ArrayList<String> mList = new ArrayList<>();
        mList.add("Name1");
        mList.add("Name2");
        mList.add("Name3");
        mList.add("Name4");
        mList.add("Name5");
        mList.add("Name6");
        mList.add("Name7");
        mList.add("Name8");

        adapter = new MyDemoAdapter(mList);
        mRecyclerView = findViewById(R.id.mRyclerView);
       // mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);


        //VideoLytics.start(getApplication());

    }


    public static class MyDemoAdapter extends RecyclerView.Adapter<MyDemoAdapter.MyDemoViewHolder>{

        private ArrayList<String> mList = new ArrayList<>();

        public MyDemoAdapter(ArrayList<String> mList) {
            this.mList = mList;
        }

        @NonNull
        @Override
        public MyDemoAdapter.MyDemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.holder_view_item, parent, false);

            MyDemoViewHolder holder = new MyDemoViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyDemoAdapter.MyDemoViewHolder holder, int position) {
            String s = mList.get(position);
            holder.mTitle.setText(s);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public static class MyDemoViewHolder extends RecyclerView.ViewHolder{

             TextView mTitle;
            public MyDemoViewHolder(View itemView) {
                super(itemView);
                mTitle = itemView.findViewById(R.id.title);
            }
        }
    }


}
