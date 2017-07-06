package gov.sciencebase.files.upload.status;

class PullStatusUpdate {
    PullStatus status;
    private String message;
    private String url;
    private long percentComplete;

    static PullStatusUpdate createStatusUpdate(PullStatus status, UserLink userLink) {
        PullStatusUpdate statusUpdate = new PullStatusUpdate();
        statusUpdate.status = status;
        statusUpdate.url = userLink.url;
        return statusUpdate;
    }

    PullStatusUpdate withProgress(long downloadedFileSize, long completeFileSize) {
        this.percentComplete = (downloadedFileSize * 100L) / completeFileSize;
        return this;
    }

    PullStatusUpdate withMessage(String message) {
        this.message = message;
        return this;
    }

    public PullStatus getStatus() {
        return status;
    }

    public String getMessage() {
        System.out.println("PullStatusUpdated.completed");
        if (message != null) {
            return message;
        }

        if (status == PullStatus.COMPLETED) {
            return "Upload (" + url + ") completed";
        }

        if (status == PullStatus.FAILED) {
            return "Upload(" + url + ") failed";
        }

        return status.toString() + ": (" + url + ") " + percentComplete + "% Complete";
    }

    public long getPercentComplete() {
        return percentComplete;
    }
}
