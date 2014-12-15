package com.example.customercamera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.customercamera.cameraperview.CameraPerview;

public class MainActivity extends Activity implements OnClickListener {
	private static final int MEDIA_TYPE_IMAGE = 0;
	private static final int MEDIA_TYPE_VIDEO = 1;
	private Camera camera;
	private CameraPerview cameraPerview;
	private Button take_picture;
	private Button reTake;
	FrameLayout frameLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		frameLayout = (FrameLayout) findViewById(R.id.camera_perview);
		camera = getCameraInstance();
		cameraPerview = new CameraPerview(this, camera);

		frameLayout.addView(cameraPerview);
		take_picture = (Button) findViewById(R.id.take_picture);
		reTake = (Button) findViewById(R.id.reTake);
		take_picture.setOnClickListener(this);
		reTake.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		camera.release();
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type) {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"MyCameraApp");
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "VID_" + timeStamp + ".mp4");
		} else {
			return null;
		}

		return mediaFile;
	}

	public boolean isHasCamera() {
		if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			return true;
		}

		return false;
	}

	public Camera getCameraInstance() {

		Camera camera = null;
		if (isHasCamera() && Camera.getNumberOfCameras() > 0) {

			camera = Camera.open(0);
		}

		return camera;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.take_picture:
			camera.takePicture(null, null, new PictureCallback() {

				@Override
				public void onPictureTaken(byte[] data, Camera camera) {
					// TODO Auto-generated method stub
					File file = getOutputMediaFile(MEDIA_TYPE_IMAGE);
					if (file == null) {
						return;
					} else {
						FileOutputStream out;
						try {
							out = new FileOutputStream(file);
							out.write(data);
							out.close();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					camera.release();
				}
			});
			break;
		case R.id.reTake:
			if (camera == null) {
				camera = getCameraInstance();
			}
			if (frameLayout.getChildCount() != 0) {
				frameLayout.removeAllViews();
			}
			if (cameraPerview == null) {
				cameraPerview = new CameraPerview(this, camera);
			}
			frameLayout.addView(cameraPerview);
			break;

		default:
			break;
		}
	}
}
