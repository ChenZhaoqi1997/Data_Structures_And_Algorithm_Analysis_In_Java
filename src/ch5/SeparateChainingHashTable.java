package ch5;

import java.util.LinkedList;
import java.util.List;

/**
 * @author 15101
 */
public class SeparateChainingHashTable<AnyType> {
    private static final int DEFAULT_TABLE_SIZE = 101;
    private List<AnyType>[] theLists;
    private int currentSize;

    public SeparateChainingHashTable(){
        this(DEFAULT_TABLE_SIZE);
    }
    public SeparateChainingHashTable(int size){
        theLists = new LinkedList[nextPrime(size)];
        for (int i = 0; i < theLists.length; i++) {
            theLists[i] = new LinkedList<>();
        }
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
    //获取新的散列值
    private int myHash(AnyType x) {
        int hashVal = x.hashCode();
        hashVal %= theLists.length;
        if (hashVal < 0) {
            hashVal += theLists.length;
        }
        return hashVal;
    }
    private void reHash() {
        List<AnyType>[] oldLists = theLists;
        theLists = new List[nextPrime(2 * theLists.length)];
        for (int j = 0; j < theLists.length; j++) {
            theLists[j] = new LinkedList<>();
        }
        for (int i = 0; i < oldLists.length; i++) {
            for (AnyType item : oldLists[i]) {
                insert(item);
            }
        }
    }

    public boolean contains(AnyType x) {
        List<AnyType> whichList = theLists[myHash(x)];
        return whichList.contains(x);
    }

    public void insert(AnyType x) {
        List<AnyType> whichList = theLists[myHash(x)];
        if (!whichList.contains(x)) {
            whichList.add(x);
            if (++currentSize > theLists.length) {
                reHash();
            }
        }
    }
    public void remove(AnyType x) {
        List<AnyType> whichList = theLists[myHash(x)];
        if (whichList.contains(x)) {
            whichList.remove(x);
            currentSize--;
        }
    }
    public void makeEmpty(){
        for (int i = 0; i < theLists.length; i++) {
            theLists[i].clear();
        }
        currentSize = 0;
    }
}
