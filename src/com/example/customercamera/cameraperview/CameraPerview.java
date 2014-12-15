package com.example.customercamera.cameraperview;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Face;
import android.hardware.Camera.FaceDetectionListener;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPerview extends SurfaceView implements
		SurfaceHolder.Callback {

	private Camera camera;
	private SurfaceHolder holder;

	public CameraPerview(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CameraPerview(Context context, Camera camera) {
		super(context);
		this.camera = camera;
		holder = getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		if (holder.getSurface() == null) {
			return;
		}
		try {
			camera.stopPreview();
			camera.setDisplayOrientation(90);
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			camera.setPreviewDisplay(holder);
			camera.startPreview();
			camera.autoFocus(new AutoFocusCallback() {

				@Override
				public void onAutoFocus(boolean success, Camera camera) {
					// TODO Auto-generated method stub

				}
			});

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressLint("NewApi") @Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		try {
			camera.setPreviewDisplay(holder);
			camera.startPreview();
//			camera.getParameters().setFlashMode(Parameters.FOCUS_MODE_INFINITY);
			camera.startFaceDetection();
			camera.setFaceDetectionListener(new FaceDetectionListener() {
				
				@Override
				public void onFaceDetection(Face[] faces, Camera camera) {
					// TODO Auto-generated method stub
					camera.autoFocus(new AutoFocusCallback() {
						
						@Override
						public void onAutoFocus(boolean success, Camera camera) {
							// TODO Auto-generated method stub
							
						}
					});
				}
			});
//			camera.autoFocus(new AutoFocusCallback() {
//
//				@Override
//				public void onAutoFocus(boolean success, Camera camera) {
//					// TODO Auto-generated method stub
//					camera.getParameters().setFlashMode(Parameters.FOCUS_MODE_INFINITY);
//				}
//			});

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

}
