package com.example.masa.twitterimageretriever;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;


public class DeviceUtils {

    public static void saveToFile(Context context, Bitmap bitmap, String folderName) {

        if (!sdcardWriteReady()) {
            Toast.makeText(context, "This device can't save image to SDcard...", Toast.LENGTH_SHORT).show();
            return;
        }


        try {

            // 引数にファイルもしくはディレクトリ名を指定し、該当するFileオブジェクトを生成
            // 要は、/storage/emulated/0/Pictureshogehoge に対し、Fileクラスのオブジェクトである「file」を対応させた。
            // これにより、今後このパスにあるファイルを、fileオブジェクトで扱える(=読み書きできる)ようになる。

//            String path = "";

//            if (context instanceof MainActivity) {
//                path = "Gachikoi";
//            }
//            else {
//                path = "AutoCollect";
//            }

            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" +
                    // Environment.DIRECTORY_PICTURES + "/" + path);
                    Environment.DIRECTORY_PICTURES + "/" + folderName);
                    //context.getResources().getString(R.string.path_image_storage));


            // 必要なすべての親ディレクトリを含めてディレクトリが生成された場合はtrue、
            // 生成されなかった場合は false ※ここ、「既に存在した場合は」falseかも。
//            Boolean res = null;

            if (!file.exists()) {
//                res = file.mkdirs();
                file.mkdirs();
            }
//
//            if (res == false) {
//                System.out.println("ディレクトリは作られていない");
//            }

            String AttachName = file.getAbsolutePath() + "/" + System.currentTimeMillis() + "." + context.getResources().getString(R.string.extension_save_image);
//            String AttachName = file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg";

            FileOutputStream out = new FileOutputStream(AttachName);


            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            mediaScan(context, new String[]{AttachName});

            if (context instanceof MainActivity) {
                Toast.makeText(context, "saved", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(context, "save failed...", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    private static void mediaScan(Context context, String[] paths) {
        String[] mimeTypes = {"image/jpeg"};
        MediaScannerConnection.scanFile(context, paths, mimeTypes, null);
    }


    private static boolean sdcardWriteReady() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }
}