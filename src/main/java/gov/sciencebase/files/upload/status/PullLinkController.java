package gov.sciencebase.files.upload.status;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

@Controller
@RestController
public class PullLinkController {
    @Value("${s3.bucketname}")
    private String bucketName;

    @Value("${s3.bucketpath}")
    private String bucketPath;

    private final PullLinkService pullLinkService;

    @Autowired
    public PullLinkController(PullLinkService pullLinkService) {
        this.pullLinkService = pullLinkService;
    }
    @RequestMapping(value = "/objectmetadata/{key}", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<ObjectMetadata> retrieveMetadata(@PathVariable final String key) {
        final AmazonS3 s3Client = new AmazonS3Client();
        ResponseEntity<ObjectMetadata> result;
        try {
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, bucketPath + "/" + fetchUsername() + "/" + key);
            S3Object s3object = s3Client.getObject(getObjectRequest);
            result = ResponseEntity.ok(s3object.getObjectMetadata());
        } catch (AmazonS3Exception e) {
            result = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return result;
    }

    @MessageMapping("/pull-link")
    @SendToUser("/queue/pull-status")
    public PullStatusUpdate startPullingLink(PullLinkMessage pullLinkMessageMessage) throws Exception {
        UserLink userLink = new UserLink(fetchUsername(), pullLinkMessageMessage.getLink(), pullLinkMessageMessage.getFileName());
        pullLinkService.pull(userLink);
        return PullStatusUpdate.createStatusUpdate(PullStatus.PENDING, userLink).withMessage("Starting " + userLink.url + " ...");
    }

    private String fetchUsername() {
        return("derek");

        //return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
