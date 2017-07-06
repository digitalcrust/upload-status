package gov.sciencebase.files.upload.status;

import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressEventType;
import com.amazonaws.event.ProgressListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.concurrent.TimeUnit;

class S3PutObjectRequestListener implements ProgressListener {
    private final Logger log = LoggerFactory.getLogger(S3PutObjectRequestListener.class);
    public static final String QUEUE_PULL_STATUS = "/queue/pull-status";

    private SimpMessagingTemplate messagingTemplate;
    private UserLink userLink;
    private long totalBytesTransferred;
    private long totalBytes;

    S3PutObjectRequestListener(SimpMessagingTemplate messagingTemplate, UserLink userLink, long totalBytes) {
        this.messagingTemplate = messagingTemplate;
        this.userLink = userLink;
        this.totalBytes = totalBytes;
    }

    @Override
    public void progressChanged(ProgressEvent progressEvent) {
        if (progressEvent.getEventType() == ProgressEventType.TRANSFER_COMPLETED_EVENT) {
            messagingTemplate.convertAndSendToUser(userLink.username, QUEUE_PULL_STATUS, fetchCompletedPayload());
            return;
        }

        if (progressEvent.getEventType() == ProgressEventType.TRANSFER_FAILED_EVENT) {
            messagingTemplate.convertAndSendToUser(userLink.username, QUEUE_PULL_STATUS, fetchFailedPayload());
            return;
        }

        if (progressEvent.getBytesTransferred() > 0) {
            messagingTemplate.convertAndSendToUser(userLink.username, QUEUE_PULL_STATUS,
                    fetchInProgressPayLoad(progressEvent.getBytesTransferred()));
        }
    }

    private PullStatusUpdate fetchInProgressPayLoad(long bytesTransferred) {
        totalBytesTransferred += bytesTransferred;
        System.out.println("fetchInProgressPayLoad, userLink: " + userLink);

        return PullStatusUpdate.createStatusUpdate(PullStatus.STARTED, userLink)
                .withProgress(totalBytesTransferred, totalBytes);
    }

    private PullStatusUpdate fetchCompletedPayload() {
      System.out.println("fetchCompletedPayload, userLink: " + userLink);
        return PullStatusUpdate.createStatusUpdate(PullStatus.COMPLETED, userLink);
    }

    public PullStatusUpdate fetchFailedPayload() {
      System.out.println("fetchFailedPayload, userLink: " + userLink);
        return PullStatusUpdate.createStatusUpdate(PullStatus.FAILED, userLink);
    }
}
