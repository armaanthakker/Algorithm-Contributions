import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

/**
 * Resizable-array implementation of the MyList interface.
 * @author Brian S. Borowski
 * @version 1.0 September 27, 2022
 */
public class MyArrayList<E> implements MyList<E>, MyStack<E> {
    /**
     * Default initial capacity.
     */
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * The size of the ArrayList (the number of elements it contains).
     */
    private int size;

    /**
     * The array buffer into which the elements of the ArrayList are stored.
     * The capacity of the ArrayList is the length of this array buffer.
     */
    Object[] elementData; // non-private to simplify nested class access

    /**
     * Constructs an empty list with the specified initial capacity.
     * @param  initialCapacity  the initial capacity of the list
     * @throws IllegalArgumentException if the specified initial capacity
     *         is negative
     */
    public MyArrayList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " +
                    initialCapacity);
        }
        this.elementData = new Object[initialCapacity];
    }

    /**
     * Constructs an empty list with an initial capacity of ten.
     */
    public MyArrayList() {
        this.elementData = new Object[DEFAULT_CAPACITY];
    }

    /**
     * Returns the number of elements in this list.
     * @return the number of elements in this list
     */
    public int size() {
        return size;
    }

    /**
     * Returns true if this list contains no elements.
     * @return true if this list contains no elements
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Appends the specified element to the end of this list.
     * @param element  element to be appended to this list
     * @return true
     */
    public boolean add(E element) {
        if (size + 1 > elementData.length) {
            Object[] newData = new Object[size * 2 + 1];
            for (int i = 0; i < size; i++) {
                newData[i] = elementData[i];
            }
            elementData = newData;
        }
        elementData[size++] = element;
        return true;
    }

    /**
     * TODO
     * Inserts the specified element at the specified position in this list.
     * Shifts the element currently at that position (if any) and any subsequent
     * elements to the right (adds one to their indices).
     * @param index index at which the specified element is to be inserted
     * @param element element to be inserted
     * @throws IndexOutOfBoundsException if the index is out of range
     * (index < 0 || index > size())
     * The exception message must be:
     * "Index: " + index + ", list size: " + size
     */
    public void add(int index, E element){
        if (index < 0 || index > size()){
            throw new IndexOutOfBoundsException(
                    "Index: " + index + ", list size: " + size);
        }
        // Adding at the end of the arraylist
        if (size() == index){
            add(element);
            return;
        }

        // Creating a temporary varibale which represents the next element in the list
        E next = null;
        for (int i = index; i<=size; i++){

            // If i is the first element the index that needs to be replaced, replace the current element with 'element'
            if (i==index){
                next = set(i, element);
                continue;
            }
            // If i is equal to the size (one bigger than the greatest index),
            // Then add the 'next' element to the end and break the for loop
            if (i == size()) {
                add(next);
                break;
            }
            next = set(i, next);
        }


    }

    /**
     * TODO
     * Removes the element at the specified position in this list.
     * @param index the index of the element to be removed
     * @return the element that was removed from the list
     * @throws IndexOutOfBoundsException if the index is out of range
     * (index < 0 || index >= size())
     * The exception message must be:
     * "Index: " + index + ", list size: " + size
     */
    public E remove(int index){
        if (index < 0 || index > size()){
            throw new IndexOutOfBoundsException(
                    "Index: " + index + ", list size: " + size);
        }

        // Creating the temporary variable 'next' to store the
        // value that is to be moved back
        E next;

        // ret holds the element which was removed by the function
        E ret = set(index, null);
        for (int i = index; i<size; i++){

            // If at the end of the list, the next last element should be replaced with null
            if (i+1 == size){
                next = null;
            }
            // If traversing from the middle of the list then next element should be at index i+1
            else {
                next = get(i + 1);
            }
            // Replacing the current element with the next element (Moving the next element down)
            set(i, next);
        }
        // Decreasing the size of the list by 1
        size--;
        return ret;
    }

    /**
     * TODO
     * Returns the index of the first occurrence of the specified element in
     * this list, or -1 if this list does not contain the element. More
     * formally, returns the lowest index i such that Objects.equals(o, get(i)),
     * or -1 if there is no such index.
     * @param element element to search for
     * @return the index of the first occurrence of the specified element in
    Lists - 2
     * this list, or -1 if this list does not contain the element
     */
    public int indexOf(E element){
        for (int i = 0; i<size(); i++){
            if (Objects.equals(element, get(i))){
                return i;
            }
        }
        return -1;
    }

    /**
     * TODO
     * Returns an array of indexes of each occurrence of the specified element
     * in this list, in ascending order. If the specified element is not found,
     * a non-null empty array (not null) is returned.
     * @param element element to search for
     * @return an array of each occurrence of the specified element in this
     * list
     */
    public int[] indexesOf(E element){
        int count = 0;
        int[] e = new int[count];
        for (int i = 0; i<size(); i++){
            // If the current element equals 'element', then create an array that is one bigger than e
            if (Objects.equals(element, get(i))){
                count++;
                int[] tempArray = new int[count];
                // Copy e to tempArray
                for(int j = 0; j<count-1; j++){
                    tempArray[j] = e[j];
                }
                // Add the current index to the end of tempArray
                tempArray[count-1] = i;

                // Copy temp array data to e
                e = new int[count];
                for(int j = 0; j < count; j++){
                    e[j] = tempArray[j];
                }
            }
        }

        return e;
    }

    /**
     * TODO
     * Reverses the data in the list.
     * For MyArrayList, the data inside the underlying array is moved. For
     * MyLinkedList, the tail must become the head, and all the pointers are
     * reversed. Both implementations must run in Theta(n).
     */
    public void reverse(){
        E temp;
        for(int i = 0; i<size()/2; i++){
            int i2 = size()-1-i;

            temp = get(i2);

            set(i2, get(i));
            set(i, temp);
        }
    }

    /**
     * Returns the element at the specified position in this list.
     * @param index  index of the element to return
     * @return       the element at the specified position in this list
     * @throws       IndexOutOfBoundsException - if the index is out of range
     *               (index < 0 || index >= size())
     */
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Index: " + index + ", list size: " + size);
        }
        return (E)elementData[index];
    }

    /**
     * Replaces the element at the specified position in this list with the
     * specified element.
     * @param index    index of the element to return
     * @param element  element to be stored at the specified position
     * @return  the element at the specified position in this list
     * @throws  IndexOutOfBoundsException - if the index is out of range
     *          (index < 0 || index >= size())
     */
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Index: " + index + ", list size: " + size);
        }
        E oldValue = (E)elementData[index];
        elementData[index] = element;
        return oldValue;
    }

    /**
     * Removes all of the elements from this list.
     */
    public void clear() {
        // clear to let GC do its work
        for (int i = 0; i < size; i++) {
            elementData[i] = null;
        }
        size = 0;
    }

    @Override
    public String toString(){
        String ret = "";
        for (int i =0; i<size; i++){
            if (i+1==size){
                ret = ret + (String)elementData[i] + "";
                break;
            }
            ret = ret + (String)elementData[i] + ", ";
        }
        return "[" + ret + "]";
    }



    /**
     * Pushes an item onto the top of this stack. This has exactly the same
     * effect as: add(item)
     * @param item the item to be pushed onto this stack
     */
    public void push(E item){
        add(item);
    }

    /**
     * Removes the object at the top of this stack and returns that object.
     * @return the object at the top of this stack (the last item in the
     * MyArrayList).
     * @throws StackException if the stack is empty. The exception's message
    Stacks - 2
     * must be "Attempt to pop from empty stack."
     */
    public E pop() throws StackException{
        if (isEmpty()){
            throw new StackException("Attempt to pop from empty stack.");}
        return remove(size-1);
    }

    /**
     * Looks at the object at the top of this stack without removing it from the
     * stack.
     * @return the object at the top of this stack (the last item in the
     * MyArrayList).
     * @throws StackException if the stack is empty. The exception's message
     * must be "Attempt to peek at empty stack."
     */
    public E peek() throws StackException{
        if (isEmpty()){
            throw new StackException("Attempt to pop from empty stack.");}
        return get(size()-1);
    }

    /**
     * Returns an iterator over the elements in this list (in proper
     * sequence).
     *
     * The returned list iterator is fail-fast -- modification of the elements
     * is not permitted during iteration.
     */
    public Iterator<E> iterator() {
        return new ListItr();
    }

    private class ListItr implements Iterator<E> {
        private int current;

        ListItr() {
            current = 0;
        }

        @Override
        public boolean hasNext() {
            return current < size;
        }

        @Override
        public E next() {
            return (E)elementData[current++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }



}


