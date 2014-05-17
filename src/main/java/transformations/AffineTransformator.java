package transformations;

import java.util.Arrays;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public enum AffineTransformator {

    instance;

    /**
     * @param srcImage
     * @param srcPoints
     * @param width
     * @param height
     * @return
     */
    public Mat transform(Mat srcImage, List<Point> srcPoints, int width, int height) {

	List<Point> dstPoints = Arrays
		.asList(new Point(0, 0), new Point(0, height), new Point(width, height));
	return this.transform(srcImage, new Mat(new Size(width, height), srcImage.type()), srcPoints,
		dstPoints);
    }

    /**
     * Execute affine transform on <code>srcImg</code> using <code>srcPoints</code> and
     * <code>dstPoint<code/> and write it to <code>dstImage<code/>
     * 
     * @param srcImage
     *            - input image
     * @param dstImage
     *            - output image
     * @param srcPoints
     *            - 3 points in the <code>srcImg</code> to be correlated to <code>dstPoints</code>
     * @param dstPoints
     *            - 3 points in the <code>dstImg</code> to be correlated by <code>srcPoints</code>
     * @return <code>dstImg</code>
     */
    public Mat transform(Mat srcImage, Mat dstImage, List<Point> srcPoints, List<Point> dstPoints) {

	MatOfPoint2f srcMatPoints = new MatOfPoint2f(listToArray(srcPoints));
	MatOfPoint2f destMatPoints = new MatOfPoint2f(listToArray(dstPoints));

	Mat affineTransformMat = Imgproc.getAffineTransform(srcMatPoints, destMatPoints);

	Imgproc.warpAffine(srcImage, dstImage, affineTransformMat, dstImage.size());

	return dstImage;

    }

    private Point[] listToArray(List<Point> srcPoints) {
	Point[] srcPointArr = new Point[srcPoints.size()];
	for (int i = 0; i < srcPointArr.length; i++) {
	    srcPointArr[i] = srcPoints.get(i);
	}
	return srcPointArr;
    }
}
