package android.com.sectionadapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.LinkedList;

/**
 * @author Abbas Syed
 * @packageName android.com.sectionadapter
 */
public abstract class SectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int RECYCLERVIEW_HEADER = 0;
    public static final int RECYCLERVIEW_ITEM = 1;

    private LinkedList<Integer> mHeaderPosition = new LinkedList<>();
    private LinkedList<Integer> mSectionCount = new LinkedList<>();

    public abstract int getItemCount(int section);

    public abstract int getSectionCount();

    public abstract void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int section);

    public abstract void onBindItemViewHolder(RecyclerView.ViewHolder viewHolder, int section, int absolutePosition);

    public abstract RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent);

    public abstract RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == RECYCLERVIEW_HEADER) {
            viewHolder = onCreateHeaderViewHolder(parent);
        } else {
            viewHolder = onCreateItemViewHolder(parent);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == RECYCLERVIEW_HEADER) {
            onBindHeaderViewHolder(holder, mHeaderPosition.indexOf(position));
        } else {
            onBindItemViewHolder(holder, getHeaderCountUpToPosition(position), position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (isHeader(position) ? RECYCLERVIEW_HEADER : RECYCLERVIEW_ITEM);
    }

    @Override
    public int getItemCount() {
        int headerCount = getSectionCount();
        int itemCount = 0;

        // clear lists
        mHeaderPosition.clear();
        mSectionCount.clear();

        for (int i = 0; i < headerCount; i++) {
            // get child item count for a section
            int childCount = getItemCount(i);

            // add header position
            mHeaderPosition.add(itemCount + mSectionCount.size());
            // add child count
            mSectionCount.add(childCount);

            // count all children
            itemCount += childCount;
        }

        return headerCount + itemCount;
    }

    GridLayoutManager createGridLayoutManager(Context context, int span) {
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(context, span);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (getItemViewType(position)) {
                    case SectionAdapter.RECYCLERVIEW_HEADER:
                        return gridLayoutManager.getSpanCount();
                    case SectionAdapter.RECYCLERVIEW_ITEM:
                        return 1;
                }
                return -1;
            }
        });
        return gridLayoutManager;
    }

    /**
     * Check if view at position
     * is a header view
     *
     * @param absolutePosition- absolute position in whole list
     * @return
     */
    private boolean isHeader(int absolutePosition) {
        return mHeaderPosition.contains(absolutePosition);
    }

    /**
     * Calculate relative position of an item
     * in a section.
     *
     * @param absolutePosition - absolute position in whole list
     * @return
     */
    int getRelativePositionInSection(int absolutePosition) {
        int relativePosition = 0;

        // get headers up to the point for absolute position
        int headerCount = getHeaderCountUpToPosition(absolutePosition);

        //absolutePosition
        //header.get(headerCount-1) = sectionPosition
        //        -1 = account for size
        relativePosition = absolutePosition - mHeaderPosition.get(headerCount - 1) - 1;

        return relativePosition;
    }

    /**
     * Calculate relative position in list.
     * Can be used for example with a arraylist, list, etc.
     *
     * @param absolutePosition - absolute position in whole list
     * @return
     */
    int getRelativePositionInList(int absolutePosition) {
        int relativePosition = 0;

        //get headers up to the point for absolute position
        int headerCount = getHeaderCountUpToPosition(absolutePosition);

        //remove all headers up to this point
        relativePosition = absolutePosition - headerCount;

        return relativePosition;
    }

    /**
     * Get the number of headers up until the
     * position
     *
     * @param position
     * @return
     */
    private int getHeaderCountUpToPosition(int position) {
        int headerCount = 0;

        for (int i = 0; i < mHeaderPosition.size(); i++) {
            int sectionPosition = mHeaderPosition.get(i);

            //if position is bigger than sectionPosition
            //then it has a header on top of it. Do this
            //until section position is bigger than position
            if (sectionPosition < position) {
                headerCount += 1;
            }
        }

        return headerCount;
    }
}