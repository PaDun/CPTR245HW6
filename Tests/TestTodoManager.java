import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

public class TestTodoManager {
    private TodoManager todo;
    private TodoService todoS;

    @Before
    public void setUp(){
       todoS = mock(TodoService.class);
       when(todoS.get("Bob")).thenReturn(new String[] {"Do Homework : June 7",
               "Lunch Date : September 12",
               "Lunch meeting : August 22"});
       when(todoS.get("Jill")).thenReturn(new String[] {"Meeting : 22 august",
               "Birthday : 12 July"});
       when(todoS.getUser("Carl")).thenThrow(new IllegalArgumentException());

       when(todoS.getUser("Jim")).thenReturn("Jim : September 10 : 20");
       when(todoS.getUser("Jack")).thenReturn("Jack : June 28 : 15");
       this.todo = new TodoManager(todoS);

    }

    @Test
    public void filterWithKeyword() {

        assertEquals(new ArrayList<>(Arrays.asList("Lunch Date : September 12", "Lunch meeting : August 22")),
                todo.filter("Bob", "Lunch"));
        verify(todoS).get("Bob");
    }

    @Test
    public void filterWithoutKeyword() {
        assertEquals(new ArrayList<>(Arrays.asList()), todo.filter("Jill", "September"));
        verify(todoS).get("Jill");
    }


    @Test
    public void userDoesNotExists() {
        assertFalse(todo.userExists("Carl"));
        verify(todoS).getUser("Carl");
    }

    @Test
    public void getMultipleUsers() {
        assertEquals( new ArrayList<>(Arrays.asList("Jim : September 10 : 20", "Jack : June 28 : 15")), todo.getUsers(new String[] {"Jim", "Jack", "Carl"}));
        verify(todoS).getUser("Jim");
        verify(todoS).getUser("Jack");
    }
}
