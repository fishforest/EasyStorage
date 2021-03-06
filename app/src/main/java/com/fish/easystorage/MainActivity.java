package com.fish.easystorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.fish.easystoragelib.fileprovider.EasyFileProvider;

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
    private String sdImgPath;
    private String innerImgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_sd_text).setOnClickListener((v) -> {
            viewFile(sdTextPath);
        });
        findViewById(R.id.btn_sd_pic).setOnClickListener((v) -> {
            viewFile(sdImgPath);
        });
        findViewById(R.id.btn_inner_text).setOnClickListener((v) -> {
            viewFile(innerTextPath);
        });
        findViewById(R.id.btn_inner_pic).setOnClickListener((v) -> {
            viewFile(innerImgPath);
        });

        sdTextPath = Environment.getExternalStorageDirectory() + File.separator + "fish/" + txtName;
        innerTextPath = getFilesDir()+ File.separator + "myfile/" + txtName;

        sdImgPath = Environment.getExternalStorageDirectory() + File.separator + "fish/" + imageName;
        innerImgPath = getFilesDir()+ File.separator + "myfile/" + imageName;

        new Thread(() -> {
            //??????
            testPermission(MainActivity.this);
        }).start();
    }

    private void viewFile(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return;

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        EasyFileProvider.fillIntent(this, new File(filePath), intent, true);

        try {
            startActivity(intent);
        } catch (Exception e) {
            Log.d("tag", "error:" + e.getLocalizedMessage());
        }
    }

    //???????????????????????????????????????????????????
    private List<String> checkPermission(Context context, String[] checkList) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < checkList.length; i++) {
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(context, checkList[i])) {
                list.add(checkList[i]);
            }
        }
        return list;
    }

    //????????????
    private void requestPermission(Activity activity, String requestPermissionList[]) {
        ActivityCompat.requestPermissions(activity, requestPermissionList, 100);
    }

    //?????????????????????????????????????????????
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "????????????????????????", Toast.LENGTH_SHORT).show();
                        writeFile();
                    } else {
                        Toast.makeText(this, "????????????????????????", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    //????????????????????????
    private void testPermission(Activity activity) {
        String[] checkList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        List<String> needRequestList = checkPermission(activity, checkList);
        if (needRequestList.isEmpty()) {
//            Toast.makeText(this, "??????????????????", Toast.LENGTH_SHORT).show();
            writeFile();
        } else {
            requestPermission(activity, needRequestList.toArray(new String[needRequestList.size()]));
        }
    }

    private void writeFile() {
        writeFile(innerTextPath);
        writeFile(sdTextPath);
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.wangyi);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.qq);
        writeFileFromImg(bitmap1, innerImgPath);
        writeFileFromImg(bitmap2, sdImgPath);
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

    private void writeFileFromImg(Bitmap bitmap, String filePath) {
        if (bitmap == null)
            return;
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
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bufferedOutputStream);
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}