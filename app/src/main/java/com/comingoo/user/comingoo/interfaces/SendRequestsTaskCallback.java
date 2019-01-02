package com.comingoo.user.comingoo.interfaces;

public interface SendRequestsTaskCallback {
    void onSendRequestsTaskCallback(int counter, int step);
    void onPickUpRequest(boolean success);
    void normalRequest();
}
