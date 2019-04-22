package coleo.com.abjo.data_class;

public class Message {

    private String text;
    private String title;

    public Message(String text, String title) {
        this.text = text;
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }
}
