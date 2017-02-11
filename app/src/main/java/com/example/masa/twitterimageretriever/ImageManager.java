//
//package com.example.masa.twitterimageretriever;
//
//import android.content.ContentResolver;
//import android.content.ContentValues;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.util.Log;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//
//
//public class ImageManager {
//
//    private Context mContext;
//    private String fileFullPath;
//
//    public ImageManager(Context context) {
//        mContext = context;
//    }
//
//    /**
//     * 画像の保存
//     * @param bitmap
//     * @param albumName
//     */
//    public void save(Bitmap bitmap, String albumName) {
//
//        if (!canUseSd()) {
//            Log.e("Error", "Can't use SD Card");
//            return;
//        }
//
//        saveToSd(getSdStorageDir(albumName), bitmap);
//    }
//
//
//    /**
//     * SDカードに画像保存
//     * @param dir
//     * @param bitmap
//     */
//    private void saveToSd(File dir, Bitmap bitmap) {
//        String fileName = getFileName();
//        fileFullPath = dir.getAbsolutePath() + "/" + fileName;
//        try {
//            // 保存処理
//            FileOutputStream fos = new FileOutputStream(fileFullPath);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//            fos.flush();
//            fos.close();
//        } catch (Exception e) {
//            Log.e("Error", "" + e.toString());
//        } finally {
//            // アルバムに反映
//            addGallery(fileName);
//        }
//    }
//
//    /**
//     * 保存した画像をギャラリーに追加
//     * @param fileName
//     */
//    private void addGallery(String fileName) {
//        try {
//            ContentValues values = new ContentValues();
//            ContentResolver contentResolver = mContext.getContentResolver();
//            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
//            values.put(MediaStore.Images.Media.TITLE, fileName);
//            values.put("_data", fileFullPath);
//            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//        } catch (Exception e) {
//            Log.e("Error", "" + e);
//        }
//    }
//
//    /**
//     * 画像のファイル名を日付から生成し取得
//     * @return
//     */
//    private String getFileName() {
//        Date mDate = new Date();
//        SimpleDateFormat fileNameFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH);
//        String fileName = fileNameFormat.format(mDate) + ".jpg";
//
//        return fileName;
//    }
//
//    /**
//     * SDカードのストレージパス取得
//     * @param albumName
//     * @return
//     */
//    private File getSdStorageDir(String albumName) {
//        File dir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), albumName);
//
//        if (!dir.exists()) {
//            if (!dir.mkdirs()) {
//                Log.e("Error", "Directory not created");
//            }
//        }
//
//        return dir;
//    }
//
//    /**
//     * SDカードが読み込み可能か
//     * @return
//     */
//    public boolean canReadSd() {
//
//        String status = Environment.getExternalStorageState();
//        if (!status.equals(Environment.MEDIA_MOUNTED)) {
//            return false;
//        }
//
//        File file = Environment.getExternalStorageDirectory();
//        if (file.canRead()){
//            return true;
//        }
//
//        return false;
//    }
//
//    /**
//     * SDカードに書き込み可能か
//     * @return
//     */
//    public boolean canWriteSd() {
//        String status = Environment.getExternalStorageState();
//        if (!status.equals(Environment.MEDIA_MOUNTED)) {
//            return false;
//        }
//
//        File file = Environment.getExternalStorageDirectory();
//        if (file.canWrite()){
//            return true;
//        }
//
//        return false;
//    }
//
//    /**
//     * SDカードが使用可能か
//     * @return
//     */
//    public boolean canUseSd() {
//        return canReadSd() && canWriteSd();
//    }
//}