package com.example.drawnative;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.opencv.android.Utils;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import com.android.future.usb.UsbAccessory;
import com.android.future.usb.UsbManager;
import com.example.drawnative.R;
import com.example.drawnative.R.id;
import com.example.drawnative.R.layout;
 

import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ViewActivity extends Activity implements Runnable {
	private static final String ACTION_USB_PERMISSION = "com.google.android.DemoKit.action.USB_PERMISSION";
	private UsbAccessory mAccessory;
	private ParcelFileDescriptor mFileDescriptor;
	private FileInputStream mInputStream;
	private FileOutputStream mOutputStream;
	private UsbManager mUsbManager;
	private PendingIntent mPermissionIntent;
	private boolean mPermissionRequestPending;

	public static final byte LED_SERVO_COMMAND = 4;
	public static final byte SERVO_COMMAND_1 = 1;

	public static final byte SERVO_COMMAND_PEN_3 = 2;
	protected static final String TAG = "info mo";
	protected static final int max = 800;
	protected static final int maxtest = 400;
	private int index;
	private int total;
	private int up = 40;
	private int down = 70;
	private int MAXGROUP = 160;
	private int MAXGROUP1 = 80;
	private myDataSingleton datacentre = myDataSingleton.getInstance();
	private ArrayList<MatOfPoint> contours;
	private TextView inflog;
	private ProgressBar progressdraw;
	private CustomDrawableImageView im;
	private ImageView etatusb;
	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ACTION_USB_PERMISSION.equals(action)) {
				synchronized (this) {
					UsbAccessory accessory = UsbManager.getAccessory(intent);
					if (intent.getBooleanExtra(
							UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
						openAccessory(accessory);
					} else {
						Log.d(TAG, "permission denied for accessory "
								+ accessory);

						CharSequence text = "pas accessory erreur ";
						int duration = Toast.LENGTH_SHORT;

						Toast toast = Toast.makeText(context, text, duration);
						toast.show();
					}
					mPermissionRequestPending = false;
				}
			} else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
				UsbAccessory accessory = UsbManager.getAccessory(intent);
				if (accessory != null && accessory.equals(mAccessory)) {
					CharSequence text = "detached accessory ";
					int duration = Toast.LENGTH_SHORT;

					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
					closeAccessory();
				}
			} else if (UsbManager.ACTION_USB_ACCESSORY_ATTACHED.equals(action)) {
				UsbAccessory accessory = UsbManager.getAccessory(intent);
				openAccessory(accessory);
			}
		}
	};
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view);
		etatusb = (ImageView) findViewById(R.id.etatconnect);
		progressdraw = (ProgressBar) findViewById(R.id.progressdraw);
		im = (CustomDrawableImageView) findViewById(R.id.imagefinal);
		im.bitmap = datacentre.bmp;
		im.bitmappen = datacentre.bmp;
		// im.setImageBitmap(datacentre.bmp);
		im.invalidate();
		inflog = (TextView) findViewById(R.id.infolog);

		mUsbManager = UsbManager.getInstance(this);
		mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(
				ACTION_USB_PERMISSION), 0);
		IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
		filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
		registerReceiver(mUsbReceiver, filter);
		if (getLastNonConfigurationInstance() != null) {
			mAccessory = (UsbAccessory) getLastNonConfigurationInstance();
			openAccessory(mAccessory);
		}
		Button end = (Button) findViewById(R.id.end);
		end.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						MainActivity.class);

				startActivity(intent);
				finish();

			}
		});
		
		SeekBar zoom = (SeekBar) findViewById(R.id.seekdrawzoom);
		zoom.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

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
				datacentre. k= 1.2;

			}
		});
		Button gotoxy = (Button) findViewById(R.id.go);
		
		gotoxy.setOnClickListener(new OnClickListener() {

			private int tox;
			private int toy;

			@Override
			public void onClick(View v) {

				try {
					/*
					 * sendCommand(SERVO_COMMAND_PEN_3, SERVO_COMMAND_PEN_3,
					 * down); Thread.sleep(100);
					 * 
					 * 
					 * runOnUiThread(new Runnable() {
					 * 
					 * @Override public void run() { EditText t1 = (EditText)
					 * findViewById(R.id.editx); EditText t2 = (EditText)
					 * findViewById(R.id.edity); double myx =
					 * Double.parseDouble(t1.getText().toString()); double myy =
					 * Double.parseDouble(t2.getText().toString()); calculAngle
					 * cal; cal = new calculAngle(myx, myy); Toast toast =
					 * Toast.makeText(getApplicationContext(),
					 * cal.getT1()+":"+cal.getT2(), Toast.LENGTH_SHORT);
					 * toast.show(); sendCommand(SERVO_COMMAND_1,
					 * SERVO_COMMAND_2, cal.getT1());
					 * sendCommand(SERVO_COMMAND_2, SERVO_COMMAND_1,
					 * cal.getT2());
					 * 
					 * } });
					 */
					sendCommanddoit(SERVO_COMMAND_PEN_3,
							down);
					Thread.sleep(250);
					for (int i = 5; i < maxtest; i++) {
						calculAngle cal = new calculAngle(i, i);
						sendCommand(SERVO_COMMAND_1, cal.getT1(), cal.getT2());

						tox = cal.getT1();
						toy = cal.getT2();

						Thread.sleep(3);

					}
					for (int i = maxtest; i >5; i--) {
						calculAngle cal = new calculAngle(i, maxtest);
						sendCommand(SERVO_COMMAND_1, cal.getT1(), cal.getT2());

						tox = cal.getT1();
						toy = cal.getT2();

						Thread.sleep(3);

					}
					for (int i = maxtest; i >5; i--) {
						calculAngle cal = new calculAngle(5, i);
						sendCommand(SERVO_COMMAND_1, cal.getT1(), cal.getT2());

						tox = cal.getT1();
						toy = cal.getT2();

						Thread.sleep(3);

					}
					sendCommanddoit(SERVO_COMMAND_PEN_3, up);
					 
					// sendCommand(SERVO_COMMAND_PEN_3, SERVO_COMMAND_PEN_3,
					// up);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		Button bstart = (Button) findViewById(R.id.Start);
		bstart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ArrayList<MatOfPoint> contourstmp =new ArrayList<MatOfPoint>();
	 
			ArrayList<Integer> keep = new ArrayList<Integer>();
		 
				for (int i = 0; i < datacentre.contours.size(); i++) {
					//test max size line
					
					//contourstmp.add(datacentre.contours.get(i));
					if (datacentre.contours.get(i).toList().size() > MAXGROUP) {
						contourstmp.add(datacentre.contours.get(i));				 
					}
					else {
						keep.add(i)	;
					}
					 
				}
				for (int j = 0; j < keep.size(); j++) {
					if(datacentre.contours.get(keep.get(j)) != null)
					contourstmp.add(datacentre.contours.get(keep.get(j)));
				} 
				 
				drawarm(contourstmp);
				
			}
		});
		 
	}
private void drawarm(ArrayList<MatOfPoint> contoursarray){
	contours=contoursarray;

	new Thread(new Runnable() {

		
		private double myx;
		private double myy;
		private double maxxy;
		private calculAngle cal;

		@Override
		public void run() {

			Math.sqrt(datacentre.bonne1
					* datacentre.bonne1 * 2);
			try {
				
		
				for (int i = 0; i < contours.size(); i++) {
					//test max size line
				//	if (contours.get(i).toList().size() > MAXGROUP) {
						//test limit of bones
						if (contours.get(i).toList()
								.get(0).x < max
								&& contours.get(i)
										.toList().get(0).y < max) {

							cal = new calculAngle(
									contours.get(i)
											.toList().get(0).x,
											contours.get(i)
											.toList().get(0).y);
							sendCommanddoit(SERVO_COMMAND_PEN_3, up);
							Thread.sleep(250);

							sendCommand(SERVO_COMMAND_1,
									cal.getT1(), cal.getT2());

							Thread.sleep(700);
							sendCommanddoit(SERVO_COMMAND_PEN_3,
									down);
							Thread.sleep(250);
						}
						total = contours.size();
						progressdraw.setMax(total);
						index = i;
						for (int j = 1; j < contours
								.get(i).toList().size()/2; j++) {
							maxxy = Math.sqrt(contours
									.get(i).toList().get(j).x
									* contours.get(i)
											.toList().get(j).x
									+ contours.get(i)
											.toList().get(j).y
									* contours.get(i)
											.toList().get(j).y);
							if (maxxy < Math.sqrt(max * max * 2)) {
								cal = new calculAngle(
										contours.get(i)
												.toList().get(j).x,
												contours.get(i)
												.toList().get(j).y);
 
								sendCommand(SERVO_COMMAND_1,
										cal.getT1(), cal.getT2());

								 
								myx = contours.get(i)
										.toList().get(j).x;
								myy = contours.get(i)
										.toList().get(j).y;
								 drawpoint();

							}
							
							//Thread.sleep(1);
						}
						sendCommanddoit(SERVO_COMMAND_PEN_3, up);
						Thread.sleep(450);
					//}

				}

				cal = new calculAngle(200, 200);
				sendCommanddoit(SERVO_COMMAND_PEN_3, up);
				Thread.sleep(100);
				sendCommand(SERVO_COMMAND_1, cal.getT1(),
						cal.getT2());

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		private void drawpoint() {
			runOnUiThread(new Runnable() {
				public void run() {
					inflog.setText("(x: "
							+ myx
							+ "y :"
							+ myy
							+ "size"
							+ contours
									.get(index)
									.toList()
									.size()
							+ " =>index="
							+ index
							+ " : Total="
							+ total);
					progressdraw
							.setProgress(index);
					im.setX((float) myx);
					im.setY((float) myy);
					im.setAngles(cal);
					 
					im.invalidate();

				}
			});
			
		}

		
	}).start();


}
	private void openAccessory(UsbAccessory accessory) {
		mFileDescriptor = mUsbManager.openAccessory(accessory);
		if (mFileDescriptor != null) {
			mAccessory = accessory;
			FileDescriptor fd = mFileDescriptor.getFileDescriptor();
			mInputStream = new FileInputStream(fd);
			mOutputStream = new FileOutputStream(fd);
			Thread thread = new Thread(null, this, "DemoKit");
			thread.start();
			Log.d(TAG, "accessory opened");
			CharSequence text = "accessory open ";
			int duration = Toast.LENGTH_SHORT;
			etatusb(true);
			Toast toast = Toast.makeText(getApplicationContext(), text,
					duration);
			toast.show();
			enableControls(true);
		} else {
			Log.d(TAG, "accessory open fail");
			CharSequence text = " open accessory erreur ";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(getApplicationContext(), text,
					duration);
			toast.show();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mUsbReceiver);
		try {
			if (mFileDescriptor != null) {
				mFileDescriptor.close();
			}
		} catch (IOException e) {
		} finally {
			mFileDescriptor = null;
			mAccessory = null;
		}
		CharSequence text = "destroy ";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(getApplicationContext(), text, duration);
		toast.show();
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		closeAccessory();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (mInputStream != null && mOutputStream != null) {
			return;

		}

		UsbAccessory[] accessories = mUsbManager.getAccessoryList();
		UsbAccessory accessory = (accessories == null ? null : accessories[0]);
		if (accessory != null) {
			if (mUsbManager.hasPermission(accessory)) {
				openAccessory(accessory);
			} else {
				synchronized (mUsbReceiver) {
					if (!mPermissionRequestPending) {
						mUsbManager.requestPermission(accessory,
								mPermissionIntent);
						mPermissionRequestPending = true;
					}
				}
			}
		} else {
			Log.d(TAG, "mAccessory is null");
		}

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	private void closeAccessory() {
		enableControls(false);
		etatusb(false);
		try {
			if (mFileDescriptor != null) {
				mFileDescriptor.close();
			}
		} catch (IOException e) {
		} finally {
			mFileDescriptor = null;
			mAccessory = null;
		}
	}
	private void sendCommanddoit(byte target, int value) {
		byte[] buffer = new byte[3];
		if (value > 180)
			value = 180;

		buffer[0] = target;
		buffer[1] = (byte) value;

		if (mOutputStream != null && buffer[1] != -1) {
			try {
				mOutputStream.write(buffer);
			} catch (IOException e) {
				Log.e(TAG, "write failed", e);
				inflog.setText("erreur write ");
			}
		}

	}
	public void sendCommand(byte target, int value, int value2) {
		byte[] buffer = new byte[3];
		if (value > 180)
			value = 180;
		buffer[0] = target;
		buffer[1] = (byte) value;
		buffer[2] = (byte) value2;
		if (mOutputStream != null && buffer[1] != -1) {
			try {
				mOutputStream.write(buffer);
			} catch (IOException e) {
				Log.e(TAG, "write failed", e);
				inflog.setText("erreur write ");
			}
		}
	}

	public void run() {
		if (mInputStream != null) {
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
	}

	private void enableControls(boolean b) {
		// TODO write code to disable the GUI

	}

	private void etatusb(final boolean enable) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (enable) {
					etatusb.setImageResource(R.drawable.connect);
				} else {
					etatusb.setImageResource(R.drawable.disconnect);
				}
			}
		});
	}
}
