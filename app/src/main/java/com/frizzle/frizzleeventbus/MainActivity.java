package com.frizzle.frizzleeventbus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.frizzle.eventbus.apt.EventBusIndex;
import com.frizzle.eventbus_annotation.Subscribe;
import com.frizzle.eventbus_annotation.mode.ThreadMode;
import com.frizzle.eventbus_library.EventBus;
import com.frizzle.frizzleeventbus.model.UserInfo;

public class MainActivity extends AppCompatActivity {

    private TextView tv;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 123456) {
                UserInfo user = (UserInfo) msg.obj;
                tv.setText(user.toString());
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().addIndex(new EventBusIndex());
        EventBus.getDefault().register(this);
        tv = findViewById(R.id.tv);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SecondActivity.class));
            }
        });
        findViewById(R.id.sticky).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new UserInfo("frizzle", 10));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void abc(UserInfo user) {
//        tv.setText(user.toString());
//        Log.e("abc", user.toString());
        Message msg = new Message();
        msg.obj = user;
        msg.what = 123456;
        handler.sendMessage(msg);
        Log.e("abc", user.toString());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC, priority = 1)
    public void abc2(UserInfo user) {
        //tv.setText(user.toString());
        Log.e("abc2", user.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        EventBus.clearCaches();
    }
}