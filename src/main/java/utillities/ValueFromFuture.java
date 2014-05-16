package utillities;

import com.google.common.base.Function;

import java.util.concurrent.Future;

public class ValueFromFuture<T> implements Function<Future<T>, T>
{
    public static <T> ValueFromFuture<T> create()
    {
        return new ValueFromFuture<>();
    }

    @Override
    public T apply(Future<T> future)
    {
        try
        {
            return future.get();
        } catch (Exception exception)
        {
            exception.printStackTrace();
            return null;
        }
    }
}
