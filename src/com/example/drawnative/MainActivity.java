package com.example.drawnative;

 
 
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
 

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.Utils;
 
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import com.android.future.usb.UsbAccessory;
import com.android.future.usb.UsbManager;

import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
 
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
 
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
 
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements CvCameraViewListener2,
		android.widget.RadioGroup.OnCheckedChangeListener {
	private CameraBridgeViewBase mOpenCvCameraView;
	protected static final String TAG = "laazizi";
	private int mViewMode;
	private static final int VIEW_MODE_CANNY = 2;
	private Mat mRgba;
	private Mat mIntermediateMat;
	private Mat mMat;
	private double vala = 40;
	private double valb = 40;
	private calculAngle cal;
	private ArrayList<MatOfPoint> contours;

	private Mat mGray;
	private boolean clean = false;
	private CheckBox checkbox;
	private RadioButton sp;
	private int RETR = Imgproc.RETR_LIST;
	private int APPROX = Imgproc.CHAIN_APPROX_NONE;
	private boolean startWrite = false;
	// **********************************************
	private ImageView imview;
	private Mat hierarchy;
	private Scalar color;
	// ********************************

	private static final String ACTION_USB_PERMISSION = "com.google.android.DemoKit.action.USB_PERMISSION";

	private UsbManager mUsbManager;
	private PendingIntent mPermissionIntent;
	private boolean mPermissionRequestPending;
	private SeekBar ledSeekBar;

	UsbAccessory mAccessory;
	ParcelFileDescriptor mFileDescriptor;
	FileInputStream mInputStream;
	FileOutputStream mOutputStream;

	public static final byte LED_SERVO_COMMAND = 4;
	public static final byte SERVO_COMMAND_1 = 1;
	public static final byte SERVO_COMMAND_2 = 2;
	public static final byte SERVO_COMMAND_PEN_3 = 3;
	protected static final int REQUEST_CODE = 0;
	private int index;
	private int total;
	private int up = 70;
	private int down = 0;
	private int MAXGROUP = 50;
	private double myx;
	private double myy;
	private double maxxy;
	private double maxdiag;
	private myDataSingleton datacentre = myDataSingleton.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial2_activity_surface_view);
		mOpenCvCameraView.setCvCameraViewListener(this);
		mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
	
		datacentre.cameracentre=mOpenCvCameraView;
		info = (TextView) findViewById(R.id.info);

		RadioGroup rad1 = (RadioGroup) findViewById(R.id.radioGroup1);
		RadioGroup rad2 = (RadioGroup) findViewById(R.id.radioGroup2);
		rad1.setOnCheckedChangeListener(this);
		rad2.setOnCheckedChangeListener(this);
		Button bput = (Button) findViewById(R.id.button2);
		Toast.makeText(getApplicationContext(), "je suis dans create", Toast.LENGTH_SHORT).show();
		bput.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				datacentre.cameracentre.clearFocus();
			}

		});
		Button b = (Button) findViewById(R.id.button1);
		checkbox = (CheckBox) findViewById(R.id.checkBox1);
		SeekBar s1 = (SeekBar) findViewById(R.id.seekbonnezoom);
		SeekBar s2 = (SeekBar) findViewById(R.id.seekbonne2);
		s1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				vala = progress;

			}
		});
		s2.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				valb = progress;

			}
		});

		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (datacentre.cameracentre != null)
					datacentre.cameracentre.disableView();

				imview = (ImageView) findViewById(R.id.imagefinal);
				Bitmap bmpS = Bitmap.createBitmap(mIntermediateMat.width(),
						mIntermediateMat.height(), Bitmap.Config.ARGB_8888);
				contours = new ArrayList<MatOfPoint>();
				Imgproc.findContours(mIntermediateMat, contours, hierarchy,
						RETR, APPROX, new Point(0, 0));
				datacentre.contours = contours;
				datacentre.matFinal = mIntermediateMat;
		
				 
				Utils.matToBitmap(mIntermediateMat, bmpS);
				datacentre.bmp = bmpS;
				imview.setImageBitmap(bmpS);
				imview.invalidate();
				Intent intent = new Intent(getApplicationContext(),
						ViewActivity.class);

				startActivity (intent );
				finish();
				if (datacentre.cameracentre != null){
					datacentre.cameracentre.disableView();
					
					 
				}
 
			}
		});
	}

	// ******************************end oncreate

	public void sendCommand(byte command, byte target, int value) {
		byte[] buffer = new byte[3];
		if (value > 180)
			value = 180;

		buffer[0] = command;
		buffer[1] = target;
		buffer[2] = (byte) value;
		if (mOutputStream != null && buffer[1] != -1) {
			try {
				mOutputStream.write(buffer);
			} catch (IOException e) {
				Log.e(TAG, "write failed", e);
			}
		}
	}

	public void run() {
		int ret = 0;
		byte[] buffer = new byte[16384];
		int i;

		while (ret >= 0) {
			try {
				ret = mInputStream.read(buffer);
			} catch (IOException e) {
				break;
			}

			i = 0;
			while (i < ret) {
				int len = ret - i;

				switch (buffer[i]) {
				default:
					Log.d(TAG, "unknown msg: " + buffer[i]);
					i = len;
					break;
				}
			}

		}
	}

	// *********************************************
	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS: {
				Log.i(TAG, "OpenCV loaded successfully");
				
			//ystem.loadLibrary("drawNative");

				datacentre.cameracentre.enableView();
				Toast.makeText(getApplicationContext(), "on callback ="+datacentre.cameracentre.getWidth(),Toast.LENGTH_SHORT).show();
			}
				break;
			default: {
				super.onManagerConnected(status);
				Toast.makeText(getApplicationContext(), "on from status ="+status, Toast.LENGTH_SHORT).show();
			}
				break;
			}
		}
	};
	private TextView info;

	@Override
	public void onPause() {
		super.onPause();
		if (datacentre.cameracentre != null){
			datacentre.cameracentre.disableView();
			
			 
		}
		Toast.makeText(this, "on pause etat ="+datacentre.cameracentre.isActivated(), Toast.LENGTH_SHORT).show();
	 
		
	}

	@Override
	public void onResume() {
		super.onResume();

		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this,
				mLoaderCallback);
		 
		Toast.makeText(this, "on resume ="+datacentre.cameracentre.isActivated(), Toast.LENGTH_SHORT).show();
		
	}

	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "on destroy", Toast.LENGTH_SHORT).show();
		if (datacentre.cameracentre != null)
			datacentre.cameracentre.disableView();

	}

	private void enableControls(boolean b) {
		// TODO write code to disable the GUI

	}

	// **********************************oncreate
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Toast.makeText(this, "on result", Toast.LENGTH_SHORT).show();
			   
		datacentre.cameracentre.setCvCameraViewListener(this);
	}

	private void enableUi(final boolean enable) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// seekBar1.setEnabled(enable);
				// seekBar2.setEnabled(enable);
				checkbox.setEnabled(enable);
			}
		});
	}

	public native int[] FindFeatures(long matAddrGr, long matAddrRgba);

	@Override
	public void onCameraViewStarted(int width, int height) {
		mRgba = new Mat(height, width, CvType.CV_8UC4);
		mIntermediateMat = new Mat(height, width, CvType.CV_8UC4);
		mGray = new Mat(height, width, CvType.CV_8UC1);
		Toast.makeText(this, "on camera start", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onCameraViewStopped() {
		// TODO Auto-generated method stub

	}

	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		mRgba = inputFrame.gray();
		
		hierarchy = new Mat();

		Imgproc.GaussianBlur(mRgba, mGray, new Size(5, 5), 2, 2);
 
		Imgproc.Canny(mGray, mIntermediateMat, vala, valb);
		 
		 return mIntermediateMat;
		 
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		if (arg0.getId() == R.id.radioGroup1) {

			switch (arg1) {
			case R.id.radio10:
				RETR = Imgproc.RETR_LIST;
			case R.id.radio11:
				RETR = Imgproc.RETR_CCOMP;
			case R.id.radio12:
				RETR = Imgproc.RETR_TREE;

			}
		}
		if (arg0.getId() == R.id.radioGroup2) {
			switch (arg1) {
			case R.id.radio20:
				APPROX = Imgproc.CHAIN_APPROX_NONE;
			case R.id.radio21:
				APPROX = Imgproc.CHAIN_APPROX_SIMPLE;
			case R.id.radio22:
				APPROX = Imgproc.CHAIN_APPROX_TC89_L1;
			case R.id.radio23:
				APPROX = Imgproc.KERNEL_SMOOTH;

			}
		}

	}
}
