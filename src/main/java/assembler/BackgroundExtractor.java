package assembler;

import org.opencv.core.Mat;

public enum BackgroundExtractor
{
    instance;

    public double calcBackgroundFromSource(Mat grayImage)
    {
        int sum = 0;

        sum += grayImage.get(0, 0)[0];
        sum += grayImage.get(0, 1)[0];
        sum += grayImage.get(0, 2)[0];
        sum += grayImage.get(1, 0)[0];
        sum += grayImage.get(2, 0)[0];

        sum += grayImage.get(grayImage.rows() - 1, 0)[0];
        sum += grayImage.get(grayImage.rows() - 1, 1)[0];
        sum += grayImage.get(grayImage.rows() - 1, 2)[0];
        sum += grayImage.get(grayImage.rows() - 2, 0)[0];
        sum += grayImage.get(grayImage.rows() - 3, 0)[0];

        sum += grayImage.get(0, grayImage.cols() - 1)[0];
        sum += grayImage.get(1, grayImage.cols() - 1)[0];
        sum += grayImage.get(2, grayImage.cols() - 1)[0];
        sum += grayImage.get(1, grayImage.cols() - 2)[0];
        sum += grayImage.get(2, grayImage.cols() - 3)[0];

        sum += grayImage.get(grayImage.rows() - 1, grayImage.cols() - 1)[0];
        sum += grayImage.get(grayImage.rows() - 1, grayImage.cols() - 2)[0];
        sum += grayImage.get(grayImage.rows() - 1, grayImage.cols() - 3)[0];
        sum += grayImage.get(grayImage.rows() - 2, grayImage.cols() - 1)[0];
        sum += grayImage.get(grayImage.rows() - 3, grayImage.cols() - 1)[0];

        return sum / 20;
    }

}
