package com.nextcloudmedia.tomorrow.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.GetDataCallback;
import com.nextcloudmedia.tomorrow.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PostListArrayAdapter extends ArrayAdapter<AVObject> {
    Context context;
    int resource;
    List<AVObject> objects;

    public PostListArrayAdapter(Context context, int resource, List<AVObject> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.objects = objects;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout newView;

        if (convertView == null) {
            newView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;

            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(inflater);
            layoutInflater.inflate(resource, newView, true);
        } else {
            newView = (LinearLayout) convertView;
        }

        final ImageView imageView = (ImageView) newView.findViewById(R.id.postListEntryImageView);
        TextView textView = (TextView) newView.findViewById(R.id.postListEntryTextView);

        AVObject current = objects.get(position);
        final AVFile imageFile = current.getAVFile("image");

        final File cacheDir = context.getCacheDir();
        if (imageFile != null) {
            File existedFile = new File(cacheDir, imageFile.getObjectId());
            if(! existedFile.exists()){
                imageFile.getDataInBackground(new GetDataCallback(){

                    public void done(byte[] data, AVException e){
                        Toast.makeText(context, "Image Loaded", Toast.LENGTH_SHORT).show();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                        try {
                            File saveFile = new File(cacheDir, imageFile.getObjectId());
                            FileOutputStream outStream = new FileOutputStream(saveFile);
                            outStream.write(data);
                            outStream.close();
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                        imageView.setImageBitmap(bitmap);
                    }
                });
            } else {
                Bitmap bitmap = BitmapFactory.decodeFile(existedFile.getAbsolutePath());
                imageView.setImageBitmap(bitmap);
            }
        }
        textView.setText(current.getString("title"));

        return newView;
    }
}
