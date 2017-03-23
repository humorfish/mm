package com.you.mm.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/3/24.
 */

public class RxMeizhi
{
    public static Observable<Uri> saveImageAndGetPathObservable(Context context, String url, String title)
    {
        return Observable.create((ObservableOnSubscribe<Bitmap>) e ->
        {
            Bitmap bitmap = null;
            try
            {
                bitmap = Picasso.with(context).load(url).get();
            }
            catch (IOException exception)
            {
                exception.printStackTrace();
                e.onError(exception);
            }

            e.onNext(bitmap);
            e.onComplete();

        }).flatMap(bitmap ->
        {
            File appDir = new File(Environment.getExternalStorageDirectory(), "meizhi");
            if (! appDir.exists())
                appDir.mkdir();

            String fileName = title.replace('/', '-') + ".jpg";
            File mFile = new File(appDir, fileName);

            try
            {

                FileOutputStream outputStream = new FileOutputStream(mFile);
                assert bitmap != null;
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.flush();
                outputStream.close();
            }
            catch (IOException exception)
            {
                exception.printStackTrace();
            }

            Uri mUri = Uri.fromFile(mFile);
            // 通知图库更新
            Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, mUri);
            context.sendBroadcast(scannerIntent);

            return Observable.just(mUri);
        }).subscribeOn(Schedulers.io());
    }
}
