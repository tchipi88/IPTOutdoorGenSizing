package ihstowers.iptoutdoorgensizing.domain.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ihstowers.iptoutdoorgensizing.R;
import ihstowers.iptoutdoorgensizing.domain.Site;
import ihstowers.iptoutdoorgensizing.domain.dao.util.DatabaseHelper;

/**
 * Created by tchipi on 3/19/18.
 */

public class SiteAutoCompleteViewAdapter extends ArrayAdapter {

    private List<Site> sites;
    private Context mContext;
    private int itemLayout;
    private DatabaseHelper databaseHelper;


    private SiteAutoCompleteViewAdapter.ListFilter listFilter = new SiteAutoCompleteViewAdapter.ListFilter();

    public SiteAutoCompleteViewAdapter(Context context, int resource, List<Site> sites,DatabaseHelper databaseHelper) {
        super(context, resource, sites);
        this.sites = sites;
        mContext = context;
        itemLayout=resource;
        this.databaseHelper=databaseHelper;
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(itemLayout, parent, false);
        }

        TextView strName = (TextView) view.findViewById(android.R.id.text1);
        strName.setText(getItem(position).name);

        return view;
    }


    @Override
    public int getCount() {
        return sites.size();
    }

    @Override
    public Site getItem(int position) {
        return sites.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }

    public class ListFilter extends Filter {
        private Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = new ArrayList<String>();
                    results.count = 0;
                }
            } else {
                final String searchStrLowerCase = "%"+prefix.toString().toUpperCase()+"%";

                //Call to database to get matching records using room
                List<Site> matchValues =
                        null;
                try {
                    matchValues = databaseHelper.getSiteDao().queryBuilder().where().like("name",searchStrLowerCase).query();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                results.values = matchValues;
                results.count = matchValues.size();
            }

            return results;
        }



        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                sites = (ArrayList<Site>)results.values;
            } else {
                sites = null;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}
