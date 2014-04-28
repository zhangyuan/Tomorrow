package com.nextcloudmedia.tomorrow.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.nextcloudmedia.tomorrow.R;

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

        ImageView imageView = (ImageView) newView.findViewById(R.id.postListEntryImageView);
        TextView textView = (TextView) newView.findViewById(R.id.postListEntryTextView);

        AVObject current = objects.get(position);
        AVFile imageFile = current.getAVFile("image");
        if (imageFile != null) {
            imageView.setImageURI(Uri.parse(imageFile.getUrl()));
        }
        textView.setText(current.getString("title"));

        return newView;
    }
}
