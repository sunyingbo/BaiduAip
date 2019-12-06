package com.syb.baiduaip.model;

import com.syb.baiduaip.bean.FaceBean;

import java.util.List;

public interface IFaceModel {

    void getFaceInfo(String path, ValueCallBack<List<FaceBean.Face>> callBack);

    List<FaceBean.Face> getData();

}
