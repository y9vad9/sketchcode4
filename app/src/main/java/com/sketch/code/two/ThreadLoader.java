package com.sketch.code.two;

import android.os.Handler;
import android.os.Message;

public class ThreadLoader {
    Runnable onSuccess;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case 0x10:
                    onSuccess.run();
                    break;
            }
        }
    };
    public ThreadLoader(final Runnable runnable, Runnable onSuccess) {
        this.onSuccess = onSuccess;
        new Thread(new Runnable() {
            @Override
            public void run() {
                runnable.run();
                handler.sendEmptyMessage(0x10);
            }
        }).start();
    }
}
