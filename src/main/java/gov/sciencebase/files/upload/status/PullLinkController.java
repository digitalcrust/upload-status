package gov.sciencebase.files.upload.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
public class PullLinkController {

    private final PullLinkService pullLinkService;

    @Autowired
    public PullLinkController(PullLinkService pullLinkService) {
        this.pullLinkService = pullLinkService;
    }

    @MessageMapping("/pull-link")
    @SendToUser("/queue/pull-status")
    public PullStatusUpdate startPullingLink(PullLinkMessage pullLinkMessageMessage) throws Exception {
        UserLink userLink = new UserLink(fetchUsername(), pullLinkMessageMessage.getLink(), pullLinkMessageMessage.getFileName());
        pullLinkService.pull(userLink);
        return PullStatusUpdate.createStatusUpdate(PullStatus.PENDING, userLink).withMessage("Starting " + userLink.url + " ...");
    }

    private String fetchUsername() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
