package com.example.drawnative;

public class calculAngle {

 private myDataSingleton datacentre = myDataSingleton.getInstance();
 public   int bonne1 =datacentre.bonneSize;
 
 public int getBonne1() {
	return bonne1;
}
public void setBonne1(int bonne1) {
	this.bonne1 = bonne1;
}

public  int bonne2 = datacentre.bonneSize;
	private double t2;
	private double t1;
 
	
	
	
	 public calculAngle(double x1, double y1) {
		 
		 double x=x1*datacentre.k;
		double  y=y1*datacentre.k;
		 t2= Math.acos(((x*x+y*y)-bonne1*bonne1-bonne2*bonne2)/(2*bonne1*bonne2));
		 t1=Math.asin((bonne2*Math.sin(t2))/Math.sqrt((x*x+y*y)))+Math.atan2(y, x);
	}
	 public int getT2() {
			return (int)Math.toDegrees(t2);
		}

		public int getT1() {
			return (int) Math.toDegrees(t1);
		}


}
