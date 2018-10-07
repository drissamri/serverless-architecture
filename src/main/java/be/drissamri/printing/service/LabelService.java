package be.drissamri.printing.service;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class LabelService {
    private static final Logger LOG = LogManager.getLogger(LabelService.class);
    private static final String LABEL_BUCKET = "serverless-architecture-generator-bucket-tst";
    private static final String FILENAME = "label/123456.zpl";
    private AmazonS3 amazonS3;

    public LabelService(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public void processMessages(List<SQSEvent.SQSMessage> messages) {
        messages.forEach(this::processMessage);
    }

    private void processMessage(final SQSEvent.SQSMessage message) {
        LOG.info("Processing message: {}", message.getMessageId());

        boolean labelGenerated = amazonS3.doesObjectExist(LABEL_BUCKET, FILENAME);
        S3ObjectInputStream labelInputStream = null;
        if(labelGenerated) {
            LOG.info("File: {} found.", FILENAME);
            labelInputStream = retrieveLabel();
        } else {
            LOG.info("File: {} not found", FILENAME);
            labelInputStream = generateLabel();
        }
    }

    private S3ObjectInputStream retrieveLabel() {
        final S3Object labelObject = amazonS3.getObject(LABEL_BUCKET, FILENAME);
        return labelObject.getObjectContent();
    }

    private S3ObjectInputStream generateLabel() {
        LOG.info("Generating {} at Centiro...", FILENAME);
        LOG.info("Generated {}.", FILENAME);

        final PutObjectResult s3UploadResult = amazonS3.putObject(LABEL_BUCKET, FILENAME, "ZPL CONTENTS");
        LOG.info("File: {} uploaded.", FILENAME);

        return null;
    }
}