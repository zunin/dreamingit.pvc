package dk.dreamingit.pvc;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

public final class GeigerPlayerSound {
        
        private MediaPlayer mp[];
        int mLast;
        
        public GeigerPlayerSound(Context ctx, int num, int resId) {
                mp = new MediaPlayer[num];
                for (int i = 0; i < num; i++) {
                        mp[i] = MediaPlayer.create(ctx, resId);
                        mp[i].setLooping(false);
                        mp[i].setOnErrorListener(new MediaPlayer.OnErrorListener() {

                                @Override
                                public boolean onError(MediaPlayer mp, int what, int extra) {
                                        Log.v("GeigerPlayerSound", "error on media player what=" + what + " extra=" + extra);
                                        return false;
                                }
                                
                        });
                }
                mLast = -1;
                
        }
        
        public void start() {
        		if (mp == null)
        			{return;}
        			
                for (int i = mLast + 1; i < mp.length; i++) {
                		if(mp[i] == null)
                		{return;}
                        if (!mp[i].isPlaying()) {
                                
                                mLast = i;
                                mp[i].start();
                                return;
                        }
                }
                for (int i = 0; i <= mLast; i++) {
                        if (!mp[i].isPlaying()) {
                                mLast = i;
                                
                                mp[i].start();
                                return;
                        }
                }
                
        }
        
        public void stop() {
                for (int i = 0; i < mp.length; i++) {
                        if (mp[i].isPlaying())
                                mp[i].stop();
                }
        }
        
        public void onDestroy() {
                for (int i = 0; i < mp.length; i++) {
                        if (mp[i].isPlaying()) {
                                mp[i].stop();
                        }
                        mp[i].release();
                        mp[i] = null;
                }
        }

}
