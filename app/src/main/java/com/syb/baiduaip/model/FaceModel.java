package com.syb.baiduaip.model;

import android.util.Log;

import com.syb.baiduaip.bean.FaceBean;
import com.syb.baiduaip.facediscern.FaceDetect;
import com.syb.baiduaip.utils.GsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FaceModel implements IFaceModel {

    private List<FaceBean.Face> mFaceList;

    public FaceModel() {
        mFaceList = new ArrayList<>();
    }

    private List<FaceBean.Face> initFromJson(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        String result = jsonObject.getString("result");
        FaceBean faceBean = GsonUtils.fromJson(result, FaceBean.class);
        return new ArrayList<>(faceBean.getFace_list());
    }

    @Override
    public void getFaceInfo(final String path, final ValueCallBack<List<FaceBean.Face>> callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = FaceDetect.faceDetect(path);
                Log.i("sun", "result: " + result);
                try {
                    List<FaceBean.Face> faceList = initFromJson(result);
                    if (!faceList.isEmpty()) {
                        callBack.onSuccess(faceList);
                    } else {
                        callBack.onFail("读取图片人脸信息失败！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.onFail("读取图片人脸信息失败！");
                }
            }
        }, "getFaceInfo").start();
    }

    @Override
    public List<FaceBean.Face> getData() {
        return mFaceList;
    }
}
