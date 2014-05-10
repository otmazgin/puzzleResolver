package assembler;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import utillities.Utilities;

import java.util.Date;

import static org.opencv.core.Core.flip;
import static org.opencv.core.Core.transpose;
import static org.opencv.imgproc.Imgproc.getRotationMatrix2D;
import static org.opencv.imgproc.Imgproc.warpAffine;

enum ImageRotator
{
    instance;

    Mat rotateLeft(Mat image)
    {
        Mat rotatedTemplate = new Mat();

        transpose(image, rotatedTemplate);

        flip(rotatedTemplate, rotatedTemplate, 0);

        return rotatedTemplate;
    }
}