package ch5;
/**
 * @author 15101
 */
public class QuadraticProbingHashTable<AnyType> {
    private static final int DEFAULT_TABLE_SIZE = 11;
    private HashEntry<AnyType>[] array;
    private int currentSize;

    private static class HashEntry<AnyType> {
        public AnyType element;
        public boolean isActive;
        public HashEntry(AnyType e) {
            this(e, true);
        }
        public HashEntry(AnyType e, boolean i) {
            element = e;
            isActive = i;
        }
    }

    public QuadraticProbingHashTable(){
        this(DEFAULT_TABLE_SIZE);
    }
    public QuadraticProbingHashTable(int size) {
        allocateArray(size);
        makeEmpty();
    }
    public void makeEmpty(){
        currentSize = 0;
        for (int i = 0; i < array.length; i++) {
            array[i] = null;
        }
    }
    private void allocateArray(int arraySize) {
        array = new HashEntry[nextPrime(arraySize)];
    }
    private int myHash(AnyType x) {
        int hashVal = x.hashCode();
        hashVal %= array.length;
        if (hashVal < 0) {
            hashVal += array.length;
        }
        return hashVal;
    }
    //寻找下一个质数
    private static int nextPrime(int n) {
        if (n % 2 == 0) {
            n++;
        }
        for ( ; !isPrime(n);n += 2) {
            ;
        }
        return n;
    }
    //判断是否是质数
    private static boolean isPrime(int n) {
        if (n == 2 || n == 3){
            return true;
        }
        if (n == 1 || n % 2 == 0){
            return false;
        }
        for (int i = 3; i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
    public boolean contains(AnyType x) {
        int currentPos = findPos(x);
        return isActive(currentPos);
    }
    private int findPos(AnyType x) {
        int offset = 1;
        int currentPos = myHash(x);
        while (array[currentPos] != null && !array[currentPos].element.equals(x)) {
            currentPos += offset;
            offset += 2;
            if (currentPos >= array.length) {
                currentPos -= array.length;
            }
        }
        return currentPos;
    }
    private boolean isActive(int currentPos) {
        return array[currentPos] != null && array[currentPos].isActive;
    }

    public void insert (AnyType x) {
        int currentPos = findPos(x);
        if (isActive(currentPos)) {
            return;
        }
        array[currentPos] = new HashEntry<>(x, true);
        if (currentPos > array.length / 2){
            reHash();
        }
    }
    public void remove (AnyType x) {
        int currentPos = findPos(x);
        if (isActive(currentPos)) {
            array[currentPos].isActive = false;
        }
    }
    private void reHash() {
        HashEntry<AnyType>[] oldArray = array;
        allocateArray(nextPrime(2 * oldArray.length));
        currentSize = 0;
        for (int i = 0; i < oldArray.length; i++) {
            if (oldArray[i] != null && oldArray[i].isActive) {
                insert(oldArray[i].element);
            }
        }
    }
}
