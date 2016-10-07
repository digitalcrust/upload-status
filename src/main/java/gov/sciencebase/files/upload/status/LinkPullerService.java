package gov.sciencebase.files.upload.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
class LinkPullerService {

    private final SimpMessagingTemplate messagingTemplate;

    private ExecutorService executorService = new ThreadPoolExecutor(4, 4, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>());

    @Autowired
    public LinkPullerService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    void pull(UserLink userLink) {
        new LinkPuller(userLink, executorService).withStatusUpdatesEvery(5, TimeUnit.SECONDS)
                                                 .subscribe(new PullStatusObserver(messagingTemplate, userLink));
    }
}
