package assembler;

import assembler.templateMatcher.Match;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import utillities.Utilities;
import utillities.ValueFromFuture;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
            Utilities.drawRect(getRectangleOf(futureMatch.getValue()), puzzle);
        }

        Utilities.writeImageToFile(puzzle, "matches.jpg");

        System.out.println("Finished assembling at: " + new Date());

        return Maps.transformValues(futures, ValueFromFuture.<Match>create());
    }

    private Rect getRectangleOf(Future<Match> future) throws InterruptedException, ExecutionException
    {
        Match match = future.get();
        return new Rect(match.getMatchPoint(), new Size(match.getWidth(), match.getHeight()));
    }
}
