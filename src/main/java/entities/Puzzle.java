package entities;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import org.opencv.core.Mat;

public class Puzzle
{
    private final Mat completePuzzle;
    private final List<PuzzlePiece> pieces;

    public Puzzle(Mat puzzle, List<PuzzlePiece> pieces)
    {
        this.completePuzzle = puzzle;
        this.pieces = ImmutableList.copyOf(pieces);
    }

    public Mat getCompletePuzzle()
    {
        return this.completePuzzle;
    }

    public List<PuzzlePiece> getPieces()
    {
        return this.pieces;
    }

}
