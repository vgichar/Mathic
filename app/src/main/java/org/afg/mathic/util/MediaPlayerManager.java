package org.afg.mathic.util;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Vojdan on 23-May-15.
 */
public class MediaPlayerManager {
    private static boolean isMuted = false;
    private static MediaPlayer mediaPlayer;
    public static String isMutedSoundKey = "IS_MUTED";

    public static boolean isMuted(){
        return isMuted;
    }

    public static void toggleMute(){
        isMuted = !isMuted;
        if(mediaPlayer != null) {
            if (isMuted) {
                mediaPlayer.setVolume(0, 0);
            } else {
                mediaPlayer.setVolume(1, 1);
            }
        }
    }

    public static void mute(){
        isMuted = true;
        if(mediaPlayer != null) {
            mediaPlayer.setVolume(0, 0);
        }
    }

    public static void unmute(){
        isMuted = false;
        if(mediaPlayer != null) {
            mediaPlayer.setVolume(1, 1);
        }
    }

    public static void play(int resource, Context ctx){
        stop();
        mediaPlayer = MediaPlayer.create(ctx, resource);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        if(isMuted){
            isMuted = !isMuted;
            toggleMute();
        }
    }

    public static void stop(){
        if(mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
    }

    public static void pause(){
        if(mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }

    public static boolean isPlaying(){
        if(mediaPlayer == null){
            return false;
        }
        return mediaPlayer.isPlaying();
    }
}
