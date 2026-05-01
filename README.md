# ToDo List Application

This is a Java Swing ToDo List app. It lets you create tasks, view task details, mark tasks as complete, remove tasks, and see tasks on a calendar.

## Features

- Add new tasks with a title, description, and due date.
- View all saved tasks from the home screen.
- Open a task to see its full description and due date.
- Mark a task as completed or not completed.
- Remove tasks from the list.
- Save tasks between sessions with a SQLite database.
- Show tasks on a calendar based on their due dates.

## Calendar Features

- Month view shows a full calendar grid.
- Week view shows the selected week.
- Day view shows the tasks for one selected day.
- The Today button jumps back to the current date.
- The arrow buttons move based on the current view.
- In day view, the arrows move one day at a time.
- In week view, the arrows move one week at a time.
- In month view, the arrows move one month at a time.
- Calendar day cells show how many tasks are due on that date.

## Data Storage

Tasks are stored in a SQLite database at:

```text
ToDoList/data/tasks.db
```

The app creates the `Tasks` table if it does not already exist.