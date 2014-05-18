package com.nextcloudmedia.tomorrow.models;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;

public class ReplyLog {
    String deviceId;
    String line1Number;
    String subscriberId;
    String replyID;
    AVObject avObject;

    public String getReplyID() {
        return replyID;
    }

    public ReplyLog(String replyID, String deviceId, String line1Number, String subscriberId) {
        this.replyID = replyID;
        this.deviceId = deviceId;
        this.line1Number = line1Number;
        this.subscriberId = subscriberId;

        avObject = new AVObject("ReplyLog");
    }

    public void saveInBackground(){
        assignAvObject();
        avObject.saveInBackground();
    }

    private void assignAvObject()
    {
        avObject.put("deviceId", deviceId);
        avObject.put("line1Number", line1Number);
        avObject.put("subscriberId", subscriberId);
        avObject.put("reply", AVObject.createWithoutData("Reply", getReplyID()));
    }
}
