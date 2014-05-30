package transformations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;

import pieceRecognizer.Corner;
import entities.Puzzle;
import entities.PuzzlePiece;

public enum PuzzlePieceTransformer {
    instance;

    private static final int WIDTH  = 1600;
    private static final int HEIGHT = 1200;

    public Puzzle transformPieces(Mat puzzleImg, List<PuzzlePiece> pieces, int puzzleColumns, int puzzleRows) {
	List<PuzzlePiece> transformedPieces = new ArrayList<>();
	List<Point> completedPuzzlePoints = Arrays.asList(new Point(0, 0), new Point(0, puzzleImg.height()),
		new Point(puzzleImg.width(), puzzleImg.height()));
	Mat transformedPuzzle = AffineTransformator.instance.transform(puzzleImg, completedPuzzlePoints,
		WIDTH, HEIGHT);

	for (PuzzlePiece puzzlePiece : pieces) {

	    boolean isPieceInLandscape = puzzleColumns > puzzleRows;
	    List<Point> pieceTransformPoints = adjustAndOrderPiecePointsForTransf(
		    puzzlePiece.getCornersList(), puzzlePiece.getRect(), isPieceInLandscape);

	    if (pieceTransformPoints.isEmpty()) {
		continue;
	    }

	    Mat puzzlePieceMat = puzzlePiece.getPiece();
	    int transPuzlePieceWidth = WIDTH / puzzleColumns;
	    int transPuzzlePieceHeight = HEIGHT / puzzleRows;

	    // Transform the piece for the template matching
	    Mat transformedPiece = AffineTransformator.instance.transform(puzzlePieceMat,
		    pieceTransformPoints, transPuzlePieceWidth, transPuzzlePieceHeight);

	    puzzlePiece.setTransformedPieceMatrix(transformedPiece);

	    transformedPieces.add(puzzlePiece);

	    // Transform the original piece to strait the contour
	    double growFactor = 2;
	    Mat rotatedImage = new Mat(new Size(transPuzlePieceWidth * (1 + growFactor),
		    transPuzzlePieceHeight * (1 + growFactor)), puzzlePieceMat.type());

	    Point rotatedImageMiddlePoint = new Point(rotatedImage.width() / 2, rotatedImage.height() / 2);
	    List<Point> rotatedPiecePoints = Arrays.asList(new Point(rotatedImageMiddlePoint.x
		    - transPuzlePieceWidth / 2, rotatedImageMiddlePoint.y - transPuzzlePieceHeight / 2),
		    new Point(rotatedImageMiddlePoint.x - transPuzlePieceWidth / 2, rotatedImageMiddlePoint.y
			    + transPuzzlePieceHeight / 2), new Point(rotatedImageMiddlePoint.x
			    + transPuzlePieceWidth / 2, rotatedImageMiddlePoint.y + transPuzzlePieceHeight
			    / 2));

	    AffineTransformator.instance.transform(puzzlePieceMat, rotatedImage, pieceTransformPoints,
		    rotatedPiecePoints);

	    puzzlePiece.setRotatedImage(rotatedImage);
	}

	return new Puzzle(transformedPuzzle, transformedPieces);

    }

    private List<Point> adjustAndOrderPiecePointsForTransf(List<Corner> cornersList, Rect boundingBox,
	    boolean isLandscape) {
	if (cornersList.size() != 4) {
	    return Collections.emptyList();
	}

	for (Corner corner : cornersList) {
	    corner.setX(corner.getX() - boundingBox.x);
	    corner.setY(corner.getY() - boundingBox.y);
	}

	boolean isPieceInLandscape = isPiecePointsInLandscape(cornersList);

	return isPieceInLandscape && isLandscape ? Corner.toPoints(cornersList.subList(0,
		cornersList.size() - 1)) : Corner.toPoints(cornersList.subList(1, cornersList.size()));
    }

    private boolean isPiecePointsInLandscape(List<Corner> cornersList) {

	boolean isPieceInLandscape = Boolean.FALSE;
	if (cornersList.size() >= 3) {
	    double firstDiff = cornersList.get(0).distance(cornersList.get(1));
	    double secondDiff = cornersList.get(1).distance(cornersList.get(2));
	    isPieceInLandscape = secondDiff - firstDiff > 0;
	}

	return isPieceInLandscape;
    }
}
