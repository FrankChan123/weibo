package com.practise.cq.weibotest.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by CQ on 2016/5/23 0023.
 */
public class ImgUtils {

    public static final int PIC_FROM_ALBUM = 1000;
    public static final int PIC_FROM_CAMERA = 1001;
    public static Uri imgFromCameraUri;

    /**调用系统相机拍照后返回照片*/
    public static void picFromCamera(Activity activity) {
        imgFromCameraUri = createImgUri(activity);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgFromCameraUri);
        activity.startActivityForResult(intent, PIC_FROM_CAMERA);
    }

    /**拍照完成后输出Uri*/
    private static Uri createImgUri(Activity activity) {
        Uri imgPath = null;
        String status = Environment.getExternalStorageState();

        /**获取当前时间，其中long变量作为DATE_TAKEN值，格式化后的字符串作为照片名称*/
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        String imgName = sdf.format(new Date(currentTime));

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.BUCKET_DISPLAY_NAME, imgName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
        values.put(MediaStore.Images.Media.DATE_TAKEN, currentTime);

        if (status.equals(Environment.MEDIA_MOUNTED)){
            imgPath = activity.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }else {
            imgPath = activity.getContentResolver().insert(
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI, values);
        }

        return imgPath;
    }

    /**从相册中选取照片后返回*/
    public static void picFromAlbum(Activity activity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(intent, PIC_FROM_ALBUM);
    }

    public static void deleteImgUri(Context context, Uri uri){
        if (uri != null){
            context.getContentResolver().delete(uri, null, null);
        }
    }

    public static Bitmap getBitmapFromUri(Context context, Uri uri){
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            Bitmap smallBitmap = ThumbnailUtils.extractThumbnail(bitmap, 400, 400);
            return smallBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            if (bitmap != null && (!bitmap.isRecycled())){
                bitmap.recycle();
            }
        }

    }

    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     * @param context
     * @param imageUri
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getAbsolutePath(Context context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT
                && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[] { split[1] };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }

        // MediaStore (and general)
        if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * 将图片保存到SD中
     */
    public static String saveFile(Context context, Bitmap bm, String fileName) throws IOException {

        /**判断SD卡是否可用*/
        String storageState = Environment.getExternalStorageState();
        if(!storageState.equals(Environment.MEDIA_MOUNTED)) {
            ToastUtil.show(context, "未检测到SD卡", Toast.LENGTH_SHORT);
            return null;
        }

        /**创建图片文件路径及文件*/
        File filePath = new File(Environment.getExternalStorageDirectory(), "/weiboTest/images");
        if (!filePath.exists()){
            filePath.mkdirs();
        }

        File imageFile = new File(filePath, fileName);
        if (!imageFile.exists()){
            imageFile.createNewFile();
        }

        /**开启输出流压缩处理*/
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(imageFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();

        /**插入到系统图库中*/
        String insertName = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                imageFile.getAbsolutePath(), fileName, "weiboimg");

        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(imageFile)));

        return insertName;
    }
}
