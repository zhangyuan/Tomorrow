package com.nextcloudmedia.tomorrow.utils;

import com.avos.avoscloud.SaveCallback;
import com.nextcloudmedia.tomorrow.models.Reply;

/**
 * Created by zhangyuan on 5/11/14.
 */
public abstract class SaveReplyCallback {
    abstract public void done(Reply reply);
}
