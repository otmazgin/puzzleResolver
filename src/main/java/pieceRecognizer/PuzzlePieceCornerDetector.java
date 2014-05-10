package pieceRecognizer;

import java.util.Collections;
import java.util.LinkedList;

import com.atul.JavaOpenCV.Imshow;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

public class PuzzlePieceCornerDetector
{
    public static void corner_finder(int blockSize,
                                     int apertureSize,
                                     double k,
                                     int threashHold,
                                     Mat mask,
                                     Mat corner_mat,
                                     LinkedList<Corner> cornersList)
    {
        Mat temp_mat = new Mat();
        corner_mat = new Mat(mask.size(), CvType.CV_32FC1);
        Imgproc.cvtColor(mask, temp_mat, Imgproc.COLOR_RGB2GRAY);
        Imgproc.blur(temp_mat, temp_mat, new Size(4, 4));
        Imgproc.cornerHarris(temp_mat, corner_mat, blockSize, apertureSize, k, Imgproc.BORDER_DEFAULT);
        Core.normalize(corner_mat, corner_mat, 0, 255, Core.NORM_MINMAX, CvType.CV_32FC1, new Mat());
        Core.convertScaleAbs(corner_mat, corner_mat);


        for (int j = 0; j < corner_mat.rows(); j++)
        {
            for (int i = 0; i < corner_mat.cols(); i++)
            {
                if ((int) corner_mat.get(j, i)[0] > threashHold)
                {
                    cornersList.add(new Corner(i, j, corner_mat.get(j, i)[0]));
                }
            }
        }
        Collections.sort(cornersList);

    }

    public static int corner_finder_by_quaters(int blockSize,
                                               int apertureSize,
                                               double k,
                                               int threashHold,
                                               Mat mask,
                                               Rect rect,
                                               Mat corner_mat,
                                               LinkedList<Corner> cornersList)
    {
        boolean isRect = false;

        Mat temp_mat = new Mat();
        corner_mat = new Mat(mask.size(), CvType.CV_32FC1);
        Imgproc.cvtColor(mask, temp_mat, Imgproc.COLOR_RGB2GRAY);
        Imgproc.blur(temp_mat, temp_mat, new Size(4, 4));

        Corner Max_at_q0 = new Corner(0, 0, 0);
        Corner Max_at_q1 = new Corner(0, 0, 0);
        Corner Max_at_q2 = new Corner(0, 0, 0);
        Corner Max_at_q3 = new Corner(0, 0, 0);


        while (!isRect)
        {
            Imgproc.cornerHarris(temp_mat, corner_mat, blockSize, apertureSize, k, Imgproc.BORDER_DEFAULT);
            Core.normalize(corner_mat, corner_mat, 0, 255, Core.NORM_MINMAX, CvType.CV_32FC1, new Mat());
            Core.convertScaleAbs(corner_mat, corner_mat);

            int z;

            for (int j = 0; j < corner_mat.rows(); j++)
            {
                for (int i = 0; i < corner_mat.cols(); i++)
                {
                    if ((int) corner_mat.get(j, i)[0] > threashHold)
                    {
                        if ((i == 278) && (j == 210))
                            z = 8;
                        if ((is_corner_in_quater(i, j, 0, rect)) && (Max_at_q0.getGrade() < corner_mat.get(j, i)[0]))
                            Max_at_q0 = new Corner(i, j, corner_mat.get(j, i)[0]);
                        if ((is_corner_in_quater(i, j, 1, rect)) && (Max_at_q1.getGrade() < corner_mat.get(j, i)[0]))
                            Max_at_q1 = new Corner(i, j, corner_mat.get(j, i)[0]);
                        if ((is_corner_in_quater(i, j, 2, rect)) && (Max_at_q2.getGrade() < corner_mat.get(j, i)[0]))
                            Max_at_q2 = new Corner(i, j, corner_mat.get(j, i)[0]);
                        if ((is_corner_in_quater(i, j, 3, rect)) && (Max_at_q3.getGrade() < corner_mat.get(j, i)[0]))
                            Max_at_q3 = new Corner(i, j, corner_mat.get(j, i)[0]);


                    }
                }
            }

            Imshow im = new Imshow("Title");
            Mat satImg = corner_mat.clone();
            Core.circle(satImg, new Point(Max_at_q0.getI(), Max_at_q0.getJ()), 5, new Scalar(Math.random() * 255), 1, 8, 0);
            Core.circle(satImg, new Point(Max_at_q1.getI(), Max_at_q1.getJ()), 5, new Scalar(Math.random() * 255), 1, 8, 0);
            Core.circle(satImg, new Point(Max_at_q2.getI(), Max_at_q2.getJ()), 5, new Scalar(Math.random() * 255), 1, 8, 0);
            Core.circle(satImg, new Point(Max_at_q3.getI(), Max_at_q3.getJ()), 5, new Scalar(Math.random() * 255), 1, 8, 0);
            Core.rectangle(satImg, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(255, 0, 0, 255), 1);

            im.showImage(satImg);

            isRect = is_rect(Max_at_q0, Max_at_q1, Max_at_q2, Max_at_q3);
            blockSize = blockSize * 2;
            if (blockSize > rect.width / 2)
                return -1;
        }
        cornersList.add(Max_at_q0);
        cornersList.add(Max_at_q1);
        cornersList.add(Max_at_q2);
        cornersList.add(Max_at_q3);

        return 1;
    }

    static boolean is_rect(
            Corner q1,
            Corner q2,
            Corner q3,
            Corner q4)
    {

        return (is90Deg(q1, q2, q3)) &&
                (is90Deg(q2, q3, q4)) &&
                (is90Deg(q3, q4, q1)) &&
                (is90Deg(q4, q1, q2));
    }

    static boolean is90Deg(Corner q1, Corner q2, Corner q3)
    {
        double angle1 = Math.toDegrees(Math.atan2(q1.getI() - q2.getI(), q1.getJ() - q2.getJ()));
        double angle2 = Math.toDegrees(Math.atan2(q2.getI() - q3.getI(), q2.getJ() - q3.getJ()));
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

        return ((angle1 - angle2 > 80) && (angle1 - angle2 < 100)) || ((angle1 - angle2 > 260) && (angle1 - angle2 < 280));
    }

    static boolean is_corner_in_quater(int i, int j, int q, Rect rect)
    {
        int rect_x = (rect.x + rect.width / 2);
        int rect_y = (rect.y + rect.height / 2);

        if (!((i > rect.x) && (i < rect.x + rect.width) && (j > rect.y) && (j < rect.y + rect.height)))
            return false;

        if (q == 0)
        {
            if ((i <= rect_x) && (j <= rect_y))
                return true;
        }
        if (q == 1)
        {
            if ((i <= rect_x) && (j >= rect_y))
                return true;
        }
        if (q == 2)
        {
            if ((i >= rect_x) && (j >= rect_y))
                return true;
        }
        if (q == 3)
        {
            if ((i >= rect_x) && (j <= rect_y))
                return true;
        }
        return false;
    }
}
