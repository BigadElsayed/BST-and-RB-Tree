package com.assignment2;

import java.util.List;

public interface TreeInterface {
    boolean insert(int v);

    boolean delete(int v);

    boolean contains(int v);

    List<Integer> inOrder();

    int height();

    int size();
}
