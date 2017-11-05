import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestTodoManager {
    private TodoManager todo;

    @Before
    public void setUp(){
       TodoService todoS = mock(TodoService.class);
       when(todoS.get("Bob")).thenReturn(new String[] {"Do Homework : June 7",
               "Lunch Date : September 12",
               "Lunch meeting : August 22"});
       when(todoS.get("Jill")).thenReturn(new String[] {"Meeting : 22 august",
               "Birthday : 12 July"});

       this.todo = new TodoManager(todoS);

    }

    @Test
    public void filterWithKeyword() {
        assertEquals(new ArrayList<>(Arrays.asList("Lunch Date : September 12", "Lunch meeting : August 22")),
                todo.filter("Bob", "Lunch"));
    }
}
