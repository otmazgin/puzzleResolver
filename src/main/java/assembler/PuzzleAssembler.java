package assembler;

import org.opencv.core.Mat;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public enum PuzzleAssembler
{
    instance;

    public void assemblePieces(Collection<Mat> puzzlePieces, Mat puzzle)
    {
        ExecutorService executorService = Executors.newFixedThreadPool(Math.min(puzzlePieces.size(), 100));

        for (Mat puzzlePiece : puzzlePieces)
        {
            executorService.submit(PuzzlePieceAssembler.createAssembler(puzzlePiece, puzzle));
        }

        executorService.shutdown();
    }
}
