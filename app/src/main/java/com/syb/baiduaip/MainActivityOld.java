package com.syb.baiduaip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.syb.baiduaip.view.CircleImageView;

public class MainActivityOld extends AppCompatActivity {

    private static final int PHOTO_REQUEST_CAREMA = 1;
    private static final int PHOTO_REQUEST_GALLERY = 2;
    private static final int PHOTO_REQUEST_CUT = 3;
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private File tempFile;
    private Uri photoOutputUri;
    private CircleImageView headIcon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {

        headIcon = (CircleImageView) findViewById(R.id.headIcon);
        headIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                changeHeadIcon();
            }
        });
        changeTheme();
        File file = new File(MainActivityOld.this.getFilesDir(), "_head_icon.jpg");
        if (file.exists()) {
            headIcon.setImageURI(Uri.fromFile(file));
        }
    }

    private void changeTheme() {
        Calendar c = Calendar.getInstance();
        System.out.println(c.get(Calendar.HOUR_OF_DAY));
        if (c.get(Calendar.HOUR_OF_DAY) < 18 && c.get(Calendar.HOUR_OF_DAY) >= 6) {
            headIcon.setImageResource(R.mipmap.img_20190628_194445);
        } else {
            headIcon.setImageResource(R.mipmap.ic_launcher);
        }
    }
    private void changeHeadIcon() {
        final CharSequence[] items = { "相冊", "拍照" };
        AlertDialog dlg = new AlertDialog.Builder(MainActivityOld.this)
                .setTitle("选择图片")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // 这里item是依据选择的方式。
                        if (item == 0) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                                tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);
                                photoOutputUri = FileProvider.getUriForFile(
                                        MainActivityOld.this,
                                        getPackageName() + ".fileprovider",
                                        tempFile);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoOutputUri);
                                startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
                            } else {
                                Toast.makeText(MainActivityOld.this, "未找到存储卡，无法存储照片！",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }).create();
        dlg.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                final Uri uri = data.getData();
                Log.e("图片路径？？", data.getData() + "");
                if (uri != null) {
                    crop(uri);
                }
            }
        } else if (requestCode == PHOTO_REQUEST_CAREMA) {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                crop(photoOutputUri);
            } else {
                Toast.makeText(MainActivityOld.this, "未找到存储卡，无法存储照片！",
                        Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            Bitmap bitmap = null;
            if (photoOutputUri != null) {
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(photoOutputUri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if (data != null) {
                bitmap = data.getParcelableExtra("data");
            }
            if (bitmap == null) return;
            // 创建画笔
            Paint paint = new Paint();
            // 创建一个画布
            Canvas canvas = new Canvas(bitmap);
            // 开始作画
//            canvas.drawBitmap(srcBitmap, new Matrix(), paint);
             canvas.drawLine(20, 30, 50, 80, paint);
             canvas.drawCircle(50, 80, Math.max(640 / 240, 2), paint);
            headIcon.setImageBitmap(bitmap);
            // 保存图片到internal storage
            FileOutputStream outputStream;
            try {
                outputStream = MainActivityOld.this.openFileOutput("_head_icon.jpg",
                        Context.MODE_PRIVATE);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (tempFile != null && tempFile.exists())
                    tempFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void crop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }
}
