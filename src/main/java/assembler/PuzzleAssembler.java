package assembler;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
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

    public void assemblePieces(Collection<Mat> puzzlePieces, Mat puzzle)
    {
        System.out.println("Started assembling at: " + new Date());

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfCPUCores);

        Collection<Future<Rect>> futures = new ArrayList<>();

        for (Mat puzzlePiece : puzzlePieces)
        {
            futures.add(executorService.submit(PuzzlePieceAssembler.createAssembler(puzzlePiece, puzzle)));
        }

        executorService.shutdown();

        for (Future<Rect> future : futures)
        {
            try
            {
                Utilities.drawRect(future.get(), puzzle);
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
