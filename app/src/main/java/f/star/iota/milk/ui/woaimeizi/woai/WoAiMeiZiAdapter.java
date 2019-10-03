package f.star.iota.milk.ui.woaimeizi.woai;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import f.star.iota.milk.R;
import f.star.iota.milk.base.BaseAdapter;


public class WoAiMeiZiAdapter extends BaseAdapter<WoAiMeiZiViewHolder, WoAiMeiZiBean> {

    @Override
    public WoAiMeiZiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WoAiMeiZiViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_description, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((WoAiMeiZiViewHolder) holder).bindView(mBeans.get(position));
    }
}
