package com.syb.baiduaip.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;

public class BitmapUtils {

    /**
     * 选择变换
     *
     * @param origin 原图
     * @param alpha  旋转角度，可正可负
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmap(Bitmap origin, float alpha) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(alpha);
        // 围绕原地进行旋转
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }

    public static void drawPoints(Canvas canvas, Paint paint, PointF[] points, float width) {
        if (canvas != null) {
            float radius = Math.max(width / 240, 2);
            for (PointF p : points) {
                paint.setColor(Color.rgb(57, 168, 243));
                canvas.drawCircle(p.x, p.y, radius, paint);
            }

            paint.setColor(Color.rgb(57, 138, 243));
        }
    }

}
