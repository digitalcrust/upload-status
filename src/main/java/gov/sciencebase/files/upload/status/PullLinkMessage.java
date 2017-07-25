package gov.sciencebase.files.upload.status;

public class PullLinkMessage {
    private String link;
    private String fileName;

    public PullLinkMessage() {
    }

    public PullLinkMessage(String link, String fileName) {
        this.link = link;
        this.fileName = fileName;
    }

    public String getLink() {
        return link;
    }

    public String getFileName() {
        return fileName;
    }
}