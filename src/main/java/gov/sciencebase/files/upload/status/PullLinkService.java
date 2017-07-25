package gov.sciencebase.files.upload.status;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

@Service
class PullLinkService {

    private final SimpMessagingTemplate messagingTemplate;
    private final TransferManager transferManager;

    @Value("${s3.bucketname}")
    private String bucketName;

    @Value("${s3.bucketpath}")
    private String bucketPath;

    private final Logger log = LoggerFactory.getLogger(PullLinkService.class);

    @Autowired
    public PullLinkService(SimpMessagingTemplate messagingTemplate, TransferManager transferManager) {
        this.messagingTemplate = messagingTemplate;
        this.transferManager = transferManager;
    }

    void pull(UserLink userLink) {
        try {
            uploadLink(userLink);
        } catch (MalformedURLException mue) {
            log.error("Bad URL: " + userLink.url + ". " + mue.getLocalizedMessage(), mue);
            messagingTemplate.convertAndSendToUser(userLink.username, "/queue/pull-status",
                    PullStatusUpdate.createStatusUpdate(PullStatus.FAILED, userLink)
                            .withMessage("Bad URL: " + userLink.url)
            );
        } catch (IOException ioe) {
            log.error("Unable to connect to " + userLink.url + ". " + ioe.getLocalizedMessage(), ioe);
            messagingTemplate.convertAndSendToUser(userLink.username, "/queue/pull-status",
                    PullStatusUpdate.createStatusUpdate(PullStatus.FAILED, userLink)
                            .withMessage("Unable to connect to " + userLink.url)
            );
        }
    }

    private void uploadLink(UserLink userLink) throws IOException {
        URL url  = new URL(userLink.url);
        URLConnection httpURLConnection = url.openConnection();

        String fileName = userLink.fileName != null ? userLink.fileName : FilenameUtils.getName(userLink.url);
        ObjectMetadata metadata = metadataFromConnection(httpURLConnection);

        InputStream linkInputStream = httpURLConnection.getInputStream();

        PutObjectRequest request = new PutObjectRequest(
                bucketName,
                bucketPath + "/" + userLink.username + "/" + fileName,
                linkInputStream,
                metadata)
                .withGeneralProgressListener(
                        new S3PutObjectRequestListener(messagingTemplate, userLink, metadata.getContentLength())
        );

        transferManager.upload(request);
    }

    private ObjectMetadata metadataFromConnection(URLConnection connection) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(connection.getContentLengthLong());
        metadata.setContentType(connection.getContentType());
        return metadata;
    }
}
