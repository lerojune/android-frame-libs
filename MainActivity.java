package com.lj.shcode.snake.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Picture;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.lj.shcode.snake.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @Bind(R.id.surface)
    SurfaceView surfaceView;
    @Bind(R.id.preview)
    Preview preview;

    SurfaceHolder holder;
    Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public int px2dp(float px){
        return (int) (px/getBaseContext().getResources().getDisplayMetrics().density+0.5f);
    }

    public void take(View view){
        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() +"img.jpg");
                try {
                    FileOutputStream outputStream = new FileOutputStream(file);
                    outputStream.write(data);
                    outputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                float width = bitmap.getWidth()*0.8f;
                float height = width*0.75f;
                int posx = (int) ((bitmap.getWidth()-width)/2.0);
                int posy = (int)((bitmap.getHeight() - height)/2.0);
                Rect r = new Rect(posx, posy, (int)(posx+width), (int)(posy+height));
                Bitmap  nb  = Bitmap.createBitmap(bitmap, r.left,r.top, r.width(),r.height());
                File file1 = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() +"img1.jpg");
                try {
                    FileOutputStream outputStream = new FileOutputStream(file1);
                    nb.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                    outputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void init(){
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        ViewGroup.LayoutParams params = surfaceView.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = (int) (metrics.widthPixels/3.0*4);
        surfaceView.setLayoutParams(params);
        preview.setLayoutParams(params);

        holder = surfaceView.getHolder();
        holder.addCallback(surfaceCallback);
    }

    SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            camera = Camera.open();
            Camera.Parameters parameters = camera.getParameters();
            List<Camera.Size> list = parameters.getSupportedPreviewSizes();
            for (Camera.Size size:list){
                Log.e("size", size.width+":"+size.height+"["+size.width*1.0/size.height+"]");
            }
            parameters.setPreviewSize(640, 480);
            List<Camera.Size> plist = parameters.getSupportedPictureSizes();
            for (Camera.Size size:plist){
                Log.e("size12", size.width+":"+size.height+"["+size.width*1.0/size.height+"]");
            }
            parameters.setPictureSize(640,480);
            parameters.setPictureFormat(ImageFormat.JPEG);
            parameters.setRotation(90);
            camera.setParameters(parameters);
            camera.setDisplayOrientation(90);
            try {
                camera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            camera.startPreview();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            camera.stopPreview();
            camera.release();
        }
    };

}
