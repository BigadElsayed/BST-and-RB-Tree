package com.assignment2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class BST implements TreeInterface {
    BSTNode root;
    private int size;
    private static final boolean VALIDATE = false;

    private static final Logger logger = LoggerFactory.getLogger(BST.class);

    public BST() {
        root = null;
        size = 0;
    }

    public boolean insert(int v) {
        logger.debug("Inserting into BST value : {} " , v);
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
                logger.debug("Value {} already exists in tree, ignoring", v);
                return false;
            } else if (v < current.getValue()) {
                logger.trace("Going left from node {}", current.getValue());
                current = current.getLeftChild();
            } else {
                logger.trace("Going right from node {}", current.getValue());
                current = current.getRightChild();
            }
        }

        logger.debug("Inserting {} as child of {} ", v , parent.getValue());
        current = new BSTNode(v);

        if (v < parent.getValue()) {
            parent.setLeftChild(current);
            current.setParent(parent);
        } else {
            parent.setRightChild(current);
            current.setParent(parent);
        }

        this.size++;
        if (VALIDATE && !Validator.validateBst(this)) {
            throw new IllegalStateException("BST invariant violated after insert!");
        }

        logger.debug("Insert completed successfully for value: {}", v);
        return true;
    }

    public boolean delete(int v) {
        logger.debug("Deleting node {} ", v);
        if (root == null) {
            logger.trace("Root is empty , returning false");
            return false;
        }
        BSTNode current = root;
        while (current != null) {

            if (v == current.getValue()) {
                logger.debug("Value {} already exists in tree", v);
                break;
            }
            if (v < current.getValue()) {
                logger.trace("Going left from node {}", current.getValue());
                current = current.getLeftChild();
            } else {
                logger.trace("Going right from node {}", current.getValue());
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
            logger.debug("Node {} has no children", current.getValue());
            actualDeleted = current;
            replacement = null;
        }

        // CASE 2
        else if (current.getLeftChild() == null) {
            logger.debug("Node {} has Only right child", current.getValue());
            actualDeleted = current;
            replacement = current.getRightChild();
        }
        else if (current.getRightChild() == null) {
            logger.debug("Node {} has Only left child", current.getValue());
            actualDeleted = current;
            replacement = current.getLeftChild();
        }
        // CASE 3
        else {
            logger.debug("Node {} has 2 children", current.getValue());
            BSTNode Successor = this.findMin(current.getRightChild());
            logger.debug("Successor is  {}", Successor.getValue());
            current.setValue(Successor.getValue());
            actualDeleted = Successor;
            replacement = Successor.getRightChild();
        }

        this.Transplant(replacement,actualDeleted);

        this.size--;
        if (VALIDATE && !Validator.validateBst(this)) {
            throw new IllegalStateException("BST invariant violated after delete!");
        }

        logger.debug("Delete completed successfully for value: {}", v);
        return true;

    }

    private void Transplant(BSTNode replacement, BSTNode actualDeleted) {
        if (actualDeleted.getParent() == null) {
            this.root = replacement;
            if(replacement != null) {
                logger.debug("Transplanting replacement {} to root", replacement.getValue());
                replacement.setParent(null);
            }
            return;
        }
        BSTNode parent = actualDeleted.getParent();

        if(actualDeleted == parent.getLeftChild()){
            parent.setLeftChild(replacement);
        }
        else if(actualDeleted == parent.getRightChild()){
            parent.setRightChild(replacement);
        }
        if(replacement != null) {
            logger.trace("Transplanting node {} into position of {}", replacement.getValue(), actualDeleted.getValue());
            replacement.setParent(parent);
        }
    }

    private BSTNode findMin(BSTNode node) {
        if (node.getLeftChild() == null) {
            return node;
        }
        return findMin(node.getLeftChild());
    }

    public boolean contains(int v) {
        logger.debug("Checking if tree contains value: {}", v);
        if (root == null){
            logger.debug("Tree is empty, returning false");
            return false;
        }
        BSTNode node = root;

        while (node != null) {
            if (v == node.getValue()) {
                logger.debug("Value {} found in tree", v);
                return true;
            }
            else if (v < node.getValue()) {
                logger.trace("Value {} < {}, going left", v, node.getValue());
                node = node.getLeftChild();
            }
            else {
                logger.trace("Value {} > {}, going right", v, node.getValue());
                node = node.getRightChild();
            }
        }
        logger.debug("Value {} not found in tree", v);

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
