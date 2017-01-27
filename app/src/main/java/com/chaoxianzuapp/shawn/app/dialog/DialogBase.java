package com.chaoxianzuapp.shawn.app.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chaoxianzuapp.shawn.app.R;

/**
 * Created by shawn on 2015-08-09.
 */
public class DialogBase {

    //프로그레스
    private static ProgressDialog progress;
    public static void setProgress(int mode ,Context context,String msg){
        progress = new ProgressDialog(context);
        progress.setMessage(msg);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCanceledOnTouchOutside((mode == 1 ? false : true));
        progress.show();
    }
    public static ProgressDialog getProgress(){
        return progress;
    }

    //기본 alert,다
    public static void setDialog(final Context context ,final Activity activity ,String mode , String[] msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if("A".equals(mode)) {//경고 메시지

            builder.setMessage(msg[0]).setCancelable(false).setPositiveButton(msg[1], new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    //BasicInfo.STATE_NETWORK = false;

                    //BasicInfo.CLOSE_ALL_PROCESS = true;

                    activity.finish();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }else{

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View layout;

            final AlertDialog dialog;

            switch (mode) {
                case "ALERT":

                    layout = inflater.inflate(R.layout.pop_alert, null);

                    TextView close_ = (TextView)layout.findViewById(R.id.close_);

                    builder.setView(layout);

                    dialog = builder.create();

                    close_.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    TextView title_ = (TextView)layout.findViewById(R.id.title_);
                    TextView msg_ = (TextView)layout.findViewById(R.id.msg_);

                    title_.setText(msg[0]);
                    msg_.setText(msg[1]);
                    close_.setText(msg[2]);

                    dialog.show();
                    break;
                default:/*종료,댓글삭제*/

                    TextView title_2;
                    TextView msg_2;
                    TextView ok_;
                    TextView close_2;

                   /* switch(mode) {
                        case "CONFIRM":*/

                            layout = inflater.inflate(R.layout.pop_confirm, null);
                            /*break;
                        default:

                            layout = inflater.inflate(R.layout.pop_confirm, null);
                    }*/

                ok_ = (TextView) layout.findViewById(R.id.ok_);
                close_2 = (TextView) layout.findViewById(R.id.close_);

                builder.setView(layout);

                dialog = builder.create();

                ok_.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        activity.finish();
                    }
                });
                close_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                title_2 = (TextView) layout.findViewById(R.id.title_);
                msg_2 = (TextView) layout.findViewById(R.id.msg_);

                title_2.setText(msg[0]);
                msg_2.setText(msg[1]);
                ok_.setText(msg[2]);
                close_2.setText(msg[3]);

                dialog.show();
            }
        }
    }
}
