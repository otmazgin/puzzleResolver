package pieceRecognizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import entities.PuzzlePiece;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import com.atul.JavaOpenCV.Imshow;

public class PuzzlePieceCornerDetector
{
    public static void cornerFinder(int blockSize, int apertureSize, double k, int threashHold, Mat mask,
                                    Mat cornerMat, List<Corner> cornersList)
    {
        Mat temp_mat = new Mat();
        cornerMat = new Mat(mask.size(), CvType.CV_32FC1);
        Imgproc.cvtColor(mask, temp_mat, Imgproc.COLOR_RGB2GRAY);
        Imgproc.blur(temp_mat, temp_mat, new Size(4, 4));
        Imgproc.cornerHarris(temp_mat, cornerMat, blockSize, apertureSize, k, Imgproc.BORDER_DEFAULT);
        Core.normalize(cornerMat, cornerMat, 0, 255, Core.NORM_MINMAX, CvType.CV_32FC1, new Mat());
        Core.convertScaleAbs(cornerMat, cornerMat);

        for (int j = 0; j < cornerMat.rows(); j++)
        {
            for (int i = 0; i < cornerMat.cols(); i++)
            {
                if ((int) cornerMat.get(j, i)[0] > threashHold)
                {
                    cornersList.add(new Corner(i, j, cornerMat.get(j, i)[0]));
                }
            }
        }

        Collections.sort(cornersList);
    }

    public static int cornerFindeByQuaters(int blockSize, int apertureSize, double k, int threashHold,
                                           PuzzlePiece piece)
    {
        return PuzzlePieceCornerDetector.cornerFindeByQuaters(blockSize, apertureSize, k, threashHold,
                piece.getMask(), piece.getRect(), piece.getCornersList(), piece.getcontoure());
    }

    public static int cornerFindeByQuaters(int blockSize, int apertureSize, double k, int threashHold,
                                           Mat mask, Rect rect, List<Corner> cornersList, MatOfPoint conture)
    {
        boolean isRect = false;

        Mat tempMat = new Mat();
        Mat cornerMat = new Mat(mask.size(), CvType.CV_32FC1);
        Imgproc.cvtColor(mask, tempMat, Imgproc.COLOR_RGB2GRAY);
        Imgproc.blur(tempMat, tempMat, new Size(6, 6));

        Corner[] corners = new Corner[4];


        while (!isRect)
        {
            for (int i = 0; i < corners.length; i++)
            {
                corners[i] = new Corner(0, 0, 0);
            }
            Imgproc.cornerHarris(tempMat, cornerMat, blockSize, apertureSize, k, Imgproc.BORDER_DEFAULT);
            Core.normalize(cornerMat, cornerMat, 0, 255, Core.NORM_MINMAX, CvType.CV_32FC1, new Mat());
            Core.convertScaleAbs(cornerMat, cornerMat);

            MatOfInt hull = find_convex_hull(conture);
            ArrayList<double[]> hull_points = new ArrayList<double[]>();
            Imshow im1 = new Imshow("Title");
            Mat satImg1 = cornerMat.clone();
            Mat satImg2 = cornerMat.clone();

            for (int i = 0; i < hull.size().height; i++)
            {
                int index = (int) hull.get(i, 0)[0];
                hull_points.add(new double[]{
                        conture.get(index, 0)[0], conture.get(index, 0)[1]
                });
            }
            for (int n = 0; n < conture.size().height; n++)
            {
                Core.circle(satImg1, new Point(conture.get(n, 0)[0], conture.get(n, 0)[1]), 5, new Scalar(
                        Math.random() * 255), 1, 8, 0);
            }
            //	   im1.showImage(satImg1);

            for (int n = 0; n < hull.size().height; n++)
            {
                Core.circle(satImg2, new Point(hull_points.get(n)[0], hull_points.get(n)[1]), 5, new Scalar(
                        Math.random() * 255), 1, 8, 0);
                //				im1.showImage(satImg2);
            }
            //			   im1.showImage(satImg2);


            for (double[] point : hull_points)
            {
                double pixelValue = cornerMat.get((int) point[1], (int) point[0])[0];


                for (int c = 0; c < corners.length; c++)
                {
                    if ((isCornerInQuater((int) point[0], (int) point[1], c, rect)) && (corners[c].getGrade() < pixelValue))
                    {
                        corners[c] = new Corner((int) point[0], (int) point[1], pixelValue);
                    }
                }
            }

/*
            for (int j = 0; j < cornerMat.rows(); j++) {
				for (int i = 0; i < cornerMat.cols(); i++) {

					double pixelValue = cornerMat.get(j, i)[0];
					//   if ((int) pixelValue <= threashHold) {
						//	continue;
					//   }

					for (int c = 0; c < corners.length; c++) {
						if ((isCornerInQuater(i, j, c, rect)) && (corners[c].getGrade() < pixelValue)) {
							corners[c] = new Corner(i, j, pixelValue);
						}
					}
				}

			}
*/
            Imshow im = new Imshow("Title");
            Mat satImg = cornerMat.clone();

            for (Corner corner : corners)
            {
                Core.circle(satImg, new Point(corner.getX(), corner.getY()), 5, new Scalar(
                        Math.random() * 255), 1, 8, 0);
            }

            Core.rectangle(satImg, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y
                    + rect.height), new Scalar(255, 0, 0, 255), 1);

            //	   im.showImage(satImg);

            isRect = isRect(corners);
            blockSize = blockSize * 2;
            if (blockSize > rect.width / 2)
            {
                return -1;
            }
        }

        cornersList.addAll(Arrays.asList(corners));

        return 1;
    }

    public static boolean isRect(Corner[] corners)
    {
        if (corners.length != 4)
        {
            return false;
        } else
        {
            return isRect(corners[0], corners[1], corners[2], corners[3]);
        }
    }

    static boolean isRect(Corner q1, Corner q2, Corner q3, Corner q4)
    {

        return (is90Deg(q1, q2, q3)) && (is90Deg(q2, q3, q4)) && (is90Deg(q3, q4, q1))
                && (is90Deg(q4, q1, q2));
    }

    static boolean is90Deg(Corner q1, Corner q2, Corner q3)
    {
        double angle1 = Math.toDegrees(Math.atan2(q1.getX() - q2.getX(), q1.getY() - q2.getY()));
        double angle2 = Math.toDegrees(Math.atan2(q2.getX() - q3.getX(), q2.getY() - q3.getY()));
        if (angle1 < 0)
        {
            angle1 += 360;
        }
        if (angle2 < 0)
        {
            angle2 += 360;
        }

        double angel;
        if (angle2 > angle1)
        {
            angel = angle2;
            angle2 = angle1;
            angle1 = angel;
        }

        return ((angle1 - angle2 > 80) && (angle1 - angle2 < 100))
                || ((angle1 - angle2 > 260) && (angle1 - angle2 < 280));
    }

    static boolean isCornerInQuater(int i, int j, int q, Rect rect)
    {
        int rectX = (rect.x + rect.width / 2);
        int rectY = (rect.y + rect.height / 2);

        if (!((i > rect.x) && (i < rect.x + rect.width) && (j > rect.y) && (j < rect.y + rect.height)))
        {
            return false;
        }

        if (q == 0)
        {
            if ((i <= rectX) && (j <= rectY))
            {
                return true;
            }
        }
        if (q == 1)
        {
            if ((i <= rectX) && (j >= rectY))
            {
                return true;
            }
        }
        if (q == 2)
        {
            if ((i >= rectX) && (j >= rectY))
            {
                return true;
            }
        }
        if (q == 3)
        {
            if ((i >= rectX) && (j <= rectY))
            {
                return true;
            }
        }
        return false;
    }

    static MatOfInt find_convex_hull(MatOfPoint conture)
    {
        MatOfInt hull = new MatOfInt();
        Imgproc.convexHull(conture, hull);
        return hull;
    }


}
