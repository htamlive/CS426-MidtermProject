package it.xabaras.android.recyclerview.swipedecorator;

import android.graphics.drawable.RotateDrawable;

public interface SwipeRecyclerViewContract {

    RotateDrawable iconTransformOnSwipeLeft(RotateDrawable icon, float recyclerViewSwipeDecorator);

    RotateDrawable iconTransformOnSwipeRight(RotateDrawable icon, float recyclerViewSwipeDecorator);

    boolean enableAdjacentCornerRadii();
}
