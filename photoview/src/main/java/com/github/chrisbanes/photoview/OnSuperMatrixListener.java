package com.github.chrisbanes.photoview;

/**
 * Created by Administrator on 2017/5/17.
 */

public interface OnSuperMatrixListener {

    void postTranslate(float dx,float dy);

    void postScale(float scaleFactor, float focusX, float focusY);

    void postScale(float scaleFactor);

    void setScale(float scaleX,float scaleY, float focalX, float focalY);
}
