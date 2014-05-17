package transformations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import pieceRecognizer.Corner;
import pieceRecognizer.PuzzlePiece;
import entities.Puzzle;

public enum PuzzlePieceTransformer {

    instance;

    public static final int WIDTH  = 1200;
    public static final int HEIGHT = 1600;

    public Puzzle transformPieces(Mat puzzleImg, List<PuzzlePiece> pieces, int puzzleColumns, int puzzleRows) {

	List<Mat> transformedPieces = new ArrayList<>();
	List<Point> completedPuzzlePoints = Arrays.asList(new Point(0, 0), new Point(puzzleImg.height(), 0),
		new Point(puzzleImg.width(), puzzleImg.height()));
	Mat transformedPuzzle = AffineTransformator.instance.transform(puzzleImg, completedPuzzlePoints,
		WIDTH, HEIGHT);

	for (PuzzlePiece puzzlePiece : pieces) {
	    List<Point> pieceTransformPoints = orderPiecePointsForTransf(puzzlePiece.getCornersList(),
		    puzzleColumns > puzzleRows);
	    Mat transformedPiece = AffineTransformator.instance.transform(puzzlePiece.getPiece(),
		    pieceTransformPoints, WIDTH / puzzleColumns, HEIGHT / puzzleRows);
	    transformedPieces.add(transformedPiece);
	}

	return new Puzzle(transformedPuzzle, transformedPieces);

    }

    private List<Point> orderPiecePointsForTransf(List<Corner> cornersList, boolean isLandscape) {
	if (cornersList.size() != 4) {
	    return Collections.emptyList();
	}

	double firstDiff = cornersList.get(0).distance(cornersList.get(1));
	double secondDiff = cornersList.get(1).distance(cornersList.get(2));
	boolean isPieceInLandscape = secondDiff - firstDiff > 0;

	return isPieceInLandscape && isLandscape ? Corner.toPoints(cornersList.subList(0,
		cornersList.size() - 1)) : Corner.toPoints(cornersList.subList(1, cornersList.size()));
    }
}
