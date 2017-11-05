import java.util.ArrayList;

public class TodoManager {
    private TodoService todoService;

    public TodoManager(TodoService todoS) {
        this.todoService = todoS;
    }

    public ArrayList<String> filter(String user, String keyword) {
        String[] todos = this.todoService.get(user);
        ArrayList<String> output = new ArrayList<>();
        String[] todo_array;
        for (String todo: todos) {
           todo_array = todo.split(" : ");
           todo_array = todo_array[0].split("\\W+");

           for (String word: todo_array) {
               if (word.equals(keyword)) {
                   output.add(todo);
                   break;
               }
           }
        }

        return output;
    }
}
