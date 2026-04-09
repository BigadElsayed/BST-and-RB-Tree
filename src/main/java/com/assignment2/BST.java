package com.assignment2;

import java.util.ArrayList;
import java.util.List;

public class BST implements TreeInterface {
    BSTNode root;
    private int size;

    public BST() {
        root = null;
        size = 0;
    }

    public boolean insert(int v) {
        if (root == null) {
            root = new BSTNode(v);
            this.size++;
            return true;
        }

        BSTNode current = root;
        BSTNode parent = null;

        while (current != null) {
            parent = current;
            if (v == current.getValue()) {
                return false;
            } else if (v < current.getValue()) {
                current = current.getLeftChild();
            } else {
                current = current.getRightChild();
            }
        }

        current = new BSTNode(v);

        if (v < parent.getValue()) {
            parent.setLeftChild(current);
            current.setParent(parent);
        } else {
            parent.setRightChild(current);
            current.setParent(parent);
        }

        this.size++;
        return true;
    }

    public boolean delete(int v) {
        if (root == null) {
            return false;
        }
        BSTNode current = root;
        while (current != null) {

            if (v == current.getValue()) {
                break;
            }
            if (v < current.getValue()) {
                current = current.getLeftChild();
            } else {
                current = current.getRightChild();
            }

        }
        if (current == null) {
            return false;
        }

        BSTNode actualDeleted = null;
        BSTNode replacement = null;

        // CASE 1
        if (current.getLeftChild() == null && current.getRightChild() == null) {
            actualDeleted = current;
            replacement = null;
        }

        // CASE 2
        else if (current.getLeftChild() == null) {
            actualDeleted = current;
            replacement = current.getRightChild();
        }
        else if (current.getRightChild() == null) {
            actualDeleted = current;
            replacement = current.getLeftChild();
        }
        // CASE 3
        else {
            BSTNode Successor = this.findMin(current.getRightChild());
            current.setValue(Successor.getValue());
            actualDeleted = Successor;
            replacement = Successor.getRightChild();
        }

        this.Transplant(replacement,actualDeleted);

        this.size--;
        return true;

    }

    private void Transplant(BSTNode replacement, BSTNode actualDeleted) {
        if (actualDeleted.getParent() == null) {
            this.root = replacement;
            if(replacement != null)
                replacement.setParent(null);
            return;
        }
        BSTNode parent = actualDeleted.getParent();

        if(actualDeleted == parent.getLeftChild()){
            parent.setLeftChild(replacement);
        }
        else if(actualDeleted == parent.getRightChild()){
            parent.setRightChild(replacement);
        }
        if(replacement != null)
            replacement.setParent(parent);
    }

    private BSTNode findMin(BSTNode node) {
        if (node.getLeftChild() == null) {
            return node;
        }
        return findMin(node.getLeftChild());
    }

    public boolean contains(int v) {
        if (root == null) return false;
        BSTNode node = root;

        while (node != null) {
            if (v == node.getValue()) return true;
            else if (v < node.getValue()) node = node.getLeftChild();
            else node = node.getRightChild();
        }
        return false;
    }

    private void inOrderHelper(BSTNode node, List<Integer> list) {
        if (node == null) return;
        inOrderHelper(node.getLeftChild(), list);
        list.add(node.getValue());
        inOrderHelper(node.getRightChild(), list);
    }

    public List<Integer> inOrder() {
        List<Integer> list = new ArrayList<>();
        inOrderHelper(this.root, list);
        return list;
    }

    private int heightHelper(BSTNode root) {
        if (root == null) return 0;

        return 1 + Math.max(heightHelper(root.getLeftChild()), heightHelper(root.getRightChild()));
    }

    public int height() {
        return heightHelper(root);
    }

    public int size() {
        return this.size;
    }
}
