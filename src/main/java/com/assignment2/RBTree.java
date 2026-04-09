package com.assignment2;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RBTree implements TreeInterface {
    RBNode root;
    RBNode NIL;
    private int size;
    private static final Logger logger = LoggerFactory.getLogger(RBTree.class);

    private static final boolean VALIDATE = false;

    public RBTree() {
        NIL = new RBNode(-1, RBNode.Color.BLACK);
        NIL.setLeftChild(NIL);
        NIL.setRightChild(NIL);
        NIL.setParent(NIL);
        root = NIL;
        size = 0;
    }

    public boolean insert(int v) {
        logger.debug("Inserting into RBTree value : {} " , v);
        if (root == NIL) {
            logger.debug("Tree is empty , Creating new RBTree Root with value : {} ", v);
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

        logger.debug("Starting fixup for node {}", current.getValue());
        this.fixUpInInsertion(current);

        this.size++;

        if (VALIDATE && !Validator.validateRBTree(this)) {
            throw new IllegalStateException("RB-Tree invariant violated after insert!");
        }

        logger.debug("Insert completed successfully for value: {}", v);
        return true;
    }

    private void fixUpInInsertion(RBNode current) {
        while (current.getParent().getColor() == RBNode.Color.RED) {
            // PARENT IS LEFT CHILD
            if (current.getParent() == current.getParent().getParent().getLeftChild()) {
                RBNode uncle = current.getParent().getParent().getRightChild();
                logger.trace("Parent is LEFT child. Uncle={} ({})",
                        uncle.getValue(), uncle.getColor());
                // CASE 0
                if (uncle.getColor() == RBNode.Color.RED) {
                    logger.debug("Case 0 in Fixation");
                    current.getParent().setColor(RBNode.Color.BLACK);
                    uncle.setColor(RBNode.Color.BLACK);
                    current.getParent().getParent().setColor(RBNode.Color.RED);
                    current = current.getParent().getParent();
                } else {
                    // CASE 1
                    if (current == current.getParent().getRightChild()) {
                        logger.debug("Case 1 in Fixation");
                        current = current.getParent();
                        this.LeftRotate(current);
                    }
                    // CASE 2
                    logger.debug("Case 2 in Fixation");
                    current.getParent().setColor(RBNode.Color.BLACK);
                    current.getParent().getParent().setColor(RBNode.Color.RED);
                    this.RightRotate(current.getParent().getParent());
                }
            }
            // PARENT IS RIGHT CHILD
            else {
                RBNode uncle = current.getParent().getParent().getLeftChild();
                logger.trace("Parent is RIGHT child. Uncle={} ({})",
                        uncle.getValue(), uncle.getColor());
                // CASE 0
                if (uncle.getColor() == RBNode.Color.RED) {
                    logger.debug("Case 0 in Fixation");
                    current.getParent().setColor(RBNode.Color.BLACK);
                    uncle.setColor(RBNode.Color.BLACK);
                    current.getParent().getParent().setColor(RBNode.Color.RED);
                    current = current.getParent().getParent();
                } else {
                    // CASE 1
                    if (current == current.getParent().getLeftChild()) {
                        logger.debug("Case 1 in Fixation");
                        current = current.getParent();
                        this.RightRotate(current);
                    }
                    // CASE 2
                    logger.debug("Case 2 in Fixation");
                    current.getParent().setColor(RBNode.Color.BLACK);
                    current.getParent().getParent().setColor(RBNode.Color.RED);
                    this.LeftRotate(current.getParent().getParent());
                }
            }
        }
        logger.debug("Fixup complete. Ensuring root is BLACK");
        this.root.setColor(RBNode.Color.BLACK);
    }

    private void LeftRotate(RBNode current) {
        logger.debug("Left rotating node: {} (parent: {}, right child: {})",
                current.getValue(),
                current.getParent() == NIL ? "NIL" : current.getParent().getValue(),
                current.getRightChild().getValue());
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
        logger.trace("Left rotation completed: {} is now parent of {}",
                newParent.getValue(), current.getValue());
    }

    private void RightRotate(RBNode current) {
        logger.debug("Right rotating node: {} (parent: {}, left child: {})",
                current.getValue(),
                current.getParent() == NIL ? "NIL" : current.getParent().getValue(),
                current.getLeftChild().getValue());
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
        logger.trace("Right rotation completed: {} is now parent of {}",
                newParent.getValue(), current.getValue());
    }

    public boolean delete(int v) {
        logger.debug("Deleting node {} ", v);
        if (root == NIL) {
            logger.trace("Root is empty , returning false");
            return false;
        }
        RBNode current = root;
        while (current != NIL) {

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
        if (current == NIL) {
            return false;
        }

        RBNode actualDeleted = null;
        RBNode replacement = null;

        // CASE 1
        if (current.getLeftChild() == NIL && current.getRightChild() == NIL) {
            logger.debug("Node {} has no children", current.getValue());
            actualDeleted = current;
            replacement = NIL;
        }

        // CASE 2
        else if (current.getLeftChild() == NIL) {
            logger.debug("Node {} has Only right child", current.getValue());
            actualDeleted = current;
            replacement = current.getRightChild();
        } else if (current.getRightChild() == NIL) {
            logger.debug("Node {} has Only left child", current.getValue());
            actualDeleted = current;
            replacement = current.getLeftChild();
        }
        // CASE 3
        else {
            logger.debug("Node {} has 2 children", current.getValue());
            RBNode Successor = this.findMin(current.getRightChild());
            logger.debug("Successor is  {}", Successor.getValue());
            current.setValue(Successor.getValue());
            actualDeleted = Successor;
            replacement = Successor.getRightChild();
        }

        RBNode.Color deletedColor = actualDeleted.getColor();
        this.Transplant(replacement, actualDeleted);
        if (deletedColor == RBNode.Color.BLACK) {
            logger.debug("Starting Deletion fixup for node {}", current.getValue());
            this.fixUpInDeletion(replacement);
        }

        this.size--;
        if (VALIDATE && !Validator.validateRBTree(this)) {
            throw new IllegalStateException("RB-Tree invariant violated after delete!");
        }
        logger.debug("Delete completed successfully for value: {}", v);
        return true;
    }

    private void Transplant(RBNode replacement, RBNode actualDeleted) {
        if (actualDeleted.getParent() == NIL) {
            logger.debug("Transplanting replacement {} to root", replacement.getValue());
            this.root = replacement;
            replacement.setParent(NIL);
            return;
        }

        RBNode parent = actualDeleted.getParent();
        logger.trace("Transplanting node {} into position of {}", replacement.getValue(), actualDeleted.getValue());

        if (actualDeleted == parent.getLeftChild()) {
            parent.setLeftChild(replacement);
        } else if (actualDeleted == parent.getRightChild()) {
            parent.setRightChild(replacement);
        }
        if(replacement != NIL) {
            replacement.setParent(parent);
        }
    }

    private void fixUpInDeletion(RBNode replacement) {
        while (replacement != root && replacement.getColor() == RBNode.Color.BLACK) {
            // REPLACEMENT IS LEFT CHILD
            if (replacement == replacement.getParent().getLeftChild()) {
                RBNode sibling = replacement.getParent().getRightChild();
                logger.trace("Replacement is LEFT child. Sibling={} ({})",
                        sibling == NIL ? "NIL" : sibling.getValue(), sibling.getColor());
                if (sibling.getColor() == RBNode.Color.RED) {
                    logger.debug("Case 1 in Deletion Fixup (Red Sibling)");
                    sibling.setColor(RBNode.Color.BLACK);
                    sibling.getParent().setColor(RBNode.Color.RED);
                    this.LeftRotate(replacement.getParent());
                    sibling = replacement.getParent().getRightChild();
                } else {
                    if (sibling.getLeftChild().getColor() == RBNode.Color.BLACK
                            && sibling.getRightChild().getColor() == RBNode.Color.BLACK) {
                        logger.debug("Case 2 in Deletion Fixup (Black Sibling, Black Children)");
                        sibling.setColor(RBNode.Color.RED);
                        replacement = replacement.getParent();
                    }
                    else {
                        if (sibling.getLeftChild().getColor() == RBNode.Color.RED
                                && sibling.getRightChild().getColor() == RBNode.Color.BLACK) {
                            logger.debug("Case 3 in Deletion Fixup (Black Sibling, Left Child Red)");
                            sibling.setColor(RBNode.Color.RED);
                            sibling.getLeftChild().setColor(RBNode.Color.BLACK);
                            this.RightRotate(sibling);
                            sibling = replacement.getParent().getRightChild();
                        }
                        logger.debug("Case 4 in Deletion Fixup (Black Sibling, Right Child Red)");
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
                logger.trace("Replacement is RIGHT child. Sibling={} ({})",
                        sibling == NIL ? "NIL" : sibling.getValue(), sibling.getColor());
                if (sibling.getColor() == RBNode.Color.RED) {
                    logger.debug("Case 1 in Deletion Fixup (Red Sibling)");
                    sibling.setColor(RBNode.Color.BLACK);
                    sibling.getParent().setColor(RBNode.Color.RED);
                    this.RightRotate(replacement.getParent());
                    sibling = replacement.getParent().getLeftChild();
                } else {
                    if (sibling.getRightChild().getColor() == RBNode.Color.BLACK
                            && sibling.getLeftChild().getColor() == RBNode.Color.BLACK) {
                        logger.debug("Case 2 in Deletion Fixup (Black Sibling, Black Children)");
                        sibling.setColor(RBNode.Color.RED);
                        replacement = replacement.getParent();
                    }
                    else {
                        if (sibling.getRightChild().getColor() == RBNode.Color.RED
                                && sibling.getLeftChild().getColor() == RBNode.Color.BLACK) {
                            logger.debug("Case 3 in Deletion Fixup (Black Sibling, Right Child Red)");
                            sibling.setColor(RBNode.Color.RED);
                            sibling.getRightChild().setColor(RBNode.Color.BLACK);
                            this.LeftRotate(sibling);
                            sibling = replacement.getParent().getLeftChild();
                        }
                        logger.debug("Case 4 in Deletion Fixup (Black Sibling, Left Child Red)");
                        sibling.setColor(replacement.getParent().getColor());
                        replacement.getParent().setColor(RBNode.Color.BLACK);
                        sibling.getLeftChild().setColor(RBNode.Color.BLACK);
                        this.RightRotate(replacement.getParent());
                        replacement = root;
                    }
                }
            }
        }
        logger.debug("Deletion fixup complete. Ensuring replacement node is BLACK");
        replacement.setColor(RBNode.Color.BLACK);
    }

    public boolean contains(int v)
    {
        logger.debug("Checking if tree contains value: {}", v);
        if (root == NIL) {
            logger.debug("Tree is empty, returning false");
            return false;
        }
        RBNode node = root;

        while (node != NIL) {
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
