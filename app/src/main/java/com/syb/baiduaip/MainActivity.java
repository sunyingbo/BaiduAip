package com.syb.baiduaip;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.syb.baiduaip.bean.FaceBean;
import com.syb.baiduaip.bean.Landmark;
import com.syb.baiduaip.presenter.FacePresenter;
import com.syb.baiduaip.utils.BitmapUtils;
import com.syb.baiduaip.utils.PictureUtils;
import com.syb.baiduaip.view.CircleImageView;
import com.syb.baiduaip.view.IViewCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IViewCallBack {

    private static final int PHOTO_REQUEST_CAREMA = 1;
    private static final int PHOTO_REQUEST_GALLERY = 2;
    private static final int PHOTO_REQUEST_CUT = 3;
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private File tempFile;
    private CircleImageView headIcon;
    private FacePresenter mFacePresenter;
    private String realPathFromUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initPresenter();
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
        File file = new File(MainActivity.this.getFilesDir(), "_head_icon.jpg");
        if (file.exists()) {
            headIcon.setImageURI(Uri.fromFile(file));
        }
    }

    private void initPresenter() {
        mFacePresenter = new FacePresenter(this);
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
        AlertDialog dlg = new AlertDialog.Builder(MainActivity.this)
                .setTitle("选择图片")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // 这里item是依据选择的方式。
                        if (item == 0) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
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
                headIcon.setImageURI(uri);
                if (uri != null) {
                    realPathFromUri = PictureUtils.getRealPathFromUri(MainActivity.this, uri);
                    mFacePresenter.initData(MainActivity.this, realPathFromUri);
                }
            }
        }
    }

    @Override
    public void setFaceImage() {
        if (!TextUtils.isEmpty(realPathFromUri)) {

            int degree = PictureUtils.readPictureDegree(realPathFromUri);

            Bitmap bitmap = BitmapFactory.decodeFile(realPathFromUri).copy(Bitmap.Config.RGB_565, true);

            List<Landmark> landmarkList = new ArrayList<>();
            List<FaceBean.Face> adapterData = mFacePresenter.getAdapterData();
            Log.i("sun", adapterData.toString());
            for (FaceBean.Face face : adapterData) {
                List<Landmark> landmarks = face.getLandmark72();
                landmarkList.addAll(landmarks);
            }

            Paint paint = new Paint();
            Canvas canvas = new Canvas(bitmap);
            PointF[] points = new PointF[landmarkList.size()];
            for(int i = 0 ; i < landmarkList.size(); i++) {
                Landmark landmark = landmarkList.get(i);
                points[i] = new PointF((float) landmark.getX(), (float) landmark.getY());
            }

            BitmapUtils.drawPoints(canvas, paint, points, bitmap.getWidth());

            headIcon.setImageBitmap(BitmapUtils.rotateBitmap(bitmap, degree));
        }
    }

    @Override
    public void setImage() {
        headIcon.setImageBitmap(BitmapFactory.decodeFile(realPathFromUri));
    }
}
