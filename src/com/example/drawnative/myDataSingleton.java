package com.example.drawnative;

import java.util.ArrayList;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;

import android.graphics.Bitmap;

public class myDataSingleton {
	private myDataSingleton()
	{
		
	}
 
	private static myDataSingleton INSTANCE = new myDataSingleton();
 
	 
	public static myDataSingleton getInstance()
	{	return INSTANCE;
	}
	public Mat matFinal;
	public Bitmap bmp;
	public ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	public CameraBridgeViewBase cameracentre;
	public int bonneSize=400;
	public int bonne1=400;
	public int bonne2=400;
	public double k=1;
}
