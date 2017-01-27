package com.chaoxianzuapp.shawn.app.dialog;

/**
 * Created by shawn on 2015-10-30.
 */
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chaoxianzuapp.shawn.app.BasicInfo;
import com.chaoxianzuapp.shawn.app.R;
import com.chaoxianzuapp.shawn.app.util.BaseUtil;

import java.io.File;
import java.util.List;


/**
 * Created by shawn on 2015-10-30.
 */
public class DialogApk {

    private int apkUpdateFlag;
    private Uri apkDownLoadUrl;
    private DownloadManager downloadManager;
    private DownloadManager.Request request;
    private List<String> pathSegments;
    private long latestId = -1;

    public DialogApk(){}

    public void getLaunchApk(final Context context ,final Activity activity ,String appUrl,final String[] msg){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.pop_confirm, null);

        TextView title_ = (TextView)layout.findViewById(R.id.title_);
        title_.setText("알림");
        TextView msg_ = (TextView)layout.findViewById(R.id.msg_);
        msg_.setText(Html.fromHtml(msg[0]));

        TextView ok = (TextView)layout.findViewById(R.id.ok_);
        TextView cancel = (TextView)layout.findViewById(R.id.close_);

        apkDownLoadUrl = Uri.parse(appUrl);
        pathSegments = apkDownLoadUrl.getPathSegments();
        downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        BasicInfo.APP_FILENAME = pathSegments.get(pathSegments.size() - 1);
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();

        BasicInfo.APP_APKPATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + BasicInfo.APP_FILENAME;

        File newApkFile = new File(BasicInfo.APP_APKPATH);

        if(newApkFile.exists()){

            if (BasicInfo.userSet.get("appUrl") != null && !"".equalsIgnoreCase(BasicInfo.userSet.get("appUrl")) && !BasicInfo.APP_APKPATH.equalsIgnoreCase(BasicInfo.userSet.get("appUrl"))) {

                File oldApkFile = new File(BasicInfo.userSet.get("appUrl"));
                if(oldApkFile.exists()) {

                    oldApkFile.delete();
                }
            }

            ok.setText("업그레이드");

            apkUpdateFlag = -1;//apk존재하고 현버전
        }else {

            apkUpdateFlag = 1;//apk가 존재하지 않음
        }

        if(apkUpdateFlag > 0){

            ok.setText("다운로드");
        }

        cancel.setText("나중에 업그레이드");

        builder.setView(layout);
        final AlertDialog d1 = builder.create();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BasicInfo.setAutoLogin(context);
                d1.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (apkUpdateFlag > 0) {//다운로드 시작

                    if (latestId < 0) {

                        request = new DownloadManager.Request(apkDownLoadUrl);
                        request.setTitle("조선족모임 앱 다운로드");
                        request.setDescription(msg[0]);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, BasicInfo.APP_FILENAME);
                        latestId = downloadManager.enqueue(request);
                    }

                    DialogBase.setProgress(1, context, "다운로드 중...");
                } else {//업데이트

                    BaseUtil.setLaunchApk(context, activity);
                }
                d1.dismiss();
            }
        });
        d1.setCanceledOnTouchOutside(false);
        d1.show();
    }
}