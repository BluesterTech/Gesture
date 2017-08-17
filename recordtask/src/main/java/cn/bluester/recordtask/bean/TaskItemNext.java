package cn.bluester.recordtask.bean;

/**
 * Created by JesseHu on 2016/9/8.
 */
public class TaskItemNext {
    private int id;
    private TaskItemNext yes;
    private TaskItemNext no;

    public TaskItemNext getYes() {
        return yes;
    }

    public void setYes(TaskItemNext yes) {
        this.yes = yes;
    }

    public TaskItemNext getNo() {
        return no;
    }

    public void setNo(TaskItemNext no) {
        this.no = no;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
