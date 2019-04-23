package com.angle.netupdatademo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.angle.netupdatademo.utils.UriparseUtils;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    // Used to load the 'native-lib' library on application startup.
    static {
        //在应用程序启动时加载本地的lib
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = findViewById(R.id.versionTv);
        tv.setText(BuildConfig.VERSION_NAME);

        //申请权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(perms, 200);
            }
        }
    }

    /**
     * 合成安装包
     *
     * @param oldApk 旧版本安装包 如1.1.0安装包
     * @param patch  查分包 patch文件
     * @param output 合成后的新版本apk安装包
     */
    public native void bsPath(String oldApk, String patch, String output);

    public void update(View view) {

        //从服务器下载patch到用户手机 sdcard
        new AsyncTask<Void, Void, File>() {
            @Override
            protected File doInBackground(Void... voids) {

                //获取现在运行的apk路径
                String oldApk = getApplicationInfo().sourceDir;
                Log.e(TAG, "第一步成功==>");

                // 获取拆分包的路径
                String patch = new File(Environment.getExternalStorageDirectory(), "patch").getAbsolutePath();
                Log.e(TAG, "第二步成功==>");

                // 获取合成之后的新apk的路径
                String output = createNewApk().getAbsolutePath();
                Log.e(TAG, "第三步成功==>");

                bsPath(oldApk, patch, output);
                Log.e(TAG, "第四步成功==>");

                return new File(output);
            }

            @Override
            protected void onPostExecute(File file) {
                super.onPostExecute(file);
                //已经合成了，调用该方法
                UriparseUtils.installApk(MainActivity.this, file);
            }
        }.execute();
    }

    private File createNewApk() {
        File newApk = new File(Environment.getExternalStorageDirectory(), "newApk.apk");
        if (!newApk.exists()) {
            try {
                newApk.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return newApk;
    }
}
