package pieceRecognizer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class PuzzlePieceDetector {

    public static List<PuzzlePiece> pieceDetector(Mat srcImage, int hueMin, int saturationMin, int valueMin,
	    int hueMax, int saturationMax, int valueMax) {

	List<PuzzlePiece> detectedPieces = new ArrayList<PuzzlePiece>();
	Mat srcGray = new Mat();

	Imgproc.cvtColor(srcImage, srcGray, Imgproc.COLOR_RGB2HSV);
	Core.inRange(srcGray, new Scalar(hueMin, saturationMin, valueMin), new Scalar(hueMax, saturationMax,
		valueMax), srcGray);
	Mat tmp = new Mat();
	srcGray.copyTo(tmp);
	tmp.setTo(new Scalar(255, 255, 255));

	Core.subtract(tmp, srcGray, srcGray);

	Mat hierarchy = new Mat();
	List<MatOfPoint> contours = new LinkedList<>();

	Imgproc.findContours(srcGray, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE,
		new Point(0, 0));

	int max_area = 1;
	for (MatOfPoint contour : contours) {
	    int area = (int) Imgproc.boundingRect(contour).area();
	    if (area > max_area) {
		max_area = area;
	    }
	}

	for (int i = 0; i < contours.size(); i++) {
	    Rect rect = Imgproc.boundingRect(contours.get(i));

	    if (rect.area() > (max_area / 2)) {
		detectedPieces.add(new PuzzlePiece(srcImage, contours, hierarchy, i));

	    }
	}

	return detectedPieces;
    }
}
