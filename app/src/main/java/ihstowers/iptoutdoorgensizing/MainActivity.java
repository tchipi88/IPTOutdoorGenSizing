package ihstowers.iptoutdoorgensizing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import org.joda.time.LocalDate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ihstowers.iptoutdoorgensizing.domain.Site;
import ihstowers.iptoutdoorgensizing.domain.SiteReleve;
import ihstowers.iptoutdoorgensizing.domain.adapter.SiteAutoCompleteViewAdapter;
import ihstowers.iptoutdoorgensizing.domain.dao.util.DatabaseHelper;
import ihstowers.iptoutdoorgensizing.domain.enumeration.SiteConfiguration;
import ihstowers.iptoutdoorgensizing.domain.enumeration.SiteLocation;
import ihstowers.iptoutdoorgensizing.domain.enumeration.SiteTopology;

public class MainActivity extends AppCompatActivity {


    @Bind(R.id.site_operator_id)
    EditText site_operator_id;
    @Bind(R.id.site_id)
    EditText site_id;
    @Bind(R.id.site_name)
    AutoCompleteTextView site_name;
    @Bind(R.id.site_location)
    AutoCompleteTextView site_location;
    @Bind(R.id.site_configuration)
    AutoCompleteTextView site_configuration;
    @Bind(R.id.site_classe)
    AutoCompleteTextView site_classe;
    @Bind(R.id.site_topology)
    AutoCompleteTextView site_topology;

    @Bind(R.id.rectifier_module_size_w)
    EditText rectifier_module_size_w;
    @Bind(R.id.generator_capacity_kva)
    EditText generator_capacity_kva;


    @Bind(R.id.c)
    EditText c;
    @Bind(R.id.pu)
    EditText pu;
    @Bind(R.id.n)
    EditText n;
    @Bind(R.id.i)
    EditText i;
    @Bind(R.id.paircon)
    EditText paircon;


    SiteReleve siteReleve;

    @Bind(R.id.layout_aircon)
    LinearLayout layout_aircon;

    private DatabaseHelper databaseHelper = null;
    private Dao<Site, String> siteDao;
    private Dao<SiteReleve, Long> siteReleveDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getText(R.string.home));
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        ButterKnife.bind(this);

        // Initialize dataset, this data would usually come from a local content provider or
        databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        try {
            siteDao = databaseHelper.getSiteDao();
            siteReleveDao = databaseHelper.getSiteReleveDao();
            if (siteDao.queryForAll().isEmpty()) populateData();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayAdapter<SiteLocation> site_location_adapter = new ArrayAdapter<SiteLocation>
                (this, android.R.layout.select_dialog_item, SiteLocation.values());
        site_location.setThreshold(2);
        site_location.setAdapter(site_location_adapter);

        List<Site> sites = new ArrayList<>();
        SiteAutoCompleteViewAdapter siteAutoCompleteViewAdapter = new SiteAutoCompleteViewAdapter(this,
                android.R.layout.select_dialog_item, sites, databaseHelper);
        site_name.setThreshold(2);
        site_name.setAdapter(siteAutoCompleteViewAdapter);
        site_name.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Site site = (Site) adapterView.getItemAtPosition(position);
                setSiteView(site);

            }
        });

        ArrayAdapter<SiteConfiguration> site_configuration_adapter = new ArrayAdapter<SiteConfiguration>
                (this, android.R.layout.select_dialog_item, SiteConfiguration.values());
        site_configuration.setThreshold(1);
        site_configuration.setAdapter(site_configuration_adapter);
        site_configuration.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                SiteConfiguration siteConfiguration = (SiteConfiguration) adapterView.getItemAtPosition(position);
                if (SiteConfiguration.OUTDOOR.name().equalsIgnoreCase(siteConfiguration.name())) {
                    layout_aircon.setVisibility(View.GONE);
                    paircon.setText("0");
                } else {
                    layout_aircon.setVisibility(View.VISIBLE);
                }

            }

        });

        ArrayAdapter<SiteTopology> site_topology_adapter = new ArrayAdapter<SiteTopology>
                (this, android.R.layout.select_dialog_item, SiteTopology.values());
        site_topology.setThreshold(2);
        site_topology.setAdapter(site_topology_adapter);


        if (((String) getIntent().getSerializableExtra("i")) != null) {
            i.setText((String) getIntent().getSerializableExtra("i"));
            pu.setText((String) getIntent().getSerializableExtra("pu"));
            n.setText((String) getIntent().getSerializableExtra("n"));
            c.setText((String) getIntent().getSerializableExtra("c"));
            paircon.setText((String) getIntent().getSerializableExtra("paircon"));

            SiteReleve siteReleve = (SiteReleve) getIntent().getSerializableExtra("siteReleve");
            setSiteView(siteReleve.site);
            rectifier_module_size_w.setText(siteReleve.rectifierNumber + "");
            generator_capacity_kva.setText(siteReleve.generatorSize + "");

        }

    }

    public void setSiteView(Site site) {
        site_id.setText(site.ihs_id);
        site_name.setText(site.name);
        site_operator_id.setText(site.operator_id);
        site_configuration.setText(site.configuration);
        site_location.setText(site.location);
        site_topology.setText(site.topology);
        site_classe.setText(site.classe);

        if (SiteConfiguration.OUTDOOR.name().equalsIgnoreCase(site.configuration)) {
            layout_aircon.setVisibility(View.GONE);
            paircon.setText("0");
        } else {
            layout_aircon.setVisibility(View.VISIBLE);
        }
    }

    private void populateData() throws SQLException {
        siteDao.create(readData());
    }

    private List<Site> readData() {
        List<Site> sites = new ArrayList<>();
        InputStream is = getResources().openRawResource(R.raw.ihs);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = "";

        try {
            while ((line = reader.readLine()) != null) {
                // Split the line into different tokens (using the comma as a separator).
                String[] tokens = line.split(",");

                // Read the data and store it in the WellData POJO.
                Site site = new Site();
                site.operator_id = tokens[0];
                site.ihs_id = tokens[1];
                site.location = tokens[2].replace('"', ' ');
                ;
                site.name = tokens[3].toUpperCase().replace('"', ' ');
                site.configuration = tokens[4];
                site.classe = tokens[5];
                site.topology = tokens[6].replace('"', ' ');

                sites.add(site);

                Log.d("MainActivity", "Just Created " + site);
            }
        } catch (IOException e1) {
            Log.e("MainActivity", "Error " + line, e1);
            e1.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e1) {
            Log.e("MainActivity", "Error " + line, e1);
            e1.printStackTrace();
        }
        return sites;
    }

    @OnClick(R.id.calculate)
    public void save(View view) {
        if (!validate()) {
            return;
        }
        Intent intent = new Intent(this, ResultActivity.class);

        siteReleve = new SiteReleve();

        Site site = new Site();
        site.ihs_id = site_id.getText().toString();
        site.operator_id = site_operator_id.getText().toString();
        site.name = site_name.getText().toString();
        site.location = (site_location.getText().toString());
        site.configuration = (site_configuration.getText().toString());
        site.classe = (site_classe.getText().toString());
        site.topology = (site_topology.getText().toString());

        siteReleve.site = site;
        siteReleve.dateReleve = LocalDate.now().toString();


        Integer iInt = Integer.parseInt(i.getText().toString());
        Integer pairconInt = Integer.parseInt(paircon.getText().toString());

        //constantes
        float k1 = 0.1f;
        float k2 = 54f;

        //No Charge
        //S= [K2.(Iload)+Paircon]/600
        Float s_nocharge = ((k2 * iInt) + pairconInt) / 600;
        intent.putExtra("s_nocharge", Math.round(s_nocharge) + "");
        //Charge
        Integer cInt = Integer.parseInt(c.getText().toString());
        Integer nInt = Integer.parseInt(n.getText().toString());
        float k3 = 0.7f;

        Float Ich = k1 * cInt * nInt;
        //S= [K2.(Iload+K1.C.n)+Paircon]/600
        Float s = (k2 * (iInt + Ich) + pairconInt) / 600;
        //n'= (K2.(Iload+K1.C.n))/Pu
        Float nn = (k2 * (iInt + Ich)) / Integer.parseInt(pu.getText().toString());

        //T= (C.n/ILoad).K3
        Float t = cInt * nInt / iInt * k3;

        int Ichint = Math.round(Ich);
           /* int lastDigitIch = Ichint % 10;

            if (lastDigitIch >= 6 && lastDigitIch <= 9) Ichint = Ichint + (10 - lastDigitIch);
            if (lastDigitIch >= 1 && lastDigitIch <= 4) Ichint = Ichint + (5 - lastDigitIch);*/

        int sInt = Math.round(s);

        intent.putExtra("s", sInt + "");
        intent.putExtra("Ich", Ichint + "");
        intent.putExtra("nn", (Math.round(nn) + 1) + "");
        intent.putExtra("t", (Math.round(t)) + "");


        siteReleve.generatorSizeCalculated = sInt;
        siteReleve.rectifierNumberCalculated = (Math.round(nn) + 1);


        //inputs
        intent.putExtra("c", c.getText().toString());
        intent.putExtra("i", i.getText().toString());
        intent.putExtra("pu", pu.getText().toString());
        intent.putExtra("n", n.getText().toString());
        intent.putExtra("paircon", paircon.getText().toString());

        siteReleve.rectifierNumber = Integer.parseInt(rectifier_module_size_w.getText().toString());
        siteReleve.generatorSize = Integer.parseInt(generator_capacity_kva.getText().toString());

        intent.putExtra("gen_power_limitation",(siteReleve.generatorSize*0.75*0.8)+"");

        try {
            siteDao.createOrUpdate(site);
            siteReleve = siteReleveDao.createIfNotExists(siteReleve);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        intent.putExtra("siteReleve", siteReleve);

        startActivity(intent);
        finish();

    }

    public boolean validate() {
        boolean valid = true;

        if (rectifier_module_size_w.getText().toString().isEmpty()) {
            rectifier_module_size_w.setError(getResources().getText(R.string.error_field_required));
            valid = false;
        }
        if (generator_capacity_kva.getText().toString().isEmpty()) {
            generator_capacity_kva.setError(getResources().getText(R.string.error_field_required));
            valid = false;
        }


        if (site_id.getText().toString().isEmpty()) {
            site_id.setError(getResources().getText(R.string.error_field_required));
            valid = false;
        }
        if (site_name.getText().toString().isEmpty()) {
            site_name.setError(getResources().getText(R.string.error_field_required));
            valid = false;
        }
        if (site_location.getText().toString().isEmpty()) {
            site_location.setError(getResources().getText(R.string.error_field_required));
            valid = false;
        }
        if (site_configuration.getText().toString().isEmpty()) {
            site_configuration.setError(getResources().getText(R.string.error_field_required));
            valid = false;
        }
        if (site_classe.getText().toString().isEmpty()) {
            site_classe.setError(getResources().getText(R.string.error_field_required));
            valid = false;
        }
        if (site_topology.getText().toString().isEmpty()) {
            site_topology.setError(getResources().getText(R.string.error_field_required));
            valid = false;
        }


        if (c.getText().toString().isEmpty()) {
            c.setError(getResources().getText(R.string.error_field_required));
            valid = false;
        }
        if (n.getText().toString().isEmpty()) {
            n.setError(getResources().getText(R.string.error_field_required));
            valid = false;
        }
        if (pu.getText().toString().isEmpty()) {
            pu.setError(getResources().getText(R.string.error_field_required));
            valid = false;
        }
        if (i.getText().toString().isEmpty()) {
            i.setError(getResources().getText(R.string.error_field_required));
            valid = false;
        }
        if (paircon.getText().toString().isEmpty()) {
            paircon.setError(getResources().getText(R.string.error_field_required));
            valid = false;
        }
        return valid;
    }
}
