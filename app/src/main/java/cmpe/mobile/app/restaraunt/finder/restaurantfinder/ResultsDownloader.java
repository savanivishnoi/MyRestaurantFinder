package cmpe.mobile.app.restaraunt.finder.restaurantfinder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by saipranesh on 22-Mar-16.
 */
public class ResultsDownloader<Token> extends HandlerThread {

    private static final String TAG = "cmpe.mobile.app.restaraunt.finder.restaurantfinder.ResultsDownloader";
    private static final int MESSAGE_DOWNLOAD = 0;

    Handler mHandler;
    Handler mResponseHandler;

    Map<Token, String> requestMap = Collections.synchronizedMap(new HashMap<Token, String>());
    Listener<Token> mListener;

    public interface  Listener<Token>{
        void ThumbnailDownloaded(Token token, Bitmap thumbnail);
    }

    public void setListener(Listener<Token> listener){
        mListener = listener;
    }

    public ResultsDownloader(Handler responseHandler) {
        super(TAG);
        mResponseHandler = responseHandler;
    }

    @Override
    protected void onLooperPrepared() {
        mHandler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                if(msg.what == MESSAGE_DOWNLOAD){

                    Token token = (Token)msg.obj;
                  //  Log.i("TAG","Got a request Url from : "+  requestMap.get(token));
                    handleRequest(token);

                }
            }
        };

    }

    private void handleRequest(final Token token) {


        try {
            final String url = requestMap.get(token);
            if(url == null){
                return;
            }
            InputStream in = new URL(url).openStream();
            final Bitmap imageIcon = BitmapFactory.decodeStream(in);
            mResponseHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(requestMap.get(token)!= url){
                        return;
                    }
                    requestMap.remove(token);
                    mListener.ThumbnailDownloaded(token,imageIcon);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void queryResults(Token token, String url){
        requestMap.put(token,url);
        mHandler.obtainMessage(MESSAGE_DOWNLOAD,token).sendToTarget();
    }

    public void clearQueue(){
        mHandler.removeMessages(MESSAGE_DOWNLOAD);
        requestMap.clear();
    }


}
