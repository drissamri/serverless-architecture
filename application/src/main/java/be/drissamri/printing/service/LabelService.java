package be.drissamri.printing.service;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.UUID;

public class LabelService {
    private static final Logger LOG = LogManager.getLogger(LabelService.class);
    private static final String LABEL_BUCKET = "serverless-architecture-dev";
    private AmazonS3 amazonS3;

    public LabelService(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public void processMessages(List<SQSEvent.SQSMessage> messages) {
        messages.forEach(this::processMessage);
    }

    private void processMessage(final SQSEvent.SQSMessage message) {
        LOG.info("Processing message: {}", message.getMessageId());

        String uniqueFile = "label/" + UUID.randomUUID().toString() + "123456.zpl";
        boolean labelGenerated = amazonS3.doesObjectExist(LABEL_BUCKET, uniqueFile);
        S3ObjectInputStream labelInputStream = null;
        if(labelGenerated) {
            LOG.info("File: {} found.", uniqueFile);
            labelInputStream = retrieveLabel(uniqueFile);
        } else {
            LOG.info("File: {} not found", uniqueFile);
            labelInputStream = generateLabel(uniqueFile);
        }
    }

    private S3ObjectInputStream retrieveLabel(String fileName) {
        final S3Object labelObject = amazonS3.getObject(LABEL_BUCKET, fileName);
        return labelObject.getObjectContent();
    }

    private S3ObjectInputStream generateLabel(String fileName) {
        LOG.info("Generating {} at EXTERNAL VENDOR...", fileName);
        LOG.info("Generated {}.", fileName);

        LOG.info("Uploading to bucket: {}.", LABEL_BUCKET);
        final PutObjectResult s3UploadResult = amazonS3.putObject(LABEL_BUCKET, fileName, "ZPL CONTENTS");
        LOG.info("File: {} uploaded.", fileName);

        return null;
    }
}