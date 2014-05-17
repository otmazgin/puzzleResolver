package pieceRecognizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class PuzzlePiece {
    private Mat	  originaSrc;
    private Mat	  mask;
    private Rect	 rect;
    private MatOfPoint   contoure;
    static Scalar	wColor = new Scalar(255, 255, 255);
    private Mat	  hierarchy;
    private List<Corner> cornersList;

    public PuzzlePiece(Mat originalSrc, MatOfPoint contoure, Mat hierarchy) {
	super();
	this.originaSrc = originalSrc;
	this.contoure = contoure;
	this.hierarchy = hierarchy;
	this.mask = new Mat(originalSrc.size(), CvType.CV_8UC3);
	Imgproc.drawContours(this.mask, Arrays.asList(contoure), 0, wColor, -1, 1, this.hierarchy, 0,
		new Point());
	this.rect = Imgproc.boundingRect(contoure);
	this.cornersList = new ArrayList<Corner>();
    }

    public Mat getPiece() {
	return new Mat(this.originaSrc, this.rect);
    }

    public void drawPiece(Mat drawing) {
	Imgproc.drawContours(drawing, Arrays.asList(getcontoure()), 0, wColor, -1, 1, getHierarchy(), 0,
		new Point());
    }

    public void drawCorners(Mat drawing) {
	for (Corner corner : getCornersList()) {
	    Core.circle(drawing, new Point(corner.getX(), corner.getY()), 5, new Scalar(Math.random() * 255),
		    1, 8, 0);
	}
    }

    public Mat getHierarchy() {
	return this.hierarchy;
    }

    public void setHierarchy(Mat hierarchy) {
	this.hierarchy = hierarchy;
    }

    public Mat getOriginalSrc() {
	return this.originaSrc;
    }

    public void setOriginalSrc(Mat originalSrc) {
	this.originaSrc = originalSrc;
    }

    public Mat getMask() {
	return this.mask;
    }

    public void setMask(Mat mask) {
	this.mask = mask;
    }

    public Rect getRect() {
	return this.rect;
    }

    public void setRect(Rect rect) {
	this.rect = rect;
    }

    public MatOfPoint getcontoure() {
	return this.contoure;
    }

    public void setcontoures(MatOfPoint contoure) {
	this.contoure = contoure;
    }

    public double getArea() {
	return this.rect.area();
    }

    public List<Corner> getCornersList() {
	return this.cornersList;
    }

    public void setCornersList(List<Corner> cornersList) {
	this.cornersList = cornersList;
    }
}
