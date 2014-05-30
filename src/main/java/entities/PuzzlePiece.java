package entities;

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
import pieceRecognizer.Corner;

public class PuzzlePiece
{
    private static Scalar wColor = new Scalar(255, 255, 255);
    private static int pieceIndex = 1;

    private final int pieceNumber;
    private final Mat originalSource;
    private final Mat mask;
    private final Mat hierarchy;
    private final MatOfPoint contoure;
    private final List<Corner> cornersList;

    private Rect rect;
    private Mat transformedPieceMatrix;
    private double bestRotationAngle;

    public PuzzlePiece(Mat originalSrc, MatOfPoint contoure, Mat hierarchy)
    {
        this.pieceNumber = pieceIndex++;
        this.originalSource = originalSrc;
        this.contoure = contoure;
        this.hierarchy = hierarchy;
        this.mask = new Mat(originalSrc.size(), CvType.CV_8UC3);
        Imgproc.drawContours(this.mask, Arrays.asList(contoure), 0, wColor, -1, 1, this.hierarchy, 0,
                new Point());
        this.rect = Imgproc.boundingRect(contoure);
        this.cornersList = new ArrayList<>();
    }

    public int getPieceNumber()
    {
        return pieceNumber;
    }

    public Mat getOriginalSource()
    {
        return this.originalSource;
    }

    public Mat getMask()
    {
        return this.mask;
    }

    public Mat getHierarchy()
    {
        return this.hierarchy;
    }

    public Rect getRect()
    {
        return this.rect;
    }

    public void setRect(Rect rect)
    {
        this.rect = rect;
    }

    public MatOfPoint getcontoure()
    {
        return this.contoure;
    }

    public List<Corner> getCornersList()
    {
        return this.cornersList;
    }

    public Mat getTransformedPieceMatrix()
    {
        return transformedPieceMatrix;
    }

    public void setTransformedPieceMatrix(Mat transformedPieceMatrix)
    {
        this.transformedPieceMatrix = transformedPieceMatrix;
    }

    public Mat getPiece()
    {
        return new Mat(this.originalSource, this.rect);
    }

    public void drawPiece(Mat drawing)
    {
        Imgproc.drawContours(drawing, Arrays.asList(getcontoure()), 0, wColor, -1, 1, getHierarchy(), 0,
                new Point());
    }

    public void drawCorners(Mat drawing)
    {
        for (Corner corner : getCornersList())
        {
            Core.circle(drawing, new Point(corner.getX(), corner.getY()), 5, new Scalar(Math.random() * 255),
                    1, 8, 0);
        }
    }

    public double getBestRotationAngle()
    {
        return bestRotationAngle;
    }

    public void setBestRotationAngle(double bestRotationAngle)
    {
        this.bestRotationAngle = bestRotationAngle;
    }
}
