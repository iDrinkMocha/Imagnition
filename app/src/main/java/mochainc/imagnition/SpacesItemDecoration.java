package mochainc.imagnition;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private int gridspan;
    private boolean haveHeader;

    public SpacesItemDecoration(int space, int gridspan, boolean haveHeader) {
        this.space = space;
        this.gridspan = gridspan;
        this.haveHeader = haveHeader;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);
        /*if(!haveHeader) {
            if (gridspan > 2) {
                if ((position + 1) % gridspan == 0) {
                    outRect.bottom = space;
                } else {
                    outRect.right = space;
                    outRect.bottom = space;
                }
            } else if (gridspan == 2) {
                if (position % gridspan == 0) {
                    outRect.right = space;
                    outRect.bottom = space;
                } else {
                    outRect.bottom = space;
                }
            } else {
                outRect.bottom = space;
            }
        }
        else{
            if(position==0){
                outRect.bottom = space;
            }

            if (gridspan > 2) {
                if (position % gridspan == 0) {
                    outRect.bottom = space;
                } else {
                    outRect.right = space;
                    outRect.bottom = space;
                }
            } else if (gridspan == 2) {
                if (position % gridspan != 0) {
                    outRect.right = space;
                    outRect.bottom = space;
                } else {
                    outRect.bottom = space;
                }
            } else {
                outRect.bottom = space;
            }
        }*/


        /*// Add top margin only for the first item to avoid double space between items
        if (position<=(gridspan-1)) {
            outRect.top = space;
        } else {
            outRect.top = 0;
        }*/

        if(haveHeader && position==0){
            outRect.top = 0;
        }

    }
}