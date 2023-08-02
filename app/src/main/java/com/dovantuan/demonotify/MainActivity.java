package com.dovantuan.demonotify;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Date;

import android.Manifest;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.btn_notify);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //viết code hiện thị notify ở đây
                // Khai báo Intent để nhận tương tác khi bấm vào notify
                Intent intentDemo = new Intent(getApplicationContext(), MessageActivity.class);
                intentDemo.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_SINGLE_TOP);
                intentDemo.putExtra("duLieu", "Dữ liệu gửi từ notify vào activity");
// ***** tạo Stack để gọi activity
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(MainActivity.this);
                stackBuilder.addNextIntentWithParentStack(intentDemo);
// Lấy pendingIntent để truyền vào notify
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(0,
                                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                //ảnh
                Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

// Khởi tạo layout cho Notify
                Notification customNotification = new NotificationCompat.Builder(MainActivity.this, NotifyConfig.CHANEL_ID)
                        .setSmallIcon(android.R.drawable.ic_delete)
                        .setContentTitle("Tiêu đề notify")
                        .setContentText("Nội dung thông báo của notify")
                        .setContentIntent(resultPendingIntent) // intent để nhận tương tác

                        //thiết lập cho ảnh to
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(logo).bigLargeIcon(null))
                        .setLargeIcon(logo)
                        .setColor(Color.RED)
                        .setAutoCancel(true)

                        .build();
// Khởi tạo Manager để quản lý notify
                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MainActivity.this);

// Cần kiểm tra quyền trước khi hiển thị notify
                if (ActivityCompat.checkSelfPermission(MainActivity.this,
                        android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

                    // Gọi hộp thoại hiển thị xin quyền người dùng
                    ActivityCompat.requestPermissions((Activity) MainActivity.this,
                            new String[]{Manifest.permission.POST_NOTIFICATIONS}, 999999);
                    return; // thoát khỏi hàm nếu chưa được cấp quyền
                }
// nếu đã cấp quyền rồi thì sẽ vượt qua lệnh if trên và đến đây thì hiển thị notify
// mỗi khi hiển thị thông báo cần tạo 1 cái ID cho thông báo riêng
                int id_notiy = (int) new Date().getTime();// lấy chuỗi time là phù hợp
//lệnh hiển thị notify
                notificationManagerCompat.notify(id_notiy, customNotification);

            }
        });

        String dulieu = getIntent().getStringExtra("duLieu");
        Toast.makeText(this, "Du lieu:  " + dulieu, Toast.LENGTH_SHORT).show();

    }
}