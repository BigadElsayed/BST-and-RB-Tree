package com.assignment2;

public class RBNode {
    public enum Color {
        RED,
        BLACK
    }

    private int value;
    private RBNode leftChild;
    private RBNode rightChild;
    private RBNode parent;
    private Color color;

    public RBNode(int value) {
        this.value = value;
        this.color = Color.RED;
    }

    public RBNode(int value, Color color) {
        this.value = value;
        this.color = color;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public RBNode getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(RBNode left) {
        this.leftChild = left;
    }

    public RBNode getRightChild() {
        return rightChild;
    }

    public void setRightChild(RBNode rightChild) {
        this.rightChild = rightChild;
    }

    public RBNode getParent() {
        return parent;
    }

    public void setParent(RBNode parent) {
        this.parent = parent;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
