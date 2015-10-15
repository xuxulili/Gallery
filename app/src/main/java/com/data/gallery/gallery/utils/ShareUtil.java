package com.data.gallery.gallery.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Toast;


import com.data.gallery.gallery.R;
import com.data.gallery.gallery.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zhaokaiqiang on 15/4/17.
 */
public class ShareUtil {

    /**
     * ��
     *
     * @param activity
     */
//	public static void sharePicture(Activity activity,String url){
//
//		String[] urlSplits = url.split("\\.");
//
//		File cacheFile = ImageLoader.getInstance().getDiskCache().get(url);
//
//		//��������ڣ���ʹ������ͼ���з���
//		if (!cacheFile.exists()) {
//			String picUrl = url;
//			picUrl = picUrl.replace("mw600", "small").replace("mw1200", "small").replace
//					("large", "small");
//			cacheFile = ImageLoader.getInstance().getDiskCache().get(picUrl);
//		}
//
//		File newFile = new File(CacheUtil.getSharePicName
//				(cacheFile, urlSplits));
//
//		if (FileUtil.copyTo(cacheFile, newFile)) {
//			ShareUtil.sharePicture(activity, newFile.getAbsolutePath(),
//					"�����Լ嵰��ǿ�� " + url);
//		} else {
//			ShowToast.Short(ToastMsg.LOAD_SHARE);
//		}
//	}
    public static void shareText(Activity activity, String shareText) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,
                shareText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(Intent.createChooser(intent, activity.getResources().getString(R
                .string.app_name)));
    }

    public static void sharePicture(Activity activity, String imgPath, String shareText) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        File f = new File(imgPath);
        if (f != null && f.exists() && f.isFile()) {
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
        } else {
//            Toast.makeText(app.getContext(), "�����ͼƬ������", Toast.LENGTH_SHORT).show();
            return;
        }

        //GIFͼƬָ������url������ͼƬָ����Ŀ��ַ
        if (imgPath.endsWith(".gif")) {
            intent.putExtra(Intent.EXTRA_TEXT, shareText);
        } else {
            intent.putExtra(Intent.EXTRA_TEXT, shareText);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(Intent.createChooser(intent, activity.getResources().getString(R
                .string.app_name)));
    }
    public static void ScreenShot(RecyclerView v){
        SimpleDateFormat sdf = new SimpleDateFormat(
                "yyyy-MM-dd_HH-mm-ss", Locale.US);
        String fname = "/sdcard/" + sdf.format(new Date()) + ".png";

        Log.d("debug", "fname = " + fname);
        View view = v.getRootView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        Bitmap bitmap = getBitmapByView(v);

        if (bitmap != null) {
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(fname);
                Log.e("debug", "FileOutputStream ");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e("debug", "bitmap is error");
            }
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

            Log.e("debug", "bitmap compress ok");
        } else {
            Log.e("debug", "bitmap is null");
        }

    }
    /**
     * ��ȡscrollview����Ļ
     * @param recyclerView
     * @return
     */
    public static Bitmap getBitmapByView(RecyclerView recyclerView) {
        int h = 0;
        Bitmap bitmap = null;
        // ��ȡscrollviewʵ�ʸ߶�
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            h += recyclerView.getChildAt(i).getHeight();
            recyclerView.getChildAt(i).setBackgroundColor(
                    Color.parseColor("#ffffff"));
        }
        // ������Ӧ��С��bitmap
        bitmap = Bitmap.createBitmap(recyclerView.getWidth(), h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        recyclerView.draw(canvas);
        return bitmap;
    }
    //��ȡ��ǰ�߶�
    public static Bitmap myShot(Activity activity) {
        // ��ȡwindows������view
        View view = activity.getWindow().getDecorView();
        view.buildDrawingCache();

        // ��ȡ״̬���߶�
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeights = rect.top;
        Display display = activity.getWindowManager().getDefaultDisplay();

        // ��ȡ��Ļ��͸�
        int widths = display.getWidth();
        int heights = display.getHeight();

        // ����ǰ���ڱ��滺����Ϣ
        view.setDrawingCacheEnabled(true);

        // ȥ��״̬��
        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), 0,
                statusBarHeights, widths, heights - statusBarHeights);

        // ���ٻ�����Ϣ
        view.destroyDrawingCache();

        return bmp;
    }

    //��ָ��ͼƬ���뱾��
    public static String saveToSD(Activity activity) throws IOException {
        String dirName = "";
        String fileName = "";
        File file;
        // �ж�sd���Ƿ����
//        Bitmap bitmap=getBitmapByView(activity);
        Bitmap bmp = myShot(activity);
//        Log.e("������ȡ��ͼƬ", bmp + "");
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
        dirName = Environment.getExternalStorageDirectory() + "/myShare/";
        }else {
//            dirName=activity.getCacheDir()+"/myShare/";
            dirName= app.getContext().getFilesDir().getPath()+"/myShare/";
        }



        SimpleDateFormat formatter = new SimpleDateFormat("MM��dd��HHmmss");
        Date curDate = new Date(System.currentTimeMillis());//��ȡ��ǰʱ��
        String str = formatter.format(curDate);
        if (!str.equals("")) {
            fileName = str+".png";
//            Log.e("fileName",fileName);
        }

//        �ж��ļ����Ƿ���ڣ��������򴴽�
        File dir = new File(dirName);
            if (!dir.exists()) {
                dir.mkdir();
//                Log.e("�½������ڵ�·��", dir+"");
            }
        //�г�ָ��Ŀ¼�������ļ�
        if (dir.exists() && dir.isDirectory()) {
            if (dir.listFiles().length != 0) {
                File[] files = dir.listFiles();

                for (File f : files) {
//                    Log.e("��ͼ�ļ��б�", "file is " + f);
                }
            }
        }

        file = new File(dirName,fileName);
        // �ж��ļ��Ƿ���ڣ��������򴴽�
        if (!file.exists()) {
            file.createNewFile();
        }
//        Log.e("����ͼƬ��ַ", file + "");
        FileOutputStream fos = null;
        try {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                fos = new FileOutputStream(file);
            } else {
                fos =app.getContext().openFileOutput(fileName, Context.MODE_WORLD_WRITEABLE | Context.MODE_WORLD_READABLE);
            }
            if (fos != null) {
                // ��һ������ͼƬ��ʽ���ڶ�����ͼƬ�������������������
//                bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
//                Log.e("�洢ͼƬ����", "");
                // ����ر�
                fos.flush();
                fos.close();
                Toast.makeText(app.getContext(),"��ͼ���",Toast.LENGTH_SHORT).show();
                return (dirName + fileName);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
