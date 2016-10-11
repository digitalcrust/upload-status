package gov.sciencebase.files.upload.status;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;

class PullStatusObserver implements Observer<PullStatusUpdate> {

    private SimpMessagingTemplate messagingTemplate;
    private UserLink userLink;

    PullStatusObserver(SimpMessagingTemplate messagingTemplate, UserLink userLink) {
        this.messagingTemplate = messagingTemplate;
        this.userLink = userLink;
    }

    @Override
    public void onCompleted() {
        messagingTemplate.convertAndSendToUser(userLink.username, "/queue/pull-status",
                PullStatusUpdate.createStatusUpdate(PullStatus.COMPLETED, userLink).withMessage("Upload (" + userLink.url + ") completed"));
    }

    @Override
    public void onError(Throwable e) {
        messagingTemplate.convertAndSendToUser(userLink.username, "/queue/pull-status",
                PullStatusUpdate.createStatusUpdate(PullStatus.FAILED, userLink).withMessage(e.getMessage()));
    }

    @Override
    public void onNext(PullStatusUpdate pullStatusUpdate) {
        messagingTemplate.convertAndSendToUser(userLink.username, "/queue/pull-status", pullStatusUpdate);
    }
}
