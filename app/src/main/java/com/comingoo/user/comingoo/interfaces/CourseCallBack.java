package com.comingoo.user.comingoo.interfaces;

import android.location.Location;

public interface CourseCallBack {
    void onCourseCallBack(String courseIDT, String statusT,
             String clientIdT,
             String driverIDT,
             Location driverLocT,
             Location startLocT,
             String driverPhone,
             String driverImage,
             String userLevel,
             String startText,
             String endText,
             String driverName,
             String driverCarName,
             String driverCarDescription,
             String totalRatingNumber);
}
