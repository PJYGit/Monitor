package com.bjtu.camerapi.service;

import com.bjtu.camerapi.entity.PictureLine;
import com.bjtu.camerapi.mapper.PicturesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PicturesService {

    private final PicturesMapper picturesDao;

    @Autowired
    public PicturesService(PicturesMapper picturesDao) {
        this.picturesDao = picturesDao;
    }

    public List<PictureLine> query(long starttime, long endtime, int startDeviceID, int endDeviceID, int startTypeID, int endTypeID) {
        return picturesDao.query(starttime, endtime, startDeviceID, endDeviceID, startTypeID, endTypeID);
    }
}
