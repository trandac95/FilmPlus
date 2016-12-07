package dph.com.filmplus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FimlWatch extends AppCompatActivity {

    ListView lsvVideo;
    VideoAdapter adapter;
    ArrayList<Video> dsVideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiml_watch);
        //addcontrol();
    }
    void addcontrol()
    {
        lsvVideo = (ListView) findViewById(R.id.lsvFiml);
        dsVideo = new ArrayList<>();
        dsVideo.add(new Video("Fiml 1","a"));
        adapter = new VideoAdapter(FimlWatch.this,R.layout.fiml_item,dsVideo);
        lsvVideo.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}
