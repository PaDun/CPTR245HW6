
Patrick Dunphy & Charles Lambert
Homework 6

We decided to make a to-do list program, in which to incorporate mocks. 
We made the class which would hold the users and their information be the mock, 
and added functionalities that involve that information. The entirety of 
our TodoService, the name of the class to be mocked, is as follows:

```java
public class TodoService {

    public String[] get(String user) {
        return new String[] {"hello"};
    }
    public String getUser(String user) { return "Stuff"; }

}
```
The first functionality we decided it should have was to filter the items
on the to-do list based on a keyword provided to it, and return the items 
that had that keyword in it. Before we wrote the implementation for it, though, 
we (spent a good while figuring out how to do things with Mockito, then) 
added the following mock information:

```java
       todoS = mock(TodoService.class); //todoS is an item of type TodoService
       when(todoS.get("Bob")).thenReturn(new String[] {"Do Homework : June 7",
               "Lunch Date : September 12",
               "Lunch meeting : August 22"});
       this.todo = new TodoManager(todoS);
```

Then the test, 

```java
    @Test
    public void filterWithKeyword() {

        assertEquals(new ArrayList<>(Arrays.asList("Lunch Date : September 12", "Lunch meeting : August 22")),
                todo.filter("Bob", "Lunch"));
        verify(todoS).get("Bob");
    }
```
    And finally, the implementation.
    
```java
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
```

And it works. We then checked that no matching keyword would return nothing.
First we added a new user mock representation, like so:

```java
       when(todoS.get("Jill")).thenReturn(new String[] {"Meeting : 22 august",
               "Birthday : 12 July"});
```

Then, a new test.

```java
    @Test
    public void filterWithoutKeyword() {
        assertEquals(new ArrayList<>(Arrays.asList()), todo.filter("Jill", "September"));
        verify(todoS).get("Jill");
    }
```

It worked as we expected it to, good. Suppose a user doesn't exist? We'd like 
it to throw an IllegalArgumentException(), so let's tell the mock as much.

```java
       when(todoS.getUser("Carl")).thenThrow(new IllegalArgumentException());
```

We didn't find how to check for if a particular exception was thrown, so we 
tested like so:

```java
    @Test
    public void userDoesNotExists() {
        assertFalse(todo.userExists("Carl"));
        verify(todoS).getUser("Carl");
    }
```

And implemented like so:

```java
    public Boolean userExists(String user) {
        try {
            this.todoService.getUser(user);
            return true;
        }
        catch (IllegalArgumentException e) {
            return false;
        }
    }
```

Cool. The last functionality we wanted to add was to get the summary of 
multiple users' to-do lists. First, we added a couple more users to work with.

```java
       when(todoS.getUser("Jim")).thenReturn("Jim : September 10 : 20");
       when(todoS.getUser("Jack")).thenReturn("Jack : June 28 : 15");
```
(I'm not sure what the date represents, but that's not essential for our purposes.)
Then the test:

```java
    @Test
    public void getMultipleUsers() {
        assertEquals( new ArrayList<>(Arrays.asList("Jim : September 10 : 20", 
        "Jack : June 28 : 15")), 
        todo.getUsers(new String[] {"Jim", "Jack", "Carl"}));
        verify(todoS).getUser("Jim");
        verify(todoS).getUser("Jack");
    }
```

And finally the implementation.

```java
    public ArrayList<String> getUsers(String[] users) {
        ArrayList<String> output = new ArrayList<>();

        for (String user: users) {
            try {
                output.add(todoService.getUser(user));
            }
            catch(IllegalArgumentException e) {
            }
        }

        return output;
    }
```

The address where all this is is: https://github.com/PaDun/CPTR245HW6
Look in src and Tests to find the relevant files.
