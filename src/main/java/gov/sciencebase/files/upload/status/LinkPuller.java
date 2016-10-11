package gov.sciencebase.files.upload.status;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

class LinkPuller {
    private UserLink userLink;
    private ExecutorService executorService;

    private final Logger log = LoggerFactory.getLogger(LinkPuller.class);

    LinkPuller(UserLink userLink, ExecutorService executorService) {
        this.userLink = userLink;
        this.executorService = executorService;
    }

    Observable<PullStatusUpdate> withStatusUpdatesEvery(long period, TimeUnit timeUnit) {
        return uploadWithStatusUpdates(userLink).sample(period, timeUnit);
    }

    private Observable<PullStatusUpdate> uploadWithStatusUpdates(UserLink userLink) {
        return Observable.create(subscriber -> executorService.execute(() -> {
            try {
                URL url  = new URL(userLink.url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                int completeFileSize = httpURLConnection.getContentLength();
                String fileName = FilenameUtils.getName(userLink.url);

                BufferedInputStream bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                FileOutputStream fileOutputStream = new FileOutputStream(new File("/tmp", fileName));
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream, 1024);

                byte[] data = new byte[1024];

                int downloadedFileSize = 0;
                int x = 0;
                while ((x = bufferedInputStream.read(data, 0, 1024)) >= 0) {
                    downloadedFileSize += x;

                    bufferedOutputStream.write(data, 0, x);

                    subscriber.onNext(PullStatusUpdate.createStatusUpdate(PullStatus.STARTED, userLink)
                                                      .withProgress(downloadedFileSize, completeFileSize));
                }
                bufferedOutputStream.close();
                bufferedInputStream.close();

                subscriber.onCompleted();
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
                subscriber.onError(e);
            }
        }));
    }
}
