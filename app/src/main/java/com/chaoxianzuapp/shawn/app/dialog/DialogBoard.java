package com.chaoxianzuapp.shawn.app.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chaoxianzuapp.shawn.app.BasicInfo;
import com.chaoxianzuapp.shawn.app.R;
import com.chaoxianzuapp.shawn.app.requestdata.BoardCommentDel;
import com.chaoxianzuapp.shawn.app.requestdata.BoardDel;

import java.io.File;

/**
 * Created by shawn on 2015-09-06.
 */
public class DialogBoard {

    public static void setCamera(final Context context ,final Activity activity ,final File imageFile, String[] msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout;

        final AlertDialog dialog;

        TextView title;
        TextView album;
        TextView camera;
        TextView delete;
        TextView close;

        layout = inflater.inflate(R.layout.pop_board_camera, null);

        //switch(mode) {
               /* case "CONFIRM":

                    layout = inflater.inflate(R.layout.pop_confirm, null);
                    break;*/
            //default:

                //layout = inflater.inflate(R.layout.pop_board_camera, null);
        //}

        album = (TextView) layout.findViewById(R.id.album);
        camera = (TextView) layout.findViewById(R.id.camera);
        //delete = (TextView) layout.findViewById(R.id.delete);
        close = (TextView) layout.findViewById(R.id.close);

        builder.setView(layout);

        dialog = builder.create();

        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                if (intent.resolveActivity(activity.getPackageManager()) != null) {
                    activity.startActivityForResult(intent, BasicInfo.IMAGE_ALBUM_ACTIVITY);
                }
                dialog.dismiss();
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
                if (intent.resolveActivity(activity.getPackageManager()) != null) {
                    activity.startActivityForResult(intent, BasicInfo.IMAGE_CAPTURE_ACTIVITY);
                }
                dialog.dismiss();
            }
        });
        /*delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });*/
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        title = (TextView) layout.findViewById(R.id.title);

        title.setText(msg[0]);

        dialog.show();
    }
    public static void setDialog(final Context context ,final Activity activity ,final int wr_id,final String mode , String[] msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout;

        final AlertDialog dialog;

        TextView title_;
        TextView msg_;
        TextView ok_;
        TextView close_;

        switch(mode) {
            default:

                layout = inflater.inflate(R.layout.pop_confirm, null);
        }

        ok_ = (TextView) layout.findViewById(R.id.ok_);
        close_ = (TextView) layout.findViewById(R.id.close_);

        builder.setView(layout);

        dialog = builder.create();

        ok_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                switch(mode){
                    case "COMMENT_DEL":
                        new BoardCommentDel(context,activity,wr_id).start();/*댓글 삭제*/
                        break;
                    case "BOARD_DEL":
                        new BoardDel(context,activity,wr_id).start();/*글 삭제*/
                        break;
                }
            }
        });
        close_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        title_ = (TextView) layout.findViewById(R.id.title_);
        msg_ = (TextView) layout.findViewById(R.id.msg_);

        title_.setText(msg[0]);
        msg_.setText(msg[1]);
        ok_.setText(msg[2]);
        close_.setText(msg[3]);

        dialog.show();
    }
}
