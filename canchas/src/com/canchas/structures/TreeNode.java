package com.canchas.structures;

// Nodo BST: key (orden), data (dato), left/right (hijos)
public class TreeNode<T> {

    public int key;
    public T data;
    public TreeNode<T> left;
    public TreeNode<T> right;

    public TreeNode(int key, T data) {
        this.key   = key;
        this.data  = data;
        this.left  = null;
        this.right = null;
    }
}
