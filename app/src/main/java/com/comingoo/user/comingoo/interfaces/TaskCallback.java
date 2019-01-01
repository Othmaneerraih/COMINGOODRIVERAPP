package com.comingoo.user.comingoo.interfaces;

public interface TaskCallback {
    void oncheckTaskCallBack(Boolean success,
                             String userName,
                             String userImage,
                             String email,
                             String phonerNumber);
}
