package transformations;

import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

public enum AffineTransformator {

    instance;

    /**
     * Execute affine transform on <code>srcImg</code> using <code>srcPoints</code> and
     * <code>dstPoint<code/> and write it to <code>dstImage<code/>
     * 
     * @param srcImage
     *            - input image
     * @param dstImage
     *            - output image
     * @param srcPoints
     *            - points in the <code>srcImg</code> to be correlated to <code>dstPoints</code>
     * @param dstPoints
     *            - points in the <code>dstImg</code> to be correlated by <code>srcPoints</code>
     * @return <code>dstImg</code>
     */
    public Mat transform(Mat srcImage, Mat dstImage, List<Point> srcPoints, List<Point> dstPoints) {
	MatOfPoint2f srcMatPoints = new MatOfPoint2f((Point[]) srcPoints.toArray());
	MatOfPoint2f destMatPoints = new MatOfPoint2f((Point[]) dstPoints.toArray());

	Mat affineTransformMat = Imgproc.getAffineTransform(srcMatPoints, destMatPoints);

	Imgproc.warpAffine(srcImage, dstImage, affineTransformMat, dstImage.size());

	return dstImage;

    }
}
