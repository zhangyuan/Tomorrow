package com.nextcloudmedia.tomorrow.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.GetDataCallback;
import com.nextcloudmedia.tomorrow.utils.DownloadPostImageCallback;
import com.nextcloudmedia.tomorrow.utils.GetPostCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Post {
    AVObject avObject;

    public Post(AVObject avObject) {
        this.avObject = avObject;
    }

    public AVObject getAvObject() {
        return avObject;
    }

    public String getTitle() {
        return avObject.getString("title");
    }

    public String getContent() {
        return avObject.getString("content");
    }

    public static List<Post> initFromAVObjects(List<AVObject> avObjects) {
        List<Post> posts = new ArrayList<Post>();

        for (int i = 0; i < avObjects.size(); i ++) {
            posts.add(new Post(avObjects.get(i)));
        }

        return posts;
    }

    public static void initFromAVObjectId(String objectId, final GetPostCallback getPostCallback){
        AVQuery<AVObject> query = new AVQuery<AVObject>("Post");

        query.getInBackground(objectId, new GetCallback<AVObject>(){
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    Post post = new Post(avObject);
                    getPostCallback.done(post);
                } else {
                    Log.d("Tomorrow", "Get Post Error: " + e.getMessage());
                }
            }
        });

    }

    public String getId() {
        return avObject.getObjectId();
    }

    public static AVQuery<AVObject> newQuery() {
        return new AVQuery<AVObject>("Post");
    }

    public static File imageFile(File cacheDir, AVFile avFile) {

        return new File(cacheDir, avFile.getObjectId());
    }

    public AVFile getImageFile() {
        return avObject.getAVFile("image");
    }

    public void downloadImageFile(final File cacheDir, final DownloadPostImageCallback callback) {
        final AVFile imageFile = getImageFile();

        File existedFile = Post.imageFile(cacheDir, imageFile);
        if(! existedFile.exists()){
            imageFile.getDataInBackground(new GetDataCallback(){

                public void done(byte[] data, AVException e){
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                    try {
                        File saveFile = Post.imageFile(cacheDir, imageFile);
                        FileOutputStream outStream = new FileOutputStream(saveFile);
                        outStream.write(data);
                        outStream.close();
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    callback.done(bitmap);
                }
            });
        } else {
            Bitmap bitmap = BitmapFactory.decodeFile(existedFile.getAbsolutePath());
            callback.done(bitmap);
        }

    }
}