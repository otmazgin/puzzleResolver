package assembler;

import assembler.templateMatcher.Match;
import com.google.common.collect.Maps;
import entities.Puzzle;
import entities.PuzzlePiece;
import utillities.ValueFromFuture;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.opencv.core.Core.putText;

public enum PuzzleAssembler
{
    instance;

    private static final int numberOfCPUCores = 4;

    public Map<PuzzlePiece, Match> assemble(Puzzle puzzle) throws Exception
    {
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfCPUCores);

        Map<PuzzlePiece, Future<Match>> futures = new HashMap<>();

        for (PuzzlePiece puzzlePiece : puzzle.getPieces())
        {
            futures.put
                    (
                            puzzlePiece,
                            executorService.submit
                                    (
                                            PuzzlePieceAssembler.createAssembler(puzzlePiece, puzzle.getCompletePuzzle())
                                    )
                    );
        }

        executorService.shutdown();

        return Maps.transformValues(futures, ValueFromFuture.<Match>create());
    }
}
