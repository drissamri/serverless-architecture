package be.drissamri.printing;

import be.drissamri.printing.config.DaggerLambdaComponent;
import be.drissamri.printing.config.LambdaComponent;
import be.drissamri.printing.config.LambdaModule;
import be.drissamri.printing.service.LabelService;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LabelGeneratorLambda implements RequestHandler<SQSEvent, Void> {
    static final Logger LOG = LogManager.getLogger(LabelGeneratorLambda.class);
    private LabelService labelService;

    public LabelGeneratorLambda() {
      final LambdaComponent components = DaggerLambdaComponent.builder()
                .lambdaModule(new LambdaModule())
                .build();
      this.labelService = components.getLabelService();
    }

    public final Void handleRequest(final SQSEvent event, final Context context) {
        final long startTime = System.currentTimeMillis();
        LOG.info("Started processing: {}", event);

        if (event != null) {
            labelService.processMessages(event.getRecords());
        }

        LOG.info("End request: {} ms", this.getExecutionTime(startTime));
        return null;
    }



    private double getExecutionTime(final long started) {
        return (double) (System.currentTimeMillis() - started) / 1000.0;
    }
}
