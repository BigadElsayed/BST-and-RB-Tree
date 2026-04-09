package com.assignment2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class RBTreeTest {
    private RBTree rbTree;

    @BeforeEach
    void setUp() {
        rbTree = new RBTree();
    }


    @Test
    @DisplayName("Should insert nodes and maintain size")
    void testInsert() {
        assertTrue(rbTree.insert(1));
        assertTrue(rbTree.insert(2));
        assertTrue(rbTree.insert(3));
        assertTrue(rbTree.insert(4));
        assertTrue(rbTree.insert(5));
        assertTrue(rbTree.insert(6));
        assertFalse(rbTree.insert(6) , "NO DUBS");
        assertFalse(rbTree.insert(5) , "NO DUBS");
        assertFalse(rbTree.insert(4) , "NO DUBS");
        assertFalse(rbTree.insert(3) , "NO DUBS");
        assertFalse(rbTree.insert(2) , "NO DUBS");
        assertFalse(rbTree.insert(1) , "NO DUBS");


        assertEquals(6, rbTree.size());
    }

    @Test
    @DisplayName("Should insert and trigger fixups (rotations/recoloring)")
    void testInsertAndBalance() {
        // INSERT SORTED -> LL IN BST BUT IN RBTREE HEIGHT MUST BE SMALL
        for (int i = 1; i <= 7; i++) {
            rbTree.insert(i);
        }

        assertEquals(7, rbTree.size());
        // HEIGHT MUST BE <= 4
        assertTrue(rbTree.height() <= 4, "Tree height is too large; balancing might be failing");
    }


    @Test
    @DisplayName("Should find values using contains")
    void testContains() {
        rbTree.insert(1);
        rbTree.insert(2);
        rbTree.insert(3);
        rbTree.insert(4);
        rbTree.insert(5);
        rbTree.insert(10);
        rbTree.insert(20);
        rbTree.insert(30);
        rbTree.insert(40);
        rbTree.insert(50);

        assertTrue(rbTree.contains(20));
        assertTrue(rbTree.contains(10));
        assertTrue(rbTree.contains(30));
        assertTrue(rbTree.contains(40));
        assertTrue(rbTree.contains(50));
        assertTrue(rbTree.contains(1));
        assertTrue(rbTree.contains(2));
        assertTrue(rbTree.contains(3));
        assertTrue(rbTree.contains(4));
        assertTrue(rbTree.contains(5));


        assertFalse(rbTree.contains(7));
        assertFalse(rbTree.contains(8));
        assertFalse(rbTree.contains(9));
        assertFalse(rbTree.contains(11));
        assertFalse(rbTree.contains(12));
        assertFalse(rbTree.contains(13));
        assertFalse(rbTree.contains(101));
        assertFalse(rbTree.contains(102));
        assertFalse(rbTree.contains(103));
    }

    @Test
    @DisplayName("Should handle deletion of red and black nodes")
    void testDelete() {
        int[] values = {10, 20, 30, 15, 25};
        for (int v : values) rbTree.insert(v);


        assertTrue(rbTree.delete(20));
        assertFalse(rbTree.contains(20));


        List<Integer> expected = List.of(10, 15, 25, 30);
        assertEquals(expected, rbTree.inOrder());
    }

    @Test
    @DisplayName("Should maintain size and handle root deletion")
    void testRootOperations() {
        rbTree.insert(100);
        assertTrue(rbTree.delete(100));
        assertEquals(0, rbTree.size());
        assertFalse(rbTree.contains(100));
    }

    @Test
    @DisplayName("Comprehensive random check")
    void testRandomFlow() {
        java.util.Random rand = new java.util.Random(42);
        java.util.Set<Integer> added = new java.util.HashSet<>();

        for (int i = 0; i < 100; i++) {
            int val = rand.nextInt(1000);
            if (rbTree.insert(val)) {
                added.add(val);
            }
        }

        assertEquals(added.size(), rbTree.size());

        int toDelete = added.iterator().next();
        assertTrue(rbTree.delete(toDelete));
        assertFalse(rbTree.contains(toDelete));
    }

    @Test
    @DisplayName("Should return sorted list from in-order traversal")
    void testInOrder() {
        int[] values = {50, 30, 70, 20, 40, 60, 80 , 10 , 100 , 1};
        for (int v : values) rbTree.insert(v);

        List<Integer> expected = List.of(1, 10 ,20, 30, 40, 50, 60, 70, 80 , 100);
        assertEquals(expected, rbTree.inOrder());
    }
}