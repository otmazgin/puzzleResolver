package assembler;

import org.opencv.core.Mat;

import java.awt.*;

public enum AverageColorCalculator
{
    instance;

    public double averageBackgroundOfOneDimension(Mat image)
    {
        double[] average = averageColorsOfPoints
                (
                        image,
                        1,
                        getPointsAroundBordersOf(image)
                );

        return average[0];
    }

    public double averageAround(Mat image, Point center)
    {
        double[] average = averageColorsOfPoints
                (
                        image,
                        1,
                        new Point(center.x - 2, center.y - 2),
                        new Point(center.x - 2, center.y - 1),
                        new Point(center.x - 2, center.y),
                        new Point(center.x - 2, center.y + 1),
                        new Point(center.x - 2, center.y + 2),
                        new Point(center.x - 1, center.y - 2),
                        new Point(center.x - 1, center.y - 1),
                        new Point(center.x - 1, center.y),
                        new Point(center.x - 1, center.y + 1),
                        new Point(center.x - 1, center.y + 2),
                        new Point(center.x, center.y - 2),
                        new Point(center.x, center.y - 1),
                        new Point(center.x, center.y + 1),
                        new Point(center.x, center.y + 2),
                        new Point(center.x + 1, center.y - 2),
                        new Point(center.x + 1, center.y - 1),
                        new Point(center.x + 1, center.y),
                        new Point(center.x + 1, center.y + 1),
                        new Point(center.x + 1, center.y + 2),
                        new Point(center.x + 2, center.y - 2),
                        new Point(center.x + 2, center.y - 1),
                        new Point(center.x + 2, center.y),
                        new Point(center.x + 2, center.y + 1),
                        new Point(center.x + 2, center.y + 2)
                );

        return average[0];
    }

    private Point[] getPointsAroundBordersOf(Mat image)
    {
        return new Point[]{new Point(0, 0),
                new Point(1, 0),
                new Point(2, 0),
                new Point(0, 1),
                new Point(0, 2),
                new Point(0, image.rows() - 1),
                new Point(1, image.rows() - 1),
                new Point(2, image.rows() - 1),
                new Point(0, image.rows() - 2),
                new Point(0, image.rows() - 3),
                new Point(image.cols() - 1, 0),
                new Point(image.cols() - 1, 1),
                new Point(image.cols() - 1, 2),
                new Point(image.cols() - 2, 1),
                new Point(image.cols() - 3, 2),
                new Point(image.cols() - 1, image.rows() - 1),
                new Point(image.cols() - 2, image.rows() - 1),
                new Point(image.cols() - 3, image.rows() - 1),
                new Point(image.cols() - 1, image.rows() - 2),
                new Point(image.cols() - 1, image.rows() - 3)};
    }

    public double[] averageColorsOfPoints(Mat image, int numOfDimensions, Point... selectedPoints)
    {
        double[] result = new double[numOfDimensions];

        for (Point selectedPoint : selectedPoints)
        {
            double[] colors = image.get(selectedPoint.y, selectedPoint.x);

            for (int dimension = 0; dimension < result.length; dimension++)
            {
                result[dimension] += colors[dimension];
            }
        }

        for (int dimension = 0; dimension < result.length; dimension++)
        {
            result[dimension] = result[dimension] / selectedPoints.length;
        }

        return result;
    }
}
