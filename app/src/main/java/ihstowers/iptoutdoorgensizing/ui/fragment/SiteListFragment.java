package ihstowers.iptoutdoorgensizing.ui.fragment;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ihstowers.iptoutdoorgensizing.R;
import ihstowers.iptoutdoorgensizing.domain.Site;
import ihstowers.iptoutdoorgensizing.ui.viewmodel.SiteViewModel;

public class SiteListFragment extends ViewModelFragment<SiteViewModel> implements SearchView.OnQueryTextListener {

    protected RecyclerView recyclerView;
    protected View emptyView;
     SimpleItemRecyclerViewAdapter adapter;


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    protected Class<SiteViewModel> getViewModel() {
        return SiteViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_site_list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);

        recyclerView = view.findViewById(R.id.enquetes);
        emptyView = view.findViewById(R.id.emptyView);
        assert recyclerView != null;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new SimpleItemRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        viewModel.loadSites().observe(getViewLifecycleOwner(), sites -> {
            adapter.addAll(sites);
        });

        return view;
    }


    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        Context context;
        private List<Site> mValues;


        public SimpleItemRecyclerViewAdapter() {
            mValues = new ArrayList<>();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            context = parent.getContext();
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.viewholder_site
                            , parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.ihsId.setText(holder.mItem.ihsId);
            holder.name.setText(holder.mItem.name);
            holder.site_operator_id.setText(holder.mItem.operatorId);
            holder.site_location.setText(holder.mItem.location);
            holder.site_classe.setText(holder.mItem.classe);
            holder.site_configuration.setText(holder.mItem.configuration);
            holder.site_topology.setText(holder.mItem.topology);
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewModel.setSite(holder.mItem);
                   //todo Navigation.findNavController(v).navigate(R.id.action_quizzFragment_to_questionFragment);

                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public void addAll(List<Site> sites) {
            mValues.addAll(sites);
            notifyDataSetChanged();

        }


        public class ViewHolder extends RecyclerView.ViewHolder {
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

            public Site mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                ButterKnife.bind(this, itemView);

            }

            @Override
            public String toString() {
                return super.toString() + " '" + name.getText() + "'";
            }
        }
    }


}
