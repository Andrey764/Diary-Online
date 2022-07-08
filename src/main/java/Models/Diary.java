package Models;

public class Diary {
    private String title;
    private String content;
    private boolean isArchive = false;

    public String getCreator() {
        return Creator;
    }

    public void setCreator(String creator) {
        Creator = creator;
    }

    private String Creator;

    public boolean getArchive() {
        return isArchive;
    }

    public void setArchive(boolean archive) {
        isArchive = archive;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Diary(String title, String content){
        setTitle(title);
        setContent(content);
        setCreator("");
    }

    public void Copy(Diary diary){
        this.title = diary.getTitle();
        this.content = diary.getContent();
        this.isArchive = diary.getArchive();
    }
}
