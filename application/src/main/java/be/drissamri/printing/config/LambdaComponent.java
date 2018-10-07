package be.drissamri.printing.config;

import be.drissamri.printing.service.LabelService;
import com.amazonaws.services.s3.AmazonS3;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = LambdaModule.class)
public interface LambdaComponent {
     LabelService getLabelService();

     AmazonS3 getAmazonS3();
}
