package com.frizzle.frizzleeventbus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.frizzle.eventbus_annotation.Subscribe;
import com.frizzle.eventbus_annotation.mode.ThreadMode;
import com.frizzle.eventbus_library.EventBus;
import com.frizzle.frizzleeventbus.model.UserInfo;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        findViewById(R.id.post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post();
            }
        });
        findViewById(R.id.sticky).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sticky();
            }
        });
    }

    // 发送事件按钮
    public void post() {
        // 发送消息 / 事件
        EventBus.getDefault().post(new UserInfo("frizzle", 35));
        finish();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                EventBus.getDefault().post(new UserInfo("simon", 35));
//                finish();
//            }
//        }).start();
    }

    // 激活粘性按钮
    public void sticky() {
        EventBus.getDefault().register(this);
        EventBus.getDefault().removeStickyEvent(UserInfo.class);
    }

    // Sticky粘性，美 [ˈstɪki]
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void sticky(UserInfo user) {
        Log.e("sticky", user.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 示例代码
        UserInfo userInfo = EventBus.getDefault().getStickyEvent(UserInfo.class);
        if (userInfo != null) {
            UserInfo info = EventBus.getDefault().removeStickyEvent(UserInfo.class);
            if (info != null) {
                EventBus.getDefault().removeAllStickyEvents();
            }
        }
        EventBus.getDefault().unregister(this);
    }
}