package cn.bluester.recordtask.bean;

/**
 * Created by JesseHu on 2016/9/8.
 */
public class TaskItem {
    private int id;
    private int type;
    private String text;
    private TaskItemNext link;
    private Attachment attachment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public TaskItemNext getLink() {
        return link;
    }

    public void setLink(TaskItemNext link) {
        this.link = link;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }
}
