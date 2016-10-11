package gov.sciencebase.files.upload.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

@Controller
public class PullLinkController {

    private final LinkPullerService linkPullerService;

    @Autowired
    public PullLinkController(LinkPullerService linkPullerService) {
        this.linkPullerService = linkPullerService;
    }

    @MessageMapping("/pull-link")
    @SendToUser("/queue/pull-status")
    public PullStatusUpdate startPullingLink(PullLinkMessage pullLinkMessageMessage) throws Exception {
        UserLink userLink = new UserLink(fetchUsername(), pullLinkMessageMessage.getLink());
        linkPullerService.pull(userLink);
        return PullStatusUpdate.createStatusUpdate(PullStatus.PENDING, userLink).withMessage("Starting " + userLink.url + " ...");
    }

    private String fetchUsername() {
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }
}
