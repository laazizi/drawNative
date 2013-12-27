package com.example.drawnative;

import java.io.InputStream;

 

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CustomDrawableImageView extends ImageView {
	private ShapeDrawable mDrawable;
	public static Bitmap bitmap;
	private myDataSingleton datacentre = myDataSingleton.getInstance();
	private Paint p;
	private float x;
	private float y;
	private   calculAngle angle= new calculAngle(400, 400);

	public float getX() {
		return x;
	}

	public void setAngles(calculAngle ang) {
		 angle=ang;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	private Paint pb;
	private Paint ppoint;
	public Bitmap bitmappen;

	public CustomDrawableImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		p = new Paint();
		p.setColor(Color.RED);
		p.setStrokeWidth(5);
		pb = new Paint();
		pb.setStrokeWidth(5);
		pb.setColor(Color.BLUE);
		ppoint = new Paint();
		ppoint.setColor(Color.YELLOW);
		//Resources d = getResources();
		//bitmappen = BitmapFactory.decodeResource(getResources(), R.drawable.pencil);

	}

	public CustomDrawableImageView(Context context) {
		super(context);
		p = new Paint();
		p.setColor(Color.RED);

	}

	protected void onDraw(Canvas canvas) {
		// mDrawable.draw(canvas);

		if (bitmap != null) {
			canvas.drawBitmap(bitmap, 0, 0, null);
/*			float x1=Math.abs((float) (200*Math.cos(angle.getT1())));
			float y1=Math.abs((float) (bitmap.getHeight()-  200*Math.sin(angle.getT1())));
			float x2=x1+Math.abs((float) (200*Math.cos(angle.getT2())));
			float y2=y1+Math.abs((float)-Math.abs(200*Math.sin(angle.getT2())));
			
		 	canvas.drawLine(0, bitmap.getHeight(),x1, y1, pb);
		 	canvas.drawLine(x1, y1,x,y, p);
			*/
			//canvas.drawLine((float) (datacentre.bonne1*Math.cos(angle.getT1())), (float) (bitmap.getHeight()-Math.abs(datacentre.bonne1*Math.sin(angle.getT1()))), x, y, pb);
			//canvas.drawLine(0, bitmap.getHeight(),(float) (datacentre.bonne1*Math.cos(angle.getT1())), (float) (bitmap.getHeight()- Math.abs(datacentre.bonne1*Math.sin(angle.getT1()))), pb);
			//canvas.drawLine((float) (datacentre.bonne1*Math.cos(angle.getT1())), (float) (bitmap.getHeight()-Math.abs(datacentre.bonne1*Math.sin(angle.getT1()))), x, y, pb);
			canvas.drawCircle(x, y, 10, ppoint);
			//canvas.drawBitmap(bitmappen, x, y, null);
		}

	}

}