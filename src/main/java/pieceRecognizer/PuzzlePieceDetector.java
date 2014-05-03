package pieceRecognizer;

import java.util.LinkedList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class PuzzlePieceDetector
{
    public static void pieceDetector
            (
                    Mat src,
                    int h_min,
                    int s_min,
                    int v_min,
                    int h_max,
                    int s_max,
                    int v_max,
                    LinkedList<PuzzlePiece> Pieces
            )
    {
        Mat src_gray = new Mat();

        Imgproc.cvtColor(src, src_gray, Imgproc.COLOR_RGB2HSV);
        Core.inRange(src_gray, new Scalar(h_min, s_min, v_min), new Scalar(h_max, s_max, v_max), src_gray);
        Mat tmp = new Mat();
        src_gray.copyTo(tmp);
        tmp.setTo(new Scalar(255, 255, 255));

        Core.subtract(tmp, src_gray, src_gray);

        Mat hierarchy = new Mat();
        List<MatOfPoint> contours = new LinkedList<>();

        Imgproc.findContours(src_gray, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));

        int max_area = 1;
        for (MatOfPoint contour : contours)
        {
            int area = (int) Imgproc.boundingRect(contour).area();
            if (area > max_area)
                max_area = area;
        }

        for (int i = 0; i < contours.size(); i++)
        {
            Rect rect = Imgproc.boundingRect(contours.get(i));

            if (rect.area() > (max_area / 2))
            {
                Pieces.add(new PuzzlePiece(src, contours, hierarchy, i));

            }
        }
    }
}
