package com.fish.easystorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String imageName = "myImage.jpg";
    private String txtName = "myTxt.txt";

    private String sdTextPath;
    private String innerTextPath;
    private String sdTextImg;
    private String innerTextImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_sd_text).setOnClickListener((v) -> {

        });
        findViewById(R.id.btn_sd_pic).setOnClickListener((v) -> {

        });
        findViewById(R.id.btn_inner_text).setOnClickListener((v) -> {

        });
        findViewById(R.id.btn_inner_pic).setOnClickListener((v) -> {

        });

        sdTextPath = Environment.getExternalStorageDirectory() + File.separator + "fish/" + txtName;
        innerTextPath = getFilesDir()+ File.separator + "myfile/" + txtName;

        new Thread(() -> {
            //写入
            testPermission(MainActivity.this);
        }).start();
    }

    //检查权限，并返回需要申请的权限列表
    private List<String> checkPermission(Context context, String[] checkList) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < checkList.length; i++) {
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(context, checkList[i])) {
                list.add(checkList[i]);
            }
        }
        return list;
    }

    //申请权限
    private void requestPermission(Activity activity, String requestPermissionList[]) {
        ActivityCompat.requestPermissions(activity, requestPermissionList, 100);
    }

    //用户作出选择后，返回申请的结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "存储权限申请成功", Toast.LENGTH_SHORT).show();
                        writeFile();
                    } else {
                        Toast.makeText(this, "存储权限申请失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    //测试申请存储权限
    private void testPermission(Activity activity) {
        String[] checkList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        List<String> needRequestList = checkPermission(activity, checkList);
        if (needRequestList.isEmpty()) {
//            Toast.makeText(this, "无需申请权限", Toast.LENGTH_SHORT).show();
            writeFile();
        } else {
            requestPermission(activity, needRequestList.toArray(new String[needRequestList.size()]));
        }
    }

    private void writeFile() {
        writeFile(innerTextPath);
        writeFile(innerTextImg);
        writeFile(sdTextPath);
    }

    private void writeFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
                String content = "content:" + filePath;
                bufferedOutputStream.write(content.getBytes(), 0, content.getBytes().length);
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void writeFileFromImg(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
                String content = "content:" + filePath;
                bufferedOutputStream.write(content.getBytes(), 0, content.getBytes().length);
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}