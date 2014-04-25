package piceRecognizer;

import java.util.LinkedList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class PuzzelPice {

	private Mat original_src;
	private Mat mask;
	private Rect rect;
	private List<MatOfPoint> contoures;
	private double area;
	private Mat corner_mat;
	static Scalar w_color =new  Scalar( 255,255, 255 );
	private Mat hierarchy;
	private LinkedList <Corner> cornersList ;
	private int contoure_num;
	
	
	public PuzzelPice(Mat original_src, List<MatOfPoint> contoures,  Mat hierarchy ,int contoure  ) {
		super();
		this.original_src = original_src;
		this.contoures = contoures;
		this.hierarchy = hierarchy;
		this.contoure_num = contoure;
		mask = new Mat( original_src.size(), CvType.CV_8UC3);
		Imgproc.drawContours( mask, this.contoures, contoure, w_color, - 1, 1, this.hierarchy, 0, new Point() );
		rect = Imgproc.boundingRect(contoures.get(contoure));
		area = rect.area();
		cornersList= new LinkedList<Corner> ();
		
		
	}
	
	public void drawPice (Mat drawing ){
	

		Imgproc.drawContours( drawing, this.getcontoures(), this.getContoure_num(), w_color, - 1, 1, this.getHierarchy(),0, new Point() );
	}
	
	public void drawcorners (Mat drawing ){
		
		for( int i = 0; i< this.getCornersList().size(); i++ ){
			
			  Core.circle( drawing, new Point( this.getCornersList().get(i).getI(), this.getCornersList().get(i).getJ()), 5, new  Scalar(Math.random()*255), 1, 8, 0 );
			
		}	}
	
	
	
	
	public int getContoure_num() {
		return contoure_num;
	}





	public void setContoure_num(int contoure_num) {
		this.contoure_num = contoure_num;
	}





	public Mat getHierarchy() {
		return hierarchy;
	}





	public void setHierarchy(Mat hierarchy) {
		this.hierarchy = hierarchy;
	}





	public Mat getOriginal_src() {
		return original_src;
	}
	public void setOriginal_src(Mat original_src) {
		this.original_src = original_src;
	}
	public Mat getMask() {
		return mask;
	}
	public void setMask(Mat mask) {
		this.mask = mask;
	}
	public Rect getRect() {
		return rect;
	}
	public void setRect(Rect rect) {
		this.rect = rect;
	}
	public List<MatOfPoint> getcontoures() {
		return contoures;
	}
	public void setcontoures(List<MatOfPoint> contoures) {
		this.contoures = contoures;
	}
	public double getArea() {
		return area;
	}
	public void setArea(double area) {
		this.area = area;
	}
	public Mat getCorner_mat() {
		return corner_mat;
	}
	public void setCorner_mat(Mat corner_mat) {
		this.corner_mat = corner_mat;
	}

	public LinkedList<Corner> getCornersList() {
		return cornersList;
	}
	public void setCornersList(LinkedList<Corner> cornersList) {
		this.cornersList = cornersList;
	}
	
	
	
}
