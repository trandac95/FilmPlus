package dph.com.filmplus;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.List;

/**
 * Created by PhongNT on 26/11/2016.
 */

public class VideoAdapter extends ArrayAdapter {

    Activity context;
    int resource;
    List<Video> objects;

    Video vd;
    public VideoAdapter(Activity context, int resource, List<Video> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource,null);

        TextView txtTenVideo = (TextView) row.findViewById(R.id.txtTenVideo);
        VideoView vdVideo = (VideoView) row.findViewById(R.id.video_View);

        vd = this.objects.get(position);

        txtTenVideo.setText(vd.getTenvideo());

        MediaController mediaController= new MediaController(context);
        mediaController.setAnchorView(vdVideo);
        Uri uri=Uri.parse("rtsp://r6---sn-a5mekn7k.googlevideo.com/Cj0LENy73wIaNAltcAyvi-QbchMYDSANFC2W-zhYMOCoAUIASARgpLvJvYTWyI1YigELYmxoYlc2ZlBIOWcM/0A3791C487E6906A6D3AD7A5DBEB24082F79498D.9ABFF7DA27139033943B8C9E67797312AD080D3D/yt6/1/video.3gp");
        vdVideo.setMediaController(mediaController);
        vdVideo.setVideoURI(uri);
        vdVideo.requestFocus();
        vdVideo.start();

        return row;
    }
}
