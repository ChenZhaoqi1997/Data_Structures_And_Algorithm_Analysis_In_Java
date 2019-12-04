package ch3;

/**
 * @author 15101
 */
public class MyLinkedList<AnyType> implements Iterable<AnyType> {
    private int theSize;
    private int modCount;
    private Node<AnyType> beginMaker;
    private Node<AnyType> endMaker;
    private static class Node<AnyType> {
        public AnyType data;
        public Node<AnyType> prev;
        public Node<AnyType> next;
        public Node(AnyType d, Node<AnyType> p, Node<AnyType> n){
            data = d;
            prev = p;
            next = n;
        }
    }
    public MyLinkedList(){
        doClear();
    }
    public void clear(){
        doClear();
    }
    private void doClear(){
        beginMaker = new Node<AnyType>(null, null, null);
        endMaker = new Node<AnyType>(null, beginMaker, null);
        beginMaker.next = endMaker;
        theSize = 0;
        modCount++;
    }
    public int size(){
        return theSize;
    }
    public boolean isEmpty(){
        return size() == 0;
    }
    public boolean add(AnyType x){
        add(size(), x);
        return true;
    }
    public void add(int idx, AnyType x){
        addBefore(getNode(idx, 0, size()), x);
    }
    private void addBefore(Node<AnyType> p,AnyType x){
        Node<AnyType> newNode = new Node<>(x, p.prev, p);
        newNode.prev.next = newNode;
        p.prev = newNode;
        theSize++;
        modCount++;
    }
    public AnyType get(int idx){
        return getNode(idx).data;
    }
    public AnyType set(int idx, AnyType newVal){
        Node<AnyType> p = getNode(idx);
        AnyType oleVal = p.data;
        p.data = newVal;
        return oleVal;
    }
    public AnyType remove(int idx){
        return remove(getNode(idx));
    }
    private AnyType remove(Node<AnyType> p){
        p.next.prev = p.prev;
        p.prev.next = p.next;
        theSize--;
        modCount++;
        return p.data;
    }
    private Node<AnyType> getNode(int idx){
        return getNode(idx, 0, size() - 1);
    }
    private Node<AnyType> getNode(int idx, int lower, int upper){
        Node<AnyType> p;
        if (idx < lower || idx > upper){
            throw new IndexOutOfBoundsException();
        }
        if (idx < size() /2){
            p = beginMaker.next;
            for (int i = 0; i < idx; i++){
                p = p.next;
            }
        } else {
            p = endMaker;
            for (int i = size(); i > idx; i--) {
                p = p.prev;
            }
        }
        return p;
    }
    @Override
    public java.util.Iterator<AnyType> iterator() {
        return new LinkedListIterator();
    }
    private class LinkedListIterator implements java.util.Iterator<AnyType>{
        private Node<AnyType> current = beginMaker.next;
        private int expectedModCount = modCount;
        private boolean okToRemove = false;
        @Override
        public boolean hasNext() {
            return current != endMaker;
        }
        @Override
        public AnyType next() {
            if (modCount != expectedModCount){
                throw new java.util.ConcurrentModificationException();
            }
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            AnyType nextItem = current.data;
            current = current.next;
            okToRemove = true;
            return nextItem;
        }
        @Override
        public void remove() {
            if (modCount != expectedModCount){
                throw new java.util.ConcurrentModificationException();
            }
            if (!okToRemove) {
                throw new IllegalStateException();
            }
            MyLinkedList.this.remove(current.prev);
            expectedModCount++;
            okToRemove = false;
        }
    }
}