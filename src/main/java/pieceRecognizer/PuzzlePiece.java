package pieceRecognizer;

import java.util.ArrayList;
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
    private Mat	      originaSrc;
    private Mat	      mask;
    private Rect	     rect;
    private List<MatOfPoint> contoures;
    private double	   area;
    private Mat	      cornerMat;
    static Scalar	    wColor = new Scalar(255, 255, 255);
    private Mat	      hierarchy;
    private List<Corner>     cornersList;
    private int	      contoureNum;

    public PuzzlePiece(Mat originalSrc, List<MatOfPoint> contoures, Mat hierarchy, int contoure) {
	super();
	this.originaSrc = originalSrc;
	this.contoures = contoures;
	this.hierarchy = hierarchy;
	this.contoureNum = contoure;
	this.mask = new Mat(originalSrc.size(), CvType.CV_8UC3);
	Imgproc.drawContours(this.mask, this.contoures, contoure, wColor, -1, 1, this.hierarchy, 0,
		new Point());
	this.rect = Imgproc.boundingRect(contoures.get(contoure));
	this.area = this.rect.area();
	this.cornersList = new ArrayList<Corner>();
    }

    public void drawPice(Mat drawing) {
	Imgproc.drawContours(drawing, getcontoures(), getContoureNum(), wColor, -1, 1, getHierarchy(), 0,
		new Point());
    }

    public void drawCorners(Mat drawing) {
	for (int i = 0; i < getCornersList().size(); i++) {
	    Core.circle(drawing, new Point(getCornersList().get(i).getI(), getCornersList().get(i).getJ()),
		    5, new Scalar(Math.random() * 255), 1, 8, 0);
	}
    }

    public int getContoureNum() {
	return this.contoureNum;
    }

    public void setContoureNum(int contoureNum) {
	this.contoureNum = contoureNum;
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

    public List<MatOfPoint> getcontoures() {
	return this.contoures;
    }

    public void setcontoures(List<MatOfPoint> contoures) {
	this.contoures = contoures;
    }

    public double getArea() {
	return this.area;
    }

    public void setArea(double area) {
	this.area = area;
    }

    public Mat getCornerMat() {
	return this.cornerMat;
    }

    public void setCornerMat(Mat cornerMat) {
	this.cornerMat = cornerMat;
    }

    public List<Corner> getCornersList() {
	return this.cornersList;
    }

    public void setCornersList(List<Corner> cornersList) {
	this.cornersList = cornersList;
    }
}
