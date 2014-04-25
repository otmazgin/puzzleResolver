package piceRecognizer;

import java.util.Collections;
import java.util.LinkedList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class PuzzelPiceCornerDetector {
	
	
	
	public static void corner_finder(int blockSize , 
			int apertureSize,
			double k , 
			int threashHold, 
			Mat mask,
			Mat corner_mat,
			LinkedList <Corner> cornersList){

		Mat temp_mat = new Mat();
		corner_mat = new Mat( mask.size(), CvType.CV_32FC1  );
	    Imgproc.cvtColor( mask, temp_mat,Imgproc.COLOR_RGB2GRAY  );
	    Imgproc.blur( temp_mat, temp_mat, new Size(4,4) );
	    Imgproc.cornerHarris( temp_mat, corner_mat, blockSize, apertureSize, k, Imgproc.BORDER_DEFAULT );
	    Core.normalize( corner_mat, corner_mat, 0, 255, Core.NORM_MINMAX, CvType.CV_32FC1,new Mat() );
	    Core.convertScaleAbs( corner_mat, corner_mat );
	    for( int j = 0; j < corner_mat.rows() ; j++ )
	    { for( int i = 0; i < corner_mat.cols(); i++ )
	         {
	           if( (int) corner_mat.get(j,i)[0] > threashHold )
	             {
	        	   cornersList.add(new Corner (i,j,corner_mat.get(j,i)[0]) );
	             }
	         }
	    }
	    Collections.sort(cornersList);
 
	}

}
