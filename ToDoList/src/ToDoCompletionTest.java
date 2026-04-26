public class ToDoCompletionTest {
    public static void main(String[] args) {
        ToDo incompleteTask = new ToDo("Study", "Finish Java test", "2026-04-26", false);
        assertCompleted(incompleteTask, false, "new incomplete task should not be completed");

        incompleteTask.setCompleted(true);
        assertCompleted(incompleteTask, true, "setCompleted(true) should mark task completed");

        ToDo completedTask = new ToDo("Submit", "Turn in assignment", "2026-04-27", true);
        assertCompleted(completedTask, true, "new completed task should be completed");

        completedTask.setCompleted(false);
        assertCompleted(completedTask, false, "setCompleted(false) should mark task incomplete");

        System.out.println("ToDoCompletionTest passed");
    }

    private static void assertCompleted(ToDo task, boolean expected, String message) {
        if (task.isCompleted() != expected) {
            throw new AssertionError(message);
        }
    }
}
