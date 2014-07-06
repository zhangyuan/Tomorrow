package com.nextcloudmedia.tomorrow;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.nextcloudmedia.tomorrow.adapter.PostListArrayAdapter;
import com.nextcloudmedia.tomorrow.models.Post;

import net.simonvt.menudrawer.MenuDrawer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class MainActivity extends ActionBarActivity {
    String app_id = "";
    String app_key = "";
    boolean isLoadingPosts = false;
    private MenuDrawer mDrawer;
    private ListView menuListView;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initAVOS();
        initApp();

        createMenu();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new NewsListFragment())
                    .commit();
        }
    }

    private void createMenu() {
        mDrawer = MenuDrawer.attach(this);
        mDrawer.setContentView(R.layout.activity_main);
        mDrawer.setMenuView(R.layout.menu);

        menuListView = (ListView)mDrawer.getMenuView().findViewById(R.id.menuListView);

        String[] items = {"最新", "登录", "设置"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        menuListView.setAdapter(arrayAdapter);

        mDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
        mDrawer.setDrawerIndicatorEnabled(true);
    }

    public void initApp()
    {
        App app = App.getInstance();
        final TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        
        app.setDeviceId(tm.getDeviceId());
        app.setLine1Number(tm.getLine1Number());
        app.setSubscriberId(tm.getSubscriberId());
    }
    public void initAVOS() {
        try {
            InputStream inputStream = getAssets().open("app.properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            app_id = properties.getProperty("app_id");
            app_key = properties.getProperty("app_key");
        } catch (IOException e) {
        }

        AVOSCloud.initialize(this, app_id, app_key);
        AVAnalytics.trackAppOpened(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.toggleMenu();
                return true;
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        final int drawerState = mDrawer.getDrawerState();
        if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
            mDrawer.closeMenu();
            return;
        }

        super.onBackPressed();
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public class NewsListFragment extends Fragment implements AbsListView.OnScrollListener {
        private int visibleLastIndex;
        private PostListArrayAdapter adapter;
        private View loadingView;
        ListView itemsListView;

        public NewsListFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            itemsListView = (ListView) getActivity().findViewById(R.id.itemsListView);

            loadingView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.loading, null);

            itemsListView.setOnScrollListener(this);
            itemsListView.addFooterView(loadingView);

            adapter = new PostListArrayAdapter(getActivity(), R.layout.post_list_entry, new ArrayList<Post>());
            itemsListView.setAdapter(adapter);

            loadPosts(0);
        }

        public void loadPosts(int skip) {
            if (isLoadingPosts) {
                return;
            }

            isLoadingPosts = true;

            AVQuery<AVObject> query = Post.newQuery();
            query.setSkip(skip);
            query.setLimit(5);
            query.orderByDescending("createdAt");

            query.findInBackground(new FindCallback<AVObject>() {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                public void done(final List<AVObject> avObjects, AVException e) {
                    isLoadingPosts = false;
                    if (e != null) {
                        Toast.makeText(getApplicationContext(), "请求错误 :(", Toast.LENGTH_SHORT).show();
                        Log.d("YLog", "查询错误: " + e.getMessage());
                        return;
                    }

                    adapter.addAll(Post.initFromAVObjects(avObjects));
                    if (avObjects.size() == 0) {
                        Toast.makeText(getApplicationContext(), "No more posts.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            if(position == adapter.getCount())
                            {
                                return;
                            }
                            Intent intent = new Intent(getActivity(), PostDetailsActivity.class);

                            Post post = adapter.getItem(position);

                            intent.putExtra(PostDetailsActivity.POST_TITLE_MESSAGE, post.getTitle());
                            intent.putExtra(PostDetailsActivity.POST_ID_MESSAGE, post.getId());

                            startActivity(intent);
                        }
                    });


                }
            });


        }

        @Override
        public void onScrollStateChanged(AbsListView absListView, int scrollState) {
            int lastIndex = adapter.getCount() - 1;

            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex) {
                loadPosts(adapter.getCount());

            }
        }

        @Override
        public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            Log.d("Yuan", "firstVisibleItem = " + firstVisibleItem + "；　visibleItemCount = " + visibleItemCount);

            visibleLastIndex = firstVisibleItem + visibleItemCount - 2;
        }
    }
}
