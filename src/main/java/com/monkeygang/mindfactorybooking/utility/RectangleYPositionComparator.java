package com.monkeygang.mindfactorybooking.utility;

import javafx.scene.layout.StackPane;

import java.util.Comparator;

public class RectangleYPositionComparator implements Comparator<StackPane> {
    @Override
    public int compare(StackPane r1, StackPane r2) {
        return Double.compare(r1.getLayoutY(), r2.getLayoutY());
    }
}