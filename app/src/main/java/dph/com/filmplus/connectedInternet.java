package dph.com.filmplus;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
/**
 * Created by PhongNT on 06/12/2016.
 */

public class connectedInternet {
    private Context _context;
    public connectedInternet(Context context)
    {
        this._context = context;
    }
    public boolean checkConnectedInternet()
    {
        // create object test connect internet
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
         if(connectivity!=null)
         {
             NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
             NetworkInfo info1 = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
             //test connectd
             if(info!=null || info1!=null)
             {
                 if(info.isConnected() || info1.isConnected())
                     return  true;

             }
         }
        return  false;
    }
}
