package com.assignment2;

import java.util.ArrayList;
import java.util.List;

public class RBTree implements TreeInterface {
    RBNode root;
    RBNode NIL;
    private int size;

    public RBTree() {
        NIL = new RBNode(-1, RBNode.Color.BLACK);
        NIL.setLeftChild(NIL);
        NIL.setRightChild(NIL);
        NIL.setParent(NIL);
        root = NIL;
        size = 0;
    }

    public boolean insert(int v) {
        if (root == NIL) {
            root = new RBNode(v, RBNode.Color.BLACK);
            root.setLeftChild(NIL);
            root.setRightChild(NIL);
            root.setParent(NIL);
            this.size++;
            return true;
        }

        RBNode current = root;
        RBNode parent = NIL;

        while (current != NIL) {
            parent = current;
            if (v == current.getValue()) {
                return false;
            } else if (v < current.getValue()) {
                current = current.getLeftChild();
            } else {
                current = current.getRightChild();
            }
        }

        current = new RBNode(v);
        current.setLeftChild(NIL);
        current.setRightChild(NIL);
        current.setParent(NIL);

        if (v < parent.getValue()) {
            parent.setLeftChild(current);
            current.setParent(parent);
        } else {
            parent.setRightChild(current);
            current.setParent(parent);
        }

        this.fixUpInInsertion(current);

        this.size++;
        return true;
    }

    private void fixUpInInsertion(RBNode current) {
        while (current.getParent().getColor() == RBNode.Color.RED) {
            // PARENT IS LEFT CHILD
            if (current.getParent() == current.getParent().getParent().getLeftChild()) {
                RBNode uncle = current.getParent().getParent().getRightChild();
                // CASE 0
                if (uncle.getColor() == RBNode.Color.RED) {
                    current.getParent().setColor(RBNode.Color.BLACK);
                    uncle.setColor(RBNode.Color.BLACK);
                    current.getParent().getParent().setColor(RBNode.Color.RED);
                    current = current.getParent().getParent();
                } else {
                    // CASE 1
                    if (current == current.getParent().getRightChild()) {
                        current = current.getParent();
                        this.LeftRotate(current);
                    }
                    // CASE 2
                    current.getParent().setColor(RBNode.Color.BLACK);
                    current.getParent().getParent().setColor(RBNode.Color.RED);
                    this.RightRotate(current.getParent().getParent());
                }
            }
            // PARENT IS RIGHT CHILD
            else {
                RBNode uncle = current.getParent().getParent().getLeftChild();
                // CASE 0
                if (uncle.getColor() == RBNode.Color.RED) {
                    current.getParent().setColor(RBNode.Color.BLACK);
                    uncle.setColor(RBNode.Color.BLACK);
                    current.getParent().getParent().setColor(RBNode.Color.RED);
                    current = current.getParent().getParent();
                } else {
                    // CASE 1
                    if (current == current.getParent().getLeftChild()) {
                        current = current.getParent();
                        this.RightRotate(current);
                    }
                    // CASE 2
                    current.getParent().setColor(RBNode.Color.BLACK);
                    current.getParent().getParent().setColor(RBNode.Color.RED);
                    this.LeftRotate(current.getParent().getParent());
                }
            }
        }
        this.root.setColor(RBNode.Color.BLACK);
    }

    private void LeftRotate(RBNode current) {
        RBNode newParent = current.getRightChild();
        current.setRightChild(newParent.getLeftChild());
        if (newParent.getLeftChild() != NIL) {
            newParent.getLeftChild().setParent(current);
        }
        newParent.setParent(current.getParent());
        if (current.getParent() == NIL) {
            this.root = newParent;
        } else if (current == current.getParent().getLeftChild()) {
            current.getParent().setLeftChild(newParent);
        } else if (current == current.getParent().getRightChild()) {
            current.getParent().setRightChild(newParent);
        }
        newParent.setLeftChild(current);
        current.setParent(newParent);
    }

    private void RightRotate(RBNode current) {
        RBNode newParent = current.getLeftChild();
        current.setLeftChild(newParent.getRightChild());
        if (newParent.getRightChild() != NIL) {
            newParent.getRightChild().setParent(current);
        }
        newParent.setParent(current.getParent());
        if (current.getParent() == NIL) {
            this.root = newParent;
        } else if (current == current.getParent().getRightChild()) {
            current.getParent().setRightChild(newParent);
        } else if (current == current.getParent().getLeftChild()) {
            current.getParent().setLeftChild(newParent);
        }
        newParent.setRightChild(current);
        current.setParent(newParent);
    }

    public boolean delete(int v) {
        if (root == NIL) {
            return false;
        }
        RBNode current = root;
        while (current != NIL) {

            if (v == current.getValue()) {
                break;
            }
            if (v < current.getValue()) {
                current = current.getLeftChild();
            } else {
                current = current.getRightChild();
            }

        }
        if (current == NIL) {
            return false;
        }

        RBNode actualDeleted = null;
        RBNode replacement = null;

        // CASE 1
        if (current.getLeftChild() == NIL && current.getRightChild() == NIL) {
            actualDeleted = current;
            replacement = NIL;
        }

        // CASE 2
        else if (current.getLeftChild() == NIL) {
            actualDeleted = current;
            replacement = current.getRightChild();
        } else if (current.getRightChild() == NIL) {
            actualDeleted = current;
            replacement = current.getLeftChild();
        }
        // CASE 3
        else {
            RBNode Successor = this.findMin(current.getRightChild());
            current.setValue(Successor.getValue());
            actualDeleted = Successor;
            replacement = Successor.getRightChild();
        }

        RBNode.Color deletedColor = actualDeleted.getColor();
        this.Transplant(replacement, actualDeleted);
        if (deletedColor == RBNode.Color.BLACK) {
            this.fixUpInDeletion(replacement);
        }

        this.size--;
        return true;
    }

    private void Transplant(RBNode replacement, RBNode actualDeleted) {
        if (actualDeleted.getParent() == NIL) {
            this.root = replacement;
            replacement.setParent(NIL);
            return;
        }

        RBNode parent = actualDeleted.getParent();

        if (actualDeleted == parent.getLeftChild()) {
            parent.setLeftChild(replacement);
        } else if (actualDeleted == parent.getRightChild()) {
            parent.setRightChild(replacement);
        }
        replacement.setParent(parent);
    }

    private void fixUpInDeletion(RBNode replacement) {
        while (replacement != root && replacement.getColor() == RBNode.Color.BLACK) {
            // REPLACEMENT IS LEFT CHILD
            if (replacement == replacement.getParent().getLeftChild()) {
                RBNode sibling = replacement.getParent().getRightChild();
                if (sibling.getColor() == RBNode.Color.RED) {
                    sibling.setColor(RBNode.Color.BLACK);
                    sibling.getParent().setColor(RBNode.Color.RED);
                    this.LeftRotate(replacement.getParent());
                    sibling = replacement.getParent().getRightChild();
                } else {
                    if (sibling.getLeftChild().getColor() == RBNode.Color.BLACK
                            && sibling.getRightChild().getColor() == RBNode.Color.BLACK) {
                        sibling.setColor(RBNode.Color.RED);
                        replacement = replacement.getParent();
                    }
                    else {
                        if (sibling.getLeftChild().getColor() == RBNode.Color.RED
                                && sibling.getRightChild().getColor() == RBNode.Color.BLACK) {
                            sibling.setColor(RBNode.Color.RED);
                            sibling.getLeftChild().setColor(RBNode.Color.BLACK);
                            this.RightRotate(sibling);
                            sibling = replacement.getParent().getRightChild();
                        }
                        sibling.setColor(replacement.getParent().getColor());
                        replacement.getParent().setColor(RBNode.Color.BLACK);
                        sibling.getRightChild().setColor(RBNode.Color.BLACK);
                        this.LeftRotate(replacement.getParent());
                        replacement = root;
                    }
                }
            }
            // REPLACEMENT IS RIGHT CHILD
            else {
                RBNode sibling = replacement.getParent().getLeftChild();
                if (sibling.getColor() == RBNode.Color.RED) {
                    sibling.setColor(RBNode.Color.BLACK);
                    sibling.getParent().setColor(RBNode.Color.RED);
                    this.RightRotate(replacement.getParent());
                    sibling = replacement.getParent().getLeftChild();
                } else {
                    if (sibling.getRightChild().getColor() == RBNode.Color.BLACK
                            && sibling.getLeftChild().getColor() == RBNode.Color.BLACK) {
                        sibling.setColor(RBNode.Color.RED);
                        replacement = replacement.getParent();
                    }
                    else {
                        if (sibling.getRightChild().getColor() == RBNode.Color.RED
                                && sibling.getLeftChild().getColor() == RBNode.Color.BLACK) {
                            sibling.setColor(RBNode.Color.RED);
                            sibling.getRightChild().setColor(RBNode.Color.BLACK);
                            this.LeftRotate(sibling);
                            sibling = replacement.getParent().getLeftChild();
                        }
                        sibling.setColor(replacement.getParent().getColor());
                        replacement.getParent().setColor(RBNode.Color.BLACK);
                        sibling.getLeftChild().setColor(RBNode.Color.BLACK);
                        this.RightRotate(replacement.getParent());
                        replacement = root;
                    }
                }
            }
        }
        replacement.setColor(RBNode.Color.BLACK);
    }

    public boolean contains(int v) {
        if (root == NIL) return false;
        RBNode node = root;

        while (node != NIL) {
            if (v == node.getValue()) return true;
            else if (v < node.getValue()) node = node.getLeftChild();
            else node = node.getRightChild();
        }
        return false;
    }

    private RBNode findMin(RBNode node) {
        if (node.getLeftChild() == NIL) {
            return node;
        }
        return findMin(node.getLeftChild());
    }

    private void inOrderHelper(RBNode node, List<Integer> list) {
        if (node == NIL) return;
        inOrderHelper(node.getLeftChild(), list);
        list.add(node.getValue());
        inOrderHelper(node.getRightChild(), list);
    }

    public List<Integer> inOrder() {
        List<Integer> list = new ArrayList<>();
        inOrderHelper(this.root, list);
        return list;
    }

    private int heightHelper(RBNode root) {
        if (root == NIL) return 0;

        return 1 + Math.max(heightHelper(root.getLeftChild()), heightHelper(root.getRightChild()));
    }

    public int height() {
        return heightHelper(root);
    }

    public int size() {
        return this.size;
    }
}
