package com.assignment2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BSTTest {
    private BST bst;

    @BeforeEach
    void setUp() {
        bst = new BST();
    }

    @Test
    @DisplayName("Should insert nodes and maintain size")
    void testInsert() {
        assertTrue(bst.insert(1));
        assertTrue(bst.insert(2));
        assertTrue(bst.insert(3));
        assertTrue(bst.insert(4));
        assertTrue(bst.insert(5));
        assertTrue(bst.insert(6));
        assertFalse(bst.insert(6) , "NO DUBS");
        assertFalse(bst.insert(5) , "NO DUBS");
        assertFalse(bst.insert(4) , "NO DUBS");
        assertFalse(bst.insert(3) , "NO DUBS");
        assertFalse(bst.insert(2) , "NO DUBS");
        assertFalse(bst.insert(1) , "NO DUBS");


        assertEquals(6, bst.size());
    }

    @Test
    @DisplayName("Should find values using contains")
    void testContains() {
        bst.insert(1);
        bst.insert(2);
        bst.insert(3);
        bst.insert(4);
        bst.insert(5);
        bst.insert(10);
        bst.insert(20);
        bst.insert(30);
        bst.insert(40);
        bst.insert(50);

        assertTrue(bst.contains(20));
        assertTrue(bst.contains(10));
        assertTrue(bst.contains(30));
        assertTrue(bst.contains(40));
        assertTrue(bst.contains(50));
        assertTrue(bst.contains(1));
        assertTrue(bst.contains(2));
        assertTrue(bst.contains(3));
        assertTrue(bst.contains(4));
        assertTrue(bst.contains(5));


        assertFalse(bst.contains(7));
        assertFalse(bst.contains(8));
        assertFalse(bst.contains(9));
        assertFalse(bst.contains(11));
        assertFalse(bst.contains(12));
        assertFalse(bst.contains(13));
        assertFalse(bst.contains(101));
        assertFalse(bst.contains(102));
        assertFalse(bst.contains(103));
    }

    @Test
    @DisplayName("Should handle deletion cases")
    void testDelete() {

        int[] values = {20, 10, 30, 5, 15};
        for (int v : values) bst.insert(v);

        // DELETE LEAF -> HAS NO CHILDREN
        assertTrue(bst.delete(5));
        assertFalse(bst.contains(5));
        assertEquals(4, bst.size());

        // DELETE NODE WITH ONLY ONE CHILD
        bst.insert(25);
        assertTrue(bst.delete(30)); // 30 has left child 25
        assertTrue(bst.contains(25));

        // DELETE NODE WITH TWO CHILDREN
        assertTrue(bst.delete(10));
        assertFalse(bst.contains(10));

        // VERIFY IN-ORDER
        List<Integer> sorted = bst.inOrder();
        assertEquals(List.of(15, 20, 25), sorted);
    }

    @Test
    @DisplayName("Should return sorted list from in-order traversal")
    void testInOrder() {
        int[] values = {50, 30, 70, 20, 40, 60, 80 , 10 , 100 , 1};
        for (int v : values) bst.insert(v);

        List<Integer> expected = List.of(1, 10 ,20, 30, 40, 50, 60, 70, 80 , 100);
        assertEquals(expected, bst.inOrder());
    }
}