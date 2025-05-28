package conf.middleware.execution;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ApplicationScoped
public class DatabaseExcecution {

    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    
    public ExecutorService customExecutor() {
        return executor;
    }

    @PreDestroy
    public void shutdown() {
        executor.shutdown();
    }
}



