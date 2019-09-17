package f.star.iota.milk.ui.ncjlh.nc;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import f.star.iota.milk.R;
import f.star.iota.milk.base.BaseAdapter;


public class NcAdapter extends BaseAdapter<NcViewHolder, NcBean> {

    @Override
    public NcViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NcViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pure_image, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((NcViewHolder) holder).bindView(mBeans);
    }
}
