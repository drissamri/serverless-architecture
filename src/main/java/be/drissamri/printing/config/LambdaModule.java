package be.drissamri.printing.config;

import be.drissamri.printing.service.LabelService;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class LambdaModule {
    @Provides
    @Singleton
    public LabelService provideLabelService(AmazonS3 amazonS3) {
        return new LabelService(amazonS3);
    }

    @Provides
    @Singleton
    public AmazonS3 provideAmazonS3() {
        return AmazonS3ClientBuilder.defaultClient();
    }
}
