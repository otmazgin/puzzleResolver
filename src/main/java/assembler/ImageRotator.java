package assembler;

import org.opencv.core.Mat;

import static org.opencv.core.Core.flip;
import static org.opencv.core.Core.transpose;

enum ImageRotator
{
    instance;

    Mat rotateToLeft(Mat template)
    {
        Mat rotatedTemplate = new Mat();

        transpose(template, rotatedTemplate);

        flip(rotatedTemplate, rotatedTemplate, 0);

        return rotatedTemplate;
    }
}
