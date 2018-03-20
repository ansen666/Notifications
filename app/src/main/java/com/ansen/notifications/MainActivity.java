package com.ansen.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private int id=1111;
    private int number=1;
    private String channelId="channelId1";//渠道id
    private NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_create_notification).setOnClickListener(this);
        findViewById(R.id.btn_update_notification).setOnClickListener(this);
        findViewById(R.id.btn_delete_notification).setOnClickListener(this);
        findViewById(R.id.btn_custom_notification).setOnClickListener(this);
        findViewById(R.id.btn_suspension_type).setOnClickListener(this);
        findViewById(R.id.btn_notification_channels).setOnClickListener(this);
        findViewById(R.id.btn_select_notification_channels).setOnClickListener(this);
        findViewById(R.id.btn_open_notification_channels).setOnClickListener(this);
        findViewById(R.id.btn_delete_notification_channels).setOnClickListener(this);

        mNotificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //创建通知渠道
            CharSequence name = "渠道名称1";
            String description = "渠道描述1";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;//重要性级别 这里用默认的
            NotificationChannel mChannel = new NotificationChannel(channelId, name, importance);
            mChannel.setDescription(description);//渠道描述
            mChannel.enableLights(true);//是否显示通知指示灯
            mChannel.enableVibration(true);//是否振动

            mNotificationManager.createNotificationChannel(mChannel);//创建通知渠道
        }
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

            mNotificationManager.notify(id, mBuilder.build());
        }else if(v.getId()==R.id.btn_delete_notification){//删除通知
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
            mNotificationManager.notify(id, notification);
        }else if(v.getId()==R.id.btn_suspension_type){//5.0悬挂式通知
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){//版本号必须5.0或5.0以上
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.mipmap.ic_launcher)//小图标
                                .setContentTitle("悬挂式通知-标题")
                                .setContentText("悬挂式通知-内容");

                Intent intent=new Intent(this,NotificationActivity.class);
                PendingIntent ClickPending = PendingIntent.getActivity(this, 0, intent, 0);

                mBuilder.setAutoCancel(true);//点击这条通知自动从通知栏中取消
                mBuilder.setFullScreenIntent(ClickPending, true);//显示悬挂式通知
                mNotificationManager.notify(id, mBuilder.build());
            }else{
                Toast.makeText(this,"Android版本必须>=5.0",Toast.LENGTH_SHORT).show();
            }
        }else if(v.getId()==R.id.btn_notification_channels){//新增/管理通知渠道
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //第二个参数与channelId对应
                Notification.Builder builder = new Notification.Builder(this,channelId);
                //icon title text必须包含，不然影响桌面图标小红点的展示
                builder.setSmallIcon(android.R.drawable.stat_notify_chat)
                        .setContentTitle("通知渠道1->标题")
                        .setContentText("通知渠道1->内容")
                        .setNumber(3); //久按桌面图标时允许的此条通知的数量

                Intent intent=new Intent(this,NotificationActivity.class);
                PendingIntent ClickPending = PendingIntent.getActivity(this, 0, intent, 0);
                builder.setContentIntent(ClickPending);

                mNotificationManager.notify(id,builder.build());
            }
        }else if(v.getId()==R.id.btn_select_notification_channels){//查看通知渠道设置信息
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                Toast.makeText(this,"具体看Log打印信息",Toast.LENGTH_SHORT).show();

                NotificationChannel notificationChannel=mNotificationManager.getNotificationChannel(channelId);
                if(notificationChannel!=null){//防止渠道被删除
                    long[] vibrationPattern=notificationChannel.getVibrationPattern();//震动模式
                    if(vibrationPattern!=null){
                        Log.i("ansen","震动模式:"+vibrationPattern.length);
                    }

                    Uri uri=notificationChannel.getSound();//通知声音
                    Log.i("ansen","通知声音:"+uri.toString());

                    int importance=notificationChannel.getImportance();//通知等级
                    Log.i("ansen","通知等级:"+importance);
                }
            }
        }else if(v.getId()==R.id.btn_open_notification_channels){//打开通知渠道设置
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Intent channelIntent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                channelIntent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                channelIntent.putExtra(Settings.EXTRA_CHANNEL_ID,channelId);//渠道id必须是我们之前注册的
                startActivity(channelIntent);
            }
        }else if(v.getId()==R.id.btn_delete_notification_channels){//删除通知渠道
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationManager.deleteNotificationChannel(channelId);
            }
        }
    }
}
