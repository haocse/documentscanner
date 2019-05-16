package com.haotran.documentscanner.widget.widget;
/**
 * <p/>
 * Author : TRONG SON<br>
 * Create Date : 11/23/2018.<br>
 */


import com.haotran.documentscanner.widget.adapter.BaseWheelAdapter;

import java.util.HashMap;
import java.util.List;

public interface IWheelView<T> {
    boolean LOOP = true;
    int WHEEL_SIZE = 3;
    boolean CLICKABLE = false;

    void setWheelClickable(boolean clickable);

    void setLoop(boolean loop);

    void setWheelSize(int wheelSize);

    void setWheelData(List<T> list);

    void setWheelAdapter(BaseWheelAdapter<T> adapter);

    void join(WheelView wheelView);

    void joinDatas(HashMap<String, List<T>> map);
}
