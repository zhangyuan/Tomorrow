package com.nextcloudmedia.tomorrow;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nextcloudmedia.tomorrow.models.Post;
import com.nextcloudmedia.tomorrow.utils.GetPostCallback;

public class PostDetailsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        Intent intent = getIntent();
        String postTitle = intent.getStringExtra(MainActivity.POST_TITLE_MESSAGE);
        String postId = intent.getStringExtra(MainActivity.POST_ID_MESSAGE);

        setTitle(postTitle);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment(postTitle, postId))
                    .commit();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.post_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public class PlaceholderFragment extends Fragment {
        private String postTitle;
        private String postId;

        public PlaceholderFragment(String postTitle, String postId) {
            this.postTitle = postTitle;
            this.postId = postId;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_post_details, container, false);

            TextView titleTextView = (TextView)rootView.findViewById(R.id.titleTextView);
            final TextView contentTextView = (TextView)rootView.findViewById(R.id.contentTextView);

            titleTextView.setText(postTitle);

            Post.initFromAVObjectId(postId, new GetPostCallback() {
                @Override
                public void done(Post post) {
                    contentTextView.setText(post.getContent());
                }
            });
            return rootView;
        }
    }

}
