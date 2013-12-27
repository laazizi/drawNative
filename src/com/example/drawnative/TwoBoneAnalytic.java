package com.example.drawnative;

public class TwoBoneAnalytic {

	public static Boolean CalcIK_2D_TwoBoneAnalytic
	(
	    double angle1,   // Angle of bone 1
	    double angle2,   // Angle of bone 2
	    Boolean solvePosAngle2, // Solve for positive angle 2 instead of negative angle 2
	    double length1,      // Length of bone 1. Assumed to be >= zero
	    double length2,      // Length of bone 2. Assumed to be >= zero
	    double targetX,      // Target x position for the bones to reach
	    double targetY       // Target y position for the bones to reach
	)
	{
	  //  Debug.Assert(length1 >= 0);
	   // Debug.Assert(length2 >= 0);
	 
	   double epsilon = 0.0001; // used to prevent division by small numbers
	 
	    Boolean foundValidSolution = true;
	 
	    double targetDistSqr = (targetX*targetX + targetY*targetY);
	 
	    //===
	    // Compute a new value for angle2 along with its cosine
	    double sinAngle2;
	    double cosAngle2;
	 
	    double cosAngle2_denom = 2*length1*length2;
	    if( cosAngle2_denom > epsilon )
	    {
	        cosAngle2 =   (targetDistSqr - length1*length1 - length2*length2)
	                    / (cosAngle2_denom);
	 
	        // if our result is not in the legal cosine range, we can not find a
	        // legal solution for the target
	        if( (cosAngle2 < -1.0) || (cosAngle2 > 1.0) )
	            foundValidSolution = false;
	 
	        // clamp our value into range so we can calculate the best
	        // solution when there are no valid ones
	        cosAngle2 = Math.max(-1, Math.min( 1, cosAngle2 ) );
	 
	        // compute a new value for angle2
	        angle2 = Math.acos( cosAngle2 );
	 
	        // adjust for the desired bend direction
	        if( !solvePosAngle2 )
	            angle2 = -angle2;
	 
	        // compute the sine of our angle
	        sinAngle2 = Math.sin( angle2 );
	    }
	    else
	    {
	        // At leaset one of the bones had a zero length. This means our
	        // solvable domain is a circle around the origin with a radius
	        // equal to the sum of our bone lengths.
	        double totalLenSqr = (length1 + length2) * (length1 + length2);
	        if(    targetDistSqr < (totalLenSqr-epsilon)
	            || targetDistSqr > (totalLenSqr+epsilon) )
	        {
	            foundValidSolution = false;
	        }
	 
	        // Only the value of angle1 matters at this point. We can just
	        // set angle2 to zero. 
	        angle2    = 0.0;
	        cosAngle2 = 1.0;
	        sinAngle2 = 0.0;
	    }
	 
	    //===
	    // Compute the value of angle1 based on the sine and cosine of angle2
	    double triAdjacent = length1 + length2*cosAngle2;
	    double triOpposite = length2*sinAngle2;
	 
	    double tanY = targetY*triAdjacent - targetX*triOpposite;
	    double tanX = targetX*triAdjacent + targetY*triOpposite;
	 
	    // Note that it is safe to call Atan2(0,0) which will happen if targetX and
	    // targetY are zero
	    angle1 = Math.atan2( tanY, tanX );
	 
	    return foundValidSolution;
	}
}