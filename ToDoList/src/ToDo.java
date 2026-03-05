public class ToDo{
    private int id;
    private String title;
    private String description;
    private String dueDate;
    private boolean isCompleted;
    
    public ToDo(int id, String title, String description, String dueDate, boolean isCompleted){
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.isCompleted = isCompleted;
    }
    
    public ToDo(String title, String description, String dueDate, boolean isCompleted){
        this(0, title, description, dueDate, isCompleted);
    }
    
    public int getId(){
        return id;
    }
    public String getTitle(){
        return title;
    }
    public String getDescription(){
        return description;
    }
    public String getDueDate(){
        return dueDate;
    }
    public void setId(int id){
        this.id = id;
    }
    public boolean isCompleted(){
        return isCompleted;
    }
    public void setCompleted(boolean completed){
        this.isCompleted = completed;
    }

    @Override
    public String toString(){
        return "Title: " + title + ", \nDescription: " + description + ", \nDue Date: " + dueDate + ", \nCompleted: " + isCompleted;
    }
}