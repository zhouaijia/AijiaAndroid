
package com.aijia.video.scene.data.page;

import java.util.List;


public class Page<T> {
    public static int TOTAL_INFINITY = -1;

    public List<T> list;
    public int index;
    public int total;

    public Page(List<T> list, int index, int total) {
        this.list = list;
        this.index = index;
        this.total = total;
    }
}
