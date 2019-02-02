package me.sagan.magic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.*;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;
import android.util.Log;
import android.os.Vibrator;

public class Tool {
    static SimpleDateFormat sdf;

    static {
        sdf = new SimpleDateFormat( "yyyy-MM-dd'T'HH-mm-ss.SSS'Z'" );
        sdf.setTimeZone( TimeZone.getTimeZone( "UTC" ) );
    }

    public static int saveImageToDCIM(File imageFile, Context context) {
        int result = 0;
        String filename = "IMG_" + sdf.format(new Date()) + "." + getFilenameExtension(imageFile.getName());
        String dst = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+ "/magic/" + filename;
        File dstFile = new File(dst);
        result = moveFile(imageFile, dstFile);
        if( result == 0 ) {
            galleryAddPic(dstFile, context);
        }
        Log.d("fuck", "Save imageFile to " + dst + " result " + result);
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(300);
        return result;
    }
    public static int moveFile(File file, String dst) {
        if( dst.endsWith("/") ) {
            dst += file.getName();
        }
        return moveFile(file, dst);
    }
    public static int moveFile(File file, File newFile) {
        FileChannel outputChannel = null;
        FileChannel inputChannel = null;
        try {
            try {
                outputChannel = new FileOutputStream(newFile).getChannel();
                inputChannel = new FileInputStream(file).getChannel();
                inputChannel.transferTo(0, inputChannel.size(), outputChannel);
                inputChannel.close();
                file.delete();
            } finally {
                if (inputChannel != null) inputChannel.close();
                if (outputChannel != null) outputChannel.close();
            }
        } catch(Exception e ) {
            return 1;
        }
        return 0;
    }
    private static void galleryAddPic(File f, Context context) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }
    public static String getFilenameExtension(String filename) {
        String extension = "";

        int i = filename.lastIndexOf('.');
        if (i > 0) {
            extension = filename.substring(i+1);
        }
        return extension;
    }
}
