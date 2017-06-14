package com.mao.model;


import android.graphics.Matrix;

/**
 * Created by Administrator on 2017/6/9.
 */

public class Product {

    private int id;
    /**本地保存的key*/
    private String key;
    /**商品图片*/
    private String photo;

    private Matrix photoMatrix;

    private Matrix stickerMatrix;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Matrix getPhotoMatrix() {
        return photoMatrix;
    }

    public void setPhotoMatrix(Matrix photoMatrix) {
        this.photoMatrix = photoMatrix;
    }

    public Matrix getStickerMatrix() {
        return stickerMatrix;
    }

    public void setStickerMatrix(Matrix stickerMatrix) {
        this.stickerMatrix = stickerMatrix;
    }
}
