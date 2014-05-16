package assembler;

import assembler.templateMatcher.Match;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import utillities.Utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public enum PuzzleAssembler
{
    instance;

    private static final int numberOfCPUCores = 4;

    public void assemblePieces(Collection<Mat> puzzlePieces, Mat puzzle, double[] backgroundColor)
    {
        System.out.println("Started assembling at: " + new Date());

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfCPUCores);

        Collection<Future<Match>> futures = new ArrayList<>();

        for (Mat puzzlePiece : puzzlePieces)
        {
            futures.add(executorService.submit(PuzzlePieceAssembler.createAssembler(puzzlePiece, puzzle, backgroundColor)));
        }

        executorService.shutdown();

        for (Future<Match> future : futures)
        {
            try
            {
                Match match = future.get();
                Rect matchRectangle = new Rect(match.getMatchPoint(), new Size(match.getWidth(), match.getHeight()));

                Utilities.drawRect(matchRectangle, puzzle);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        Utilities.writeImageToFile(puzzle, "matches.jpg");

        System.out.println("Finished assembling at: " + new Date());
    }
}
