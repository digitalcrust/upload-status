package gov.sciencebase.files.upload.status;

class PullStatusUpdate {
    PullStatus status;
    private String message;
    private Integer percentComplete;

    static PullStatusUpdate createStatusUpdate(PullStatus status) {
        PullStatusUpdate statusUpdate = new PullStatusUpdate();
        statusUpdate.status = status;
        return statusUpdate;
    }

    PullStatusUpdate withProgress(int downloadedFileSize, int completeFileSize) {
        this.percentComplete = ((downloadedFileSize / completeFileSize) * 100);
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
        if (message != null) {
            return message;
        }

        return status.toString() + ": " + percentComplete + "% Complete";
    }

    public Integer getPercentComplete() {
        return percentComplete;
    }
}
