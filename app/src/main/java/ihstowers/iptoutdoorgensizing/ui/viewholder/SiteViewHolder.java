package ihstowers.iptoutdoorgensizing.ui.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ihstowers.iptoutdoorgensizing.R;

public class SiteViewHolder extends RecyclerView.ViewHolder {

    public final View mView;
    @BindView(R.id.name)
    public TextView name;
    @BindView(R.id.ihsId)
    public TextView ihsId;
    @BindView(R.id.site_operator_id)
    public TextView site_operator_id;
    @BindView(R.id.site_location)
    public TextView site_location;
    @BindView(R.id.site_configuration)
    public TextView site_configuration;
    @BindView(R.id.site_classe)
    public TextView site_classe;
    @BindView(R.id.site_topology)
    public TextView site_topology;

    public SiteViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

        ButterKnife.bind(this, itemView);
    }

}