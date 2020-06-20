package com.stp.music_player.BroadcaseReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.stp.music_player.MainActivity;
import com.stp.music_player.fragment.Playing;

public class NotiBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Playing.ACTION_NEXT)){
//                Toast.makeText(context,"NEXT",Toast.LENGTH_SHORT).show();
                ((MainActivity)context).mPlaying.onClickButtonNext();
            }
            else if(intent.getAction().equals(Playing.ACTION_PAUSE)) {
//                Toast.makeText(context,"PAUSE",Toast.LENGTH_SHORT).show();
                ((MainActivity)context).mPlaying.onClickButtonPlay();

            }
            else if(intent.getAction().equals(Playing.ACTION_PLAY)){
//                Toast.makeText(context,"PLAY",Toast.LENGTH_SHORT).show();
                ((MainActivity)context).mPlaying.onClickButtonPlay();

            }else if(intent.getAction().equals(Playing.ACTION_PREVIOUS)){
//                Toast.makeText(context,"BACK",Toast.LENGTH_SHORT).show();
                ((MainActivity)context).mPlaying.onClickButtonBack();


            }
    }
}
