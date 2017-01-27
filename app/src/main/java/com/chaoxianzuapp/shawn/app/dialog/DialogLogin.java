package com.chaoxianzuapp.shawn.app.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.chaoxianzuapp.shawn.app.R;
import com.chaoxianzuapp.shawn.app.member.Terms;
import com.chaoxianzuapp.shawn.app.requestdata.Login;
import com.chaoxianzuapp.shawn.app.util.BaseUtil;

/**
 * Created by shawn on 2015-08-22.
 */
public class DialogLogin {


    public static void login(final Context context,final Activity activity) { //,String mode , String[] msg

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout;

        layout = inflater.inflate(R.layout.pop_login, null);

        final EditText mb_id = (EditText) layout.findViewById(R.id.mb_id);
        final EditText mb_password = (EditText) layout.findViewById(R.id.mb_password); //비번 input

        final CheckBox auto_login = (CheckBox) layout.findViewById(R.id.auto_login); // 자동 로그인

        CheckBox show_pw = (CheckBox) layout.findViewById(R.id.show_pw);
        TextView login_button = (TextView)layout.findViewById(R.id.login_button);
        TextView close_button = (TextView)layout.findViewById(R.id.close_button);
        TextView join_button = (TextView)layout.findViewById(R.id.join_button);//회원 가입

        show_pw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    mb_password.setTransformationMethod(null);
                else
                    mb_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        builder.setView(layout);

        final AlertDialog dialog = builder.create();

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mb_id_ = mb_id.getText().toString();
                String mb_password_ = mb_password.getText().toString();
                if("".equals(mb_id_) == false && "".equals(mb_password_) == false) {

                    new Login(context ,activity ,mb_id_ , BaseUtil.md5(mb_password_) , auto_login.isChecked()).start();

                    dialog.dismiss();
                }
            }
        });
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toJoin = new Intent(context, Terms.class);
                context.startActivity(toJoin);
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
