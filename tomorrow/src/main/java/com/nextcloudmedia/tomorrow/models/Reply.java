package com.nextcloudmedia.tomorrow.models;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.nextcloudmedia.tomorrow.utils.SaveReplyCallback;

/**
 * Created by zhangyuan on 5/11/14.
 */
public class Reply {
    AVObject avObject;
    String content;
    String postId;
    final String CONTENT = "content";
    final String POST = "post";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;

    public Reply(String postId, String content) {
        avObject = new AVObject("Reply");
        this.postId = postId;
        this.content = content;
    }

    public void save(final SaveReplyCallback callback){
        avObject.put(CONTENT, content);
        avObject.put(POST, AVObject.createWithoutData("Post", postId));

        final Reply reply = this;

        avObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null)
                {
                    reply.setId(avObject.getObjectId());
                    callback.done(reply);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
