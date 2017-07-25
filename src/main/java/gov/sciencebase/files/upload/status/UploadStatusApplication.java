package gov.sciencebase.files.upload.status;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.transfer.TransferManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UploadStatusApplication {
    @Bean(destroyMethod = "shutdownNow")
    public TransferManager transferManager() {
        return new TransferManager(new AmazonS3Client());
    }
    
  public static void main(String[] args) {
    SpringApplication.run(UploadStatusApplication.class, args);
  }
}