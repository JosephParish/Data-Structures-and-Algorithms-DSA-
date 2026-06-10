import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class STACKTest {

    private STACK<Integer> stack;

    @BeforeEach
    void setUp() {
        stack = new STACK<>();
    }

    @Test
    void pushShouldAddElementToStack() {
        stack.push(10);
        assertEquals(10, stack.peek());
    }

    @Test
    void pushMultipleElementsShouldFollowLIFO() {
        stack.push(1);
        stack.push(2);
        stack.push(3);

        assertEquals(3, stack.pop());
        assertEquals(2, stack.pop());
        assertEquals(1, stack.pop());
    }

    @Test
    void popOnEmptyStackReturnsNull() {
        assertNull(stack.pop());
    }

    @Test
    void peekDoesNotRemoveElement() {
        stack.push(42);
        assertEquals(42, stack.peek());
        assertEquals(42, stack.pop());
    }
}
