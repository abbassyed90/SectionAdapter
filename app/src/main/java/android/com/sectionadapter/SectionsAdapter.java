package android.com.sectionadapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * @author Abbas Syed
 * @packageName android.com.sectionadapter
 */
public class SectionsAdapter extends SectionAdapter {

    private LinkedList<Integer> itemCount = new LinkedList<>();

    public SectionsAdapter() {
        itemCount.add(5);
        itemCount.add(3);
        itemCount.add(3);
        itemCount.add(3);
        itemCount.add(3);
        itemCount.add(3);
        itemCount.add(3);
    }

    @Override
    public int getItemCount(int section) {
        return itemCount.get(section);
    }

    @Override
    public int getSectionCount() {
        return itemCount.size();
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int section) {
        HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
        holder.mHeaderTextView.setText(String.format("Section %s", section));
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder viewHolder, int section, int absolutePosition) {
        ItemViewHolder holder = (ItemViewHolder) viewHolder;
        holder.mItemTextView.setText(String.format("Item in section = %s and item in list position = %s", getRelativePositionInSection(absolutePosition), getRelativePositionInList(absolutePosition)));
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false));
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item, parent, false));
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView mHeaderTextView;

        HeaderViewHolder(View v) {
            super(v);
            mHeaderTextView = (TextView) v.findViewById(R.id.headerTextView);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView mItemTextView;

        ItemViewHolder(View v) {
            super(v);
            mItemTextView = (TextView) v.findViewById(R.id.itemTextView);
        }
    }

}
