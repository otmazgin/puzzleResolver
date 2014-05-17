package assembler;

import assembler.templateMatcher.Match;
import com.google.common.collect.Maps;
import org.opencv.core.*;
import utillities.Utilities;
import utillities.ValueFromFuture;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.opencv.core.Core.putText;

public enum PuzzleAssembler
{
    instance;

    private static final int numberOfCPUCores = 4;

    public Map<Integer, Match> assemblePieces(Map<Integer, Mat> puzzlePieces, Mat puzzle, double[] backgroundColor) throws Exception
    {
        System.out.println("Started assembling at: " + new Date());

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfCPUCores);

        Map<Integer, Future<Match>> futures = new HashMap<>();

        for (Map.Entry<Integer, Mat> puzzlePiece : puzzlePieces.entrySet())
        {
            futures.put
                    (
                            puzzlePiece.getKey(),
                            executorService.submit(PuzzlePieceAssembler.createAssembler(puzzlePiece.getValue(), puzzle, backgroundColor))
                    );
        }

        executorService.shutdown();

        for (Map.Entry<Integer, Future<Match>> futureMatch : futures.entrySet())
        {
            drawPieceNumberOn(puzzle, futureMatch);
        }

        Utilities.writeImageToFile(puzzle, "matches.jpg");

        System.out.println("Finished assembling at: " + new Date());

        return Maps.transformValues(futures, ValueFromFuture.<Match>create());
    }

    private void drawPieceNumberOn(Mat puzzle, Map.Entry<Integer, Future<Match>> futureMatch) throws InterruptedException, ExecutionException
    {
        Match match = futureMatch.getValue().get();
        Point matchPoint = match.getMatchPoint();
        Point pieceCenter = new Point(matchPoint.x + (match.getWidth() / 2) - 30, matchPoint.y + (match.getHeight() / 2));

        putText(puzzle, futureMatch.getKey().toString(), pieceCenter, 1, 10, new Scalar(0, 0, 255), 15);
    }
}
