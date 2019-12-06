package com.syb.baiduaip.presenter;

import android.app.Activity;

import com.syb.baiduaip.bean.FaceBean;
import com.syb.baiduaip.model.FaceModel;
import com.syb.baiduaip.model.IFaceModel;
import com.syb.baiduaip.model.ValueCallBack;
import com.syb.baiduaip.view.IViewCallBack;

import java.util.List;

public class FacePresenter {

    private IFaceModel mFaceModel;
    private IViewCallBack mViewCallBack;

    public FacePresenter(IViewCallBack callBack) {
        mFaceModel = new FaceModel();
        this.mViewCallBack = callBack;
    }

    public void initData(final Activity activity, String path) {
        mFaceModel.getFaceInfo(path, new ValueCallBack<List<FaceBean.Face>>() {
            @Override
            public void onSuccess(List<FaceBean.Face> faces) {
                mFaceModel.getData().clear();
                mFaceModel.getData().addAll(faces);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mViewCallBack.setFaceImage();
                    }
                });
            }

            @Override
            public void onFail(String message) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mViewCallBack.setImage();
                    }
                });
            }
        });
    }

    public List<FaceBean.Face> getAdapterData() {
        return mFaceModel.getData();
    }

}
