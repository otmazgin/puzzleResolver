package pieceRecognizer;

import java.util.ArrayList;
import java.util.List;

import entities.PuzzlePiece;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class PuzzlePieceDetector {

    public static List<PuzzlePiece> pieceDetector(Mat srcImage, int hueMin, int saturationMin, int valueMin,
	    int hueMax, int saturationMax, int valueMax, boolean auto) {

	List<PuzzlePiece> detectedPieces = new ArrayList<PuzzlePiece>();
	Mat srcGray = new Mat();

	Imgproc.cvtColor(srcImage, srcGray, Imgproc.COLOR_RGB2HSV);
	byte[] data = new byte[3];

	srcGray.get(0, 0, data);
	System.out.print(data[0] + " " + data[1] + " " + data[2]);

	if (!auto) {
	    Core.inRange(srcGray, new Scalar(hueMin, saturationMin, valueMin), new Scalar(hueMax,
		    saturationMax, valueMax), srcGray);
	} else {
	    int hue = (data[0] & 0xFF);
	    int saturation = (data[1] & 0xFF);
	    int value = (data[2] & 0xFF);
	    Core.inRange(srcGray, new Scalar(hue - 10, saturation - 20, 0), new Scalar(hue + 10,
		    saturation + 40, 255), srcGray);
	}
	Mat tmp = srcGray.clone();
	tmp.setTo(new Scalar(255, 255, 255));

	Core.subtract(tmp, srcGray, srcGray);

	Mat hierarchy = new Mat();
	List<MatOfPoint> contours = new ArrayList<>();

	Imgproc.findContours(srcGray, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE,
		new Point(0, 0));

	int maxArea = 1;
	for (MatOfPoint contour : contours) {
	    int area = (int) Imgproc.boundingRect(contour).area();
	    if (area > maxArea) {
		maxArea = area;
	    }
	}

	for (MatOfPoint contour : contours) {
	    Rect rect = Imgproc.boundingRect(contour);

	    if (rect.area() > (maxArea / 2)) {
		detectedPieces.add(new PuzzlePiece(srcImage, contour, hierarchy));

	    }

	}

	return detectedPieces;
    }
}
