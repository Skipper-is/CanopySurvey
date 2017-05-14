package com.skipper.canopysurvey;

import java.sql.Blob;

/**
 * Created by Skipper on 14/05/2017.
 */

public class CoverRecord {

    int _id;
    float _cover;
    double _lat, _lng;
    byte[] _image;

    public CoverRecord(){

    }

    public CoverRecord(int id,  byte[] image, float cover, double lat, double lng){
        this._id= id;
        this._cover = cover;
        this._lat = lat;
        this._lng = lng;
        this._image = image;

    }

    public CoverRecord(byte[] image, float cover, double lat, double lng ){
        this._cover = cover;
        this._lat = lat;
        this._lng = lng;
        this._image = image;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public float get_cover() {
        return _cover;
    }

    public void set_cover(float _cover) {
        this._cover = _cover;
    }

    public double get_lat() {
        return _lat;
    }

    public void set_lat(double _lat) {
        this._lat = _lat;
    }

    public double get_lng() {
        return _lng;
    }

    public void set_lng(double _lng) {
        this._lng = _lng;
    }

    public byte[] get_image() {
        return _image;
    }

    public void set_image(byte[] _image) {
        this._image = _image;
    }
}
