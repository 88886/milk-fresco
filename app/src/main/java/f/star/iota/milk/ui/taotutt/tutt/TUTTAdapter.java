package f.star.iota.milk.ui.taotutt.tutt;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import f.star.iota.milk.R;
import f.star.iota.milk.base.BaseAdapter;


public class TUTTAdapter extends BaseAdapter<TUTTViewHolder, TUTTBean> {

    @Override
    public TUTTViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TUTTViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pure_image, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((TUTTViewHolder) holder).bindView(mBeans);
    }
}
