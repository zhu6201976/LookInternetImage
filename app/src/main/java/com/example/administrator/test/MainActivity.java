package com.example.administrator.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 网络图片查看器
 */
public class MainActivity extends AppCompatActivity {

    private EditText et_image_url;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_image_url = (EditText) findViewById(R.id.et_image_url);
        iv = (ImageView) findViewById(R.id.iv);

    }

    public void lookImage(View view) {
        final String path = et_image_url.getText().toString().trim();
        if (path.isEmpty()) {
            Toast.makeText(this, "please input the image url", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(3 * 1000);
                    int responseCode = conn.getResponseCode();
                    if (responseCode == 200) {
                        InputStream inputStream = conn.getInputStream();
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        // 主线程更新UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                iv.setImageBitmap(bitmap);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
