package com.nextcloudmedia.tomorrow;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;


public class MainActivity extends ActionBarActivity {
    String app_id = "";
    String app_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAVOS();

        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new NewsListFragment())
                    .commit();
        }
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
    public static class NewsListFragment extends Fragment {
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

            final ListView itemsListView = (ListView) getActivity().findViewById(R.id.itemsListView);

            AVQuery<AVObject> query = new AVQuery<AVObject>("Post");
            query.setLimit(15);
            query.findInBackground(new FindCallback<AVObject>() {
                public void done(List<AVObject> avObjects, AVException e) {
                    if (e == null) {
                        Log.d("成功", "查询到" + avObjects.size() + " 条符合条件的数据");


                        final ArrayList<String> items = new ArrayList<String>();

                        for(int i = 0; i < avObjects.size(); i++){
                            items.add(avObjects.get(i).getString("title"));
                        }

                        final ArrayAdapter<String> aa;

                        aa = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);

                        itemsListView.setAdapter(aa);


                    } else {
                        Log.d("失败", "查询错误: " + e.getMessage());
                    }
                }
            });
        }
    }

}
