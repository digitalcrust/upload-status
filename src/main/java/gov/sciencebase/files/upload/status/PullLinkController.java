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

    @MessageMapping("/pullLink")
    @SendToUser("/queue/pullStatus")
    public PullStatusUpdate startPullingLink(PullLinkMessage pullLinkMessageMessage) throws Exception {
        linkPullerService.pull(new UserLink(fetchUsername(), pullLinkMessageMessage.getLink()));
        return PullStatusUpdate.createStatusUpdate(PullStatus.PENDING).withMessage("Starting...");
    }

    private String fetchUsername() {
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }
}
