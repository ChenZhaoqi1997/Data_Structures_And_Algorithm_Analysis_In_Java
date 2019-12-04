package ch12;

import javax.swing.*;

public class RedBlackTree<AnyType extends Comparable<? super AnyType>> {
    private RedBlackNode<AnyType> header;
    private RedBlackNode<AnyType> nullNode;
    private static final int BLACK = 1;
    private static final int RED = 0;
    private static class RedBlackNode<AnyType> {
        AnyType element;
        RedBlackNode<AnyType> left;
        RedBlackNode<AnyType> right;
        int color;
        RedBlackNode(AnyType theElement) {
            this(theElement, null, null);
        }
        RedBlackNode(AnyType theElement, RedBlackNode<AnyType> lt, RedBlackNode<AnyType> rt) {
            element = theElement;
            left = lt;
            right = rt;
            color = RedBlackTree.BLACK;
        }
    }
    public RedBlackTree() {
        nullNode = new RedBlackNode<>(null);
        nullNode.left = nullNode.right = nullNode;
        header = new RedBlackNode<>(null);
        header.left = header.right = nullNode;
    }

    private RedBlackNode<AnyType> rotate(AnyType item, RedBlackNode<AnyType> parent) {
        if (compare(item, parent) < 0) {
            return parent.left = compare(item, parent.left) < 0 ?
                    rotateWithLeftChild(parent.left) :
                    rotateWithRightChild(parent.left);
        } else {
            return parent.left = compare(item, parent.left) < 0 ?
                    rotateWithLeftChild(parent.left) :
                    rotateWithRightChild(parent.left);
        }
    }
    private final int compare(AnyType item, RedBlackNode<AnyType> t) {
        if (t == header) {
            return 1;
        } else {
            return item.compareTo(t.element);
        }
    }
    private RedBlackNode<AnyType> rotateWithLeftChild( RedBlackNode<AnyType> k2 ) {
        RedBlackNode<AnyType> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        return k1;
    }
    private RedBlackNode<AnyType> rotateWithRightChild( RedBlackNode<AnyType> k1 ) {
        RedBlackNode<AnyType> k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        return k2;
    }

    private RedBlackNode<AnyType> current;
    private RedBlackNode<AnyType> parent;
    private RedBlackNode<AnyType> grand;
    private RedBlackNode<AnyType> great;
    private void handleReorient(AnyType item) {
        current.color = RED;
        current.left.color = BLACK;
        current.right.color = BLACK;
        if (parent.color == RED) {
            grand.color = RED;
            if ((compare(item, parent) < 0) != (compare(item, grand) < 0)) {
                parent = rotate(item, grand);
            }
            current = rotate(item, great);
            current.color = BLACK;
        }
        header.right.color = BLACK;
    }
    public void insert( AnyType item ) {
        current = parent = grand = header;
        nullNode.element = item;
        while(compare(item, current) != 0) {
            great = grand;
            grand = parent;
            parent = current;
            current = compare( item, current ) < 0 ? current.left : current.right;
            if( current.left.color == RED && current.right.color == RED ) {
                handleReorient( item );
            }
        }
        if( current != nullNode ) {
            return;
        }
        current = new RedBlackNode<>( item, nullNode, nullNode );
        if( compare( item, parent ) < 0 ) {
            parent.left = current;
        } else {
            parent.right = current;
        }
        handleReorient( item );
    }
}
