package dph.com.filmplus;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dph.com.filmplus.Adapter.SDCardVideoRecyclerFragment;
import dph.com.filmplus.Adapter.VideoRecyclerViewFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView lsvVideo;
    VideoAdapter adapter;
    List<Video> dsVideo;
    int position = 0;
    //connectedInternet cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new VideoRecyclerViewFragment())
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //addcontrol();
//        final VideoView vdVideo = (VideoView) findViewById(R.id.vdVideo);
//        vdVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mediaPlayer) {
//                vdVideo.seekTo(position);
//            }
//        });
    }
    void addcontrol()
    {
        /*lsvVideo = (ListView) findViewById(R.id.lst_Video);
        dsVideo = new ArrayList<>();
        dsVideo.add(new Video("Fiml 1","a"));
        adapter = new VideoAdapter(MainActivity.this,R.layout.fiml_item,dsVideo);
        lsvVideo.setAdapter(adapter);
        adapter.notifyDataSetChanged();*/

    }

//    public class GetData extends AsyncTask<Void,Void,Void> {
//        Context context;
//        ProgressDialog pd;
//        GetData(Context context) {
//            this.context = context;
//        }
//get data when load app
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // cho nay chua chac
//            pd = new ProgressDialog(getBaseContext());
//            pd.setTitle("Get data");
//            pd.setMessage("Loading...");
//            pd.setIndeterminate(false);
//            pd.show();
//        }
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            pd.dismiss();
//        }
//
//    }
//
//    void testConnect()
//    {
//        new GetData(getBaseContext()).execute();
//        cd = new connectedInternet(getBaseContext().getApplicationContext());
//
//    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        // xem tat ca
        if (id == R.id.nav_camera) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new SDCardVideoRecyclerFragment())
                    .commit();
        } else if (id == R.id.nav_gallery) {
            // danh sach yeu thich
        } else if (id == R.id.nav_slideshow) {
            // Xem online
            loadFileList();
            onCreateDialog(DIALOG_LOAD_FILE);
        } else if (id == R.id.nav_manage) {
            // them video
        } else if (id == R.id.nav_send) {
            // thong tin ung dung
            final Dialog dialog=new Dialog(MainActivity.this);
            dialog.setTitle("Thông tin ứng dụng");
            dialog.setContentView(R.layout.activity_info_product);
            Button btnClose=(Button) dialog.findViewById(R.id.btnDong);
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private String[] mFileList;
    private File mPath = new File(Environment.getExternalStorageDirectory()+"//Downloads");
    private String mChosenFile;
    private static final String FTYPE = ".txt";
    private static final String FTYPE2 = ".xml";
    private static final int DIALOG_LOAD_FILE = 1000;

    private void loadFileList() {
        try {
            mPath.mkdirs();
        }
        catch(SecurityException e) {
        }
        if(mPath.exists()) {
            FilenameFilter filter = new FilenameFilter() {

                @Override
                public boolean accept(File dir, String filename) {
                    File sel = new File(dir, filename);
                    return filename.contains(FTYPE) || filename.contains(FTYPE2) || sel.isDirectory();
                }

            };
            mFileList = mPath.list(filter);
        }
        else {
            mFileList= new String[0];
        }
    }

    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch(id) {
            case DIALOG_LOAD_FILE:
                builder.setTitle("Chọn tập tin");
                if(mFileList == null) {
                    dialog = builder.create();
                    return dialog;
                }
                builder.setItems(mFileList, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mChosenFile = mFileList[which];

                    }
                });
                break;
        }
        dialog = builder.show();
        return dialog;
    }
}
