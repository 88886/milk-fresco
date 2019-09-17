package f.star.iota.milk.ui.mmcools.mm;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import f.star.iota.milk.R;
import f.star.iota.milk.base.BaseAdapter;


public class MmAdapter extends BaseAdapter<MmViewHolder, MmBean> {

    @Override
    public MmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MmViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pure_image, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MmViewHolder) holder).bindView(mBeans);
    }
}
