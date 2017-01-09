package gov.sciencebase.files.upload.status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import rx.Observer;

class PullStatusObserver implements Observer<PullStatusUpdate> {

    public static final String QUEUE_PULL_STATUS = "/queue/pull-status";

    private final Logger log = LoggerFactory.getLogger(PullStatusObserver.class);

    private SimpMessagingTemplate messagingTemplate;
    private UserLink userLink;


    PullStatusObserver(SimpMessagingTemplate messagingTemplate, UserLink userLink) {
        this.messagingTemplate = messagingTemplate;
        this.userLink = userLink;
    }

    @Override
    public void onCompleted() {
        messagingTemplate.convertAndSendToUser(userLink.username, QUEUE_PULL_STATUS,
                PullStatusUpdate.createStatusUpdate(PullStatus.COMPLETED, userLink).withMessage("Upload (" + userLink.url + ") completed"));
    }

    @Override
    public void onError(Throwable e) {
        log.error(e.getLocalizedMessage(), e);
        messagingTemplate.convertAndSendToUser(userLink.username, QUEUE_PULL_STATUS,
                PullStatusUpdate.createStatusUpdate(PullStatus.FAILED, userLink).withMessage(e.getMessage()));
    }

    @Override
    public void onNext(PullStatusUpdate pullStatusUpdate) {
        messagingTemplate.convertAndSendToUser(userLink.username, QUEUE_PULL_STATUS, pullStatusUpdate);
    }
}
