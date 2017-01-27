package com.chaoxianzuapp.shawn.app.db;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * Created by shawn on 2015-08-08.
 */
public class RemoteDB {

    public static String getHttpJsonData(String path, String params) throws Exception{

        String ResultStr = "";

        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //conn.setReadTimeout(20000);
        //conn.setConnectTimeout(30000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        if(!"".equals(params)) {

            byte[] bypes = params.getBytes();
            conn.getOutputStream().write(bypes);
        }

        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){

            StringBuilder outputBuffer = new StringBuilder();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line = null;

            while ((line = reader.readLine()) != null){
                outputBuffer.append(line);
            }

            reader.close();
            conn.disconnect();

            ResultStr = outputBuffer.toString();
        }/*else{

            ResultStr = String.valueOf(resCode);
        }*/
        return ResultStr;
    }

    /*
    public static String setUploadFile(String path, String params ,String imageFilePath) {

        String result = null;
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";

        try {

            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", "utf-8"); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+ BOUNDARY);

            OutputStream os = conn.getOutputStream();

            //if(!"".equals(params)) {

            //    byte[] bypes = params.getBytes();
           //     os.write(bypes);
           // }

            if(imageFilePath != null){

                File file = new File(imageFilePath);

                DataOutputStream st = new DataOutputStream(os);

                StringBuilder sb = new StringBuilder(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                sb.append("Content-Disposition: form-data; name=\"bf_file[]\"; filename=\""+ file.getName() + "\"");
                sb.append(LINE_END);
                sb.append("Content-Type: application/octet-stream; charset=utf-8");
                sb.append(LINE_END);

                st.write(sb.toString().getBytes());

                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len;
                while ((len = is.read(bytes)) != -1) {
                    st.write(bytes, 0, len);
                }
                is.close();
                st.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                st.write(end_data);
                st.flush();
            }

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

               StringBuilder outputBuffer = new StringBuilder();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String line = null;

                while ((line = reader.readLine()) != null){
                    outputBuffer.append(line);
                }

                reader.close();
                conn.disconnect();

                result = outputBuffer.toString();




            } else {
                Log.e("xxxxeeeee", "request error");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }*/
}
