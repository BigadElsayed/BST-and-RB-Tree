package com.assignment2;

import java.util.List;

public class Validator {
    public static boolean validateBst(BST tree) {
        return checkBSTProperty(tree.inOrder());
    }

    public static boolean validateRBTree(RBTree tree) {
        if (!checkBSTProperty(tree.inOrder())) return false;


        if (tree.root.getColor() != RBNode.Color.BLACK) return false;
        if (tree.NIL.getColor() != RBNode.Color.BLACK) return false;
        if (!checkRedProperty(tree.root, tree)) return false;
        if (calculateBlackHeight(tree.root, tree) == -1) return false;

        return true;
    }

    private static boolean checkBSTProperty(List<Integer> list) {
        if (list.isEmpty()) return true;

        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i) >= list.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkRedProperty(RBNode node, RBTree tree) {
        if (node == tree.NIL) return true;
        if (node.getColor() == RBNode.Color.RED) {
            if (node.getLeftChild().getColor() == RBNode.Color.RED ||
                    node.getRightChild().getColor() == RBNode.Color.RED) {
                return false; // red-red
            }
        }
        return checkRedProperty(node.getLeftChild(), tree) && checkRedProperty(node.getRightChild(), tree);
    }

    private static int calculateBlackHeight(RBNode node, RBTree tree) {
        if (node == tree.NIL) return 1;

        int leftHeight = calculateBlackHeight(node.getLeftChild(), tree);
        int rightHeight = calculateBlackHeight(node.getRightChild(), tree);

        if (leftHeight == -1 || rightHeight == -1) return -1;

        if (leftHeight != rightHeight) return -1;

        // ana lw wslt l hna ana kda kda mtmn an leftheight = rightheight
        if (node.getColor() == RBNode.Color.BLACK) {
            return leftHeight + 1;
        } else {
            return leftHeight;
        }
    }
}
