package pieceRecognizer;

import java.util.LinkedList;
import java.util.List;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

import com.atul.JavaOpenCV.Imshow;

public class MainTestOld
{
    public static void main(String args[])
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat src = Highgui.imread("test6.jpg");
        Mat src_gray = new Mat();
        //Imgproc.cvtColor( src, src_gray,Imgproc.COLOR_RGB2GRAY  );
        //Imgproc.blur( src_gray, src_gray, new Size(3,3) );
        Imshow im = new Imshow("Title");
        Mat satImg = new Mat();
        Imgproc.cvtColor(src, src_gray, Imgproc.COLOR_RGB2HSV);
        // ArrayList<Mat> channels = new ArrayList<Mat>();
        //Core.split(src, channels);
        //src_gray = channels.get(2);
        //
        //Imgproc.medianBlur(satImg , satImg , 11);

        // Imgproc.adaptiveThreshold(satImg , satImg , 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 401, -10);
        //for test 4
        //Core.inRange(src_gray, new Scalar(35, 65, 0), new Scalar(50, 255, 255), src_gray);
        // for test 6
        Core.inRange(src_gray, new Scalar(70, 100, 150), new Scalar(80, 255, 255), src_gray);
        im.showImage(src_gray);
        Mat tmp = new Mat();
        src_gray.copyTo(tmp);
        tmp.setTo(new Scalar(255, 255, 255));

        Core.subtract(tmp, src_gray, src_gray);

        Mat hierarchy = new Mat();
        List<MatOfPoint> contours = new LinkedList<MatOfPoint>();


        Imgproc.findContours(src_gray, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));


        Mat drawing = new Mat(src_gray.size(), CvType.CV_8UC3);


        int max_area = 1;
        for (int i = 0; i < contours.size(); i++)
        {
            int area = (int) Imgproc.boundingRect(contours.get(i)).area();
            if (area > max_area)
                max_area = area;
        }

        for (int i = 0; i < contours.size(); i++)
        {
            Scalar color = new Scalar(255, 255, 255);

            Rect rect = Imgproc.boundingRect(contours.get(i));

            if (rect.area() > (max_area / 2))
            {
                Imgproc.drawContours(drawing, contours, i, color, -1, 8, hierarchy, 0, new Point());

                // draw enclosing rectangle (all same color, but you could use variable i to make them unique)
                // Core.rectangle(drawing, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height), new Scalar(255, 0, 0, 255), 1);
            }
            // im.showImage(drawing);
        }


        int blockSize = 30;
        int apertureSize = 5;
        double k = 0.05;


        Mat corners = new Mat(src_gray.size(), CvType.CV_32FC1);
        Imgproc.cvtColor(drawing, drawing, Imgproc.COLOR_RGB2GRAY);
        Imgproc.blur(drawing, drawing, new Size(4, 4));
        Imgproc.cornerHarris(drawing, corners, blockSize, apertureSize, k, Imgproc.BORDER_DEFAULT);
        Core.normalize(corners, corners, 0, 255, Core.NORM_MINMAX, CvType.CV_32FC1, new Mat());
        Core.convertScaleAbs(corners, corners);

        int thresh = 200;
        for (int j = 0; j < corners.rows(); j++)
        {
            for (int i = 0; i < corners.cols(); i++)
            {
                if ((int) corners.get(j, i)[0] > thresh)
                {
                    Core.circle(drawing, new Point(i, j), 5, new Scalar(Math.random() * 255), 1, 8, 0);
                }
            }
        }
        im.showImage(drawing);

        //   Scalar color =new  Scalar( Math.random()*255,Math.random()*255, Math.random()*255 );
        //  Imgproc.drawContours( drawing,  contours, 0, color, 2, 8, hierarchy, 0, new Point() );
  
    
 /*  
       
   
    Mat canny_output = new Mat() ;
    double thresh = 100;
    Imgproc.Canny( src_gray, canny_output, thresh, thresh*2 );


    Mat hierarchy = new Mat();
	List<MatOfPoint> contours = new LinkedList<MatOfPoint>();
	
	Imgproc.findContours( src_gray, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0) );
	  
/*	
	  Mat drawing = new Mat( canny_output.size(), CvType.CV_8UC3 );
 
	 
	  for( int i = 0; i< contours.size(); i++ )
	     {
	       Scalar color =new  Scalar( Math.random()*255,Math.random()*255, Math.random()*255 );
	       Imgproc.drawContours( drawing, contours, i, color, 2, 8, hierarchy, 0, new Point() );
	     }
	*/
//	  System.out.print(contours.get(0));
        //  Mat fill_drawing = new Mat( canny_output.size(), CvType.CV_8UC3 );
        // Imgproc.drawContours(fill_drawing, contours, -1, new Scalar(255,255,0),1);


    }

} 