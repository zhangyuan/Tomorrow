package com.nextcloudmedia.tomorrow;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nextcloudmedia.tomorrow.models.Reply;
import com.nextcloudmedia.tomorrow.models.ReplyLog;
import com.nextcloudmedia.tomorrow.utils.SaveReplyCallback;
import com.nextcloudmedia.tomorrow.utils.SaveReplyLogCallback;

import java.util.jar.Manifest;

public class CreateRelyActivity extends ActionBarActivity {
    String postTitle;
    String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post_rely);

        Intent intent = getIntent();
        postTitle = intent.getStringExtra(PostDetailsActivity.POST_TITLE_MESSAGE);
        postId = intent.getStringExtra(PostDetailsActivity.POST_ID_MESSAGE);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment(postId, postTitle))
                    .commit();
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_post_rely, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case android.R.id.home:
                navigateToReplyDetails();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void navigateToReplyDetails()
    {
        Intent intent = new Intent(this, PostDetailsActivity.class);
        intent.putExtra(PostDetailsActivity.POST_ID_MESSAGE, postId);
        intent.putExtra(PostDetailsActivity.POST_TITLE_MESSAGE, postTitle);
        NavUtils.navigateUpTo(this, intent);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public class PlaceholderFragment extends Fragment {
        String postId;
        String postTitle;

        public PlaceholderFragment(String postId, String postTitle) {
            this.postId = postId;
            this.postTitle = postTitle;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_create_post_rely, container, false);

            final Button button = (Button) rootView.findViewById(R.id.createReplyButton);


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView replyEditText = (TextView) rootView.findViewById(R.id.replyEditText);
                    button.setEnabled(false);

                    Reply reply = new Reply(postId, replyEditText.getText().toString());

                    reply.save(new SaveReplyCallback() {
                        @Override
                        public void done(Reply reply) {
                            App app = App.getInstance();

                            ReplyLog replyLog = new ReplyLog(reply.getId(), app.getDeviceId(), app.getLine1Number(), app.getSubscriberId());
                            replyLog.saveInBackground();

                            Intent intent = new Intent(getActivity(), PostDetailsActivity.class);
                            intent.putExtra(PostDetailsActivity.POST_ID_MESSAGE, postId);
                            intent.putExtra(PostDetailsActivity.POST_TITLE_MESSAGE, postTitle);
                            NavUtils.navigateUpTo(getActivity(), intent);
                        }
                    });
                }
            });

            return rootView;
        }
    }

}
