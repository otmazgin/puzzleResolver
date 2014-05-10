package pieceRecognizer;

import java.util.Collections;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import com.atul.JavaOpenCV.Imshow;

public class PuzzlePieceCornerDetector {

    public static void cornerFinder(int blockSize, int apertureSize, double k, int threashHold, Mat mask,
	    Mat cornerMat, List<Corner> cornersList) {
	Mat temp_mat = new Mat();
	cornerMat = new Mat(mask.size(), CvType.CV_32FC1);
	Imgproc.cvtColor(mask, temp_mat, Imgproc.COLOR_RGB2GRAY);
	Imgproc.blur(temp_mat, temp_mat, new Size(4, 4));
	Imgproc.cornerHarris(temp_mat, cornerMat, blockSize, apertureSize, k, Imgproc.BORDER_DEFAULT);
	Core.normalize(cornerMat, cornerMat, 0, 255, Core.NORM_MINMAX, CvType.CV_32FC1, new Mat());
	Core.convertScaleAbs(cornerMat, cornerMat);

	for (int j = 0; j < cornerMat.rows(); j++) {
	    for (int i = 0; i < cornerMat.cols(); i++) {
		if ((int) cornerMat.get(j, i)[0] > threashHold) {
		    cornersList.add(new Corner(i, j, cornerMat.get(j, i)[0]));
		}
	    }
	}
	Collections.sort(cornersList);

    }

    public static int cornerFindeByQuaters(int blockSize, int apertureSize, double k, int threashHold,
	    Mat mask, Rect rect, Mat cornerMat, List<Corner> cornersList) {
	boolean isRect = false;

	Mat temp_mat = new Mat();
	cornerMat = new Mat(mask.size(), CvType.CV_32FC1);
	Imgproc.cvtColor(mask, temp_mat, Imgproc.COLOR_RGB2GRAY);
	Imgproc.blur(temp_mat, temp_mat, new Size(4, 4));

	Corner Max_at_q0 = new Corner(0, 0, 0);
	Corner Max_at_q1 = new Corner(0, 0, 0);
	Corner Max_at_q2 = new Corner(0, 0, 0);
	Corner Max_at_q3 = new Corner(0, 0, 0);

	while (!isRect) {
	    Imgproc.cornerHarris(temp_mat, cornerMat, blockSize, apertureSize, k, Imgproc.BORDER_DEFAULT);
	    Core.normalize(cornerMat, cornerMat, 0, 255, Core.NORM_MINMAX, CvType.CV_32FC1, new Mat());
	    Core.convertScaleAbs(cornerMat, cornerMat);

	    int z;

	    for (int j = 0; j < cornerMat.rows(); j++) {
		for (int i = 0; i < cornerMat.cols(); i++) {
		    if ((int) cornerMat.get(j, i)[0] > threashHold) {
			if ((i == 278) && (j == 210)) {
			    z = 8;
			}
			if ((isCornerInQuater(i, j, 0, rect))
				&& (Max_at_q0.getGrade() < cornerMat.get(j, i)[0])) {
			    Max_at_q0 = new Corner(i, j, cornerMat.get(j, i)[0]);
			}
			if ((isCornerInQuater(i, j, 1, rect))
				&& (Max_at_q1.getGrade() < cornerMat.get(j, i)[0])) {
			    Max_at_q1 = new Corner(i, j, cornerMat.get(j, i)[0]);
			}
			if ((isCornerInQuater(i, j, 2, rect))
				&& (Max_at_q2.getGrade() < cornerMat.get(j, i)[0])) {
			    Max_at_q2 = new Corner(i, j, cornerMat.get(j, i)[0]);
			}
			if ((isCornerInQuater(i, j, 3, rect))
				&& (Max_at_q3.getGrade() < cornerMat.get(j, i)[0])) {
			    Max_at_q3 = new Corner(i, j, cornerMat.get(j, i)[0]);
			}

		    }
		}
	    }

	    Imshow im = new Imshow("Title");
	    Mat satImg = cornerMat.clone();
	    Core.circle(satImg, new Point(Max_at_q0.getI(), Max_at_q0.getJ()), 5, new Scalar(
		    Math.random() * 255), 1, 8, 0);
	    Core.circle(satImg, new Point(Max_at_q1.getI(), Max_at_q1.getJ()), 5, new Scalar(
		    Math.random() * 255), 1, 8, 0);
	    Core.circle(satImg, new Point(Max_at_q2.getI(), Max_at_q2.getJ()), 5, new Scalar(
		    Math.random() * 255), 1, 8, 0);
	    Core.circle(satImg, new Point(Max_at_q3.getI(), Max_at_q3.getJ()), 5, new Scalar(
		    Math.random() * 255), 1, 8, 0);
	    Core.rectangle(satImg, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y
		    + rect.height), new Scalar(255, 0, 0, 255), 1);

	    im.showImage(satImg);

	    isRect = isRect(Max_at_q0, Max_at_q1, Max_at_q2, Max_at_q3);
	    blockSize = blockSize * 2;
	    if (blockSize > rect.width / 2) {
		return -1;
	    }
	}
	cornersList.add(Max_at_q0);
	cornersList.add(Max_at_q1);
	cornersList.add(Max_at_q2);
	cornersList.add(Max_at_q3);

	return 1;
    }

    static boolean isRect(Corner q1, Corner q2, Corner q3, Corner q4) {

	return (is90Deg(q1, q2, q3)) && (is90Deg(q2, q3, q4)) && (is90Deg(q3, q4, q1))
		&& (is90Deg(q4, q1, q2));
    }

    static boolean is90Deg(Corner q1, Corner q2, Corner q3) {
	double angle1 = Math.toDegrees(Math.atan2(q1.getI() - q2.getI(), q1.getJ() - q2.getJ()));
	double angle2 = Math.toDegrees(Math.atan2(q2.getI() - q3.getI(), q2.getJ() - q3.getJ()));
	if (angle1 < 0) {
	    angle1 += 360;
	}
	if (angle2 < 0) {
	    angle2 += 360;
	}

	double angel;
	if (angle2 > angle1) {
	    angel = angle2;
	    angle2 = angle1;
	    angle1 = angel;
	}

	return ((angle1 - angle2 > 80) && (angle1 - angle2 < 100))
		|| ((angle1 - angle2 > 260) && (angle1 - angle2 < 280));
    }

    static boolean isCornerInQuater(int i, int j, int q, Rect rect) {
	int rectX = (rect.x + rect.width / 2);
	int rectY = (rect.y + rect.height / 2);

	if (!((i > rect.x) && (i < rect.x + rect.width) && (j > rect.y) && (j < rect.y + rect.height))) {
	    return false;
	}

	if (q == 0) {
	    if ((i <= rectX) && (j <= rectY)) {
		return true;
	    }
	}
	if (q == 1) {
	    if ((i <= rectX) && (j >= rectY)) {
		return true;
	    }
	}
	if (q == 2) {
	    if ((i >= rectX) && (j >= rectY)) {
		return true;
	    }
	}
	if (q == 3) {
	    if ((i >= rectX) && (j <= rectY)) {
		return true;
	    }
	}
	return false;
    }
}
