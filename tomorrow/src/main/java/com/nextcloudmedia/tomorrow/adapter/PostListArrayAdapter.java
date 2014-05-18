package com.nextcloudmedia.tomorrow.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.nextcloudmedia.tomorrow.R;
import com.nextcloudmedia.tomorrow.models.Post;
import com.nextcloudmedia.tomorrow.utils.DownloadPostImageCallback;

import java.util.List;

public class PostListArrayAdapter extends ArrayAdapter<Post> {
    Context context;
    int resource;
    List<Post> posts;

    public PostListArrayAdapter(Context context, int resource, List<Post> posts) {
        super(context, resource, posts);
        this.resource = resource;
        this.posts = posts;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout newView;
        Post post = posts.get(position);

        if (convertView == null) {
            newView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;

            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(inflater);
            layoutInflater.inflate(resource, newView, true);
        } else {
            newView = (LinearLayout) convertView;
        }

        final ImageView imageView = (ImageView) newView.findViewById(R.id.postListEntryImageView);
        imageView.setImageResource(android.R.color.transparent);

        TextView textView = (TextView) newView.findViewById(R.id.postListEntryTextView);

        if(post.getImageFile() != null) {
            post.downloadImageFile(context.getCacheDir(), new DownloadPostImageCallback() {
                @Override
                public void done(Bitmap bitmap) {
                    imageView.setImageBitmap(bitmap);
                }
            });
        }

        textView.setText(post.getTitle());
        return newView;
    }
}
