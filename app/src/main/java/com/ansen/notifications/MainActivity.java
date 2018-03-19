package com.ansen.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private int id=1111;

    private int number=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_create_notification).setOnClickListener(this);
        findViewById(R.id.btn_update_notification).setOnClickListener(this);
        findViewById(R.id.btn_delete_notification).setOnClickListener(this);
        findViewById(R.id.btn_custom_notification).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_create_notification){
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)//小图标
                            .setContentTitle("标题")
                            .setContentText("内容");

            Intent intent=new Intent(this,NotificationActivity.class);
            PendingIntent ClickPending = PendingIntent.getActivity(this, 0, intent, 0);

            mBuilder.setContentIntent(ClickPending);
            mBuilder.setAutoCancel(true);//点击这条通知自动从通知栏中取消
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(id, mBuilder.build());
        }else if(v.getId()==R.id.btn_update_notification){
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)//小图标
                            .setContentTitle("更新通知-标题"+(++number))
                            .setContentText("更新通知-内容").setNumber(number);

            Intent intent=new Intent(this,NotificationActivity.class);
            PendingIntent ClickPending = PendingIntent.getActivity(this, 0, intent, 0);
            mBuilder.setContentIntent(ClickPending);

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(id, mBuilder.build());
        }else if(v.getId()==R.id.btn_delete_notification){//删除通知
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.cancel(id);//根据id删除通知
//            mNotificationManager.cancelAll();//删除所有通知
        }else if(v.getId()==R.id.btn_custom_notification){
            //RemoteViews加载xml
            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_custom_notification);
            //设置图片 参数1:是我们xml中ImageView设置的id 参数2是资源id
            remoteViews.setImageViewResource(R.id.imageview,R.mipmap.ic_launcher);
            remoteViews.setTextViewText(R.id.tv_title,"这是标题");
            remoteViews.setTextViewText(R.id.tv_content,"这是内容");

            Intent intent=new Intent(this,NotificationActivity.class);
            PendingIntent clickIntent = PendingIntent.getActivity(this, 0, intent, 0);

            Notification notification = new Notification();
            //必须要设置一个图标，通知区域中需要显示
            notification.icon = android.R.drawable.ic_media_play;
            notification.contentView = remoteViews;//自定义布局
            notification.contentIntent = clickIntent;//点击跳转Intent
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(id, notification);
        }
    }
}
