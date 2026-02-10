public class ToDo{
    private String title;
    private String description;
    private String dueDate;
    private boolean isCompleted;
    
    public ToDo(String title, String description, String dueDate){
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.isCompleted = false;
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
    public boolean isCompleted(){
        return isCompleted;
    }

    @Override
    public String toString(){
        return "Title: " + title + ", \nDescription: " + description + ", \nDue Date: " + dueDate + ", \nCompleted: " + isCompleted;
    }
}