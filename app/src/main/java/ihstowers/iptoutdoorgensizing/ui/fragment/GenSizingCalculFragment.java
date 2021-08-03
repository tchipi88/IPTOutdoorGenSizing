package ihstowers.iptoutdoorgensizing.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


import org.joda.time.LocalDate;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ihstowers.iptoutdoorgensizing.MainActivity;
import ihstowers.iptoutdoorgensizing.R;
import ihstowers.iptoutdoorgensizing.dao.repository.SiteRepository;
import ihstowers.iptoutdoorgensizing.domain.Site;
import ihstowers.iptoutdoorgensizing.domain.SiteReleve;
import ihstowers.iptoutdoorgensizing.domain.adapter.SiteAutoCompleteViewAdapter;
import ihstowers.iptoutdoorgensizing.domain.enumeration.SiteConfiguration;
import ihstowers.iptoutdoorgensizing.domain.enumeration.SiteLocation;
import ihstowers.iptoutdoorgensizing.domain.enumeration.SiteTopology;
import ihstowers.iptoutdoorgensizing.ui.viewmodel.SiteViewModel;

public class GenSizingCalculFragment extends Fragment {


    @BindView(R.id.site_operator_id)
    EditText site_operator_id;
    @BindView(R.id.site_id)
    EditText site_id;
    @BindView(R.id.site_name)
    AutoCompleteTextView site_name;
    @BindView(R.id.site_location)
    AutoCompleteTextView site_location;
    @BindView(R.id.site_configuration)
    AutoCompleteTextView site_configuration;
    @BindView(R.id.site_classe)
    AutoCompleteTextView site_classe;
    @BindView(R.id.site_topology)
    AutoCompleteTextView site_topology;

    @BindView(R.id.rectifier_module_size_w)
    EditText rectifier_module_size_w;
    @BindView(R.id.generator_capacity_kva)
    EditText generator_capacity_kva;


    @BindView(R.id.c)
    EditText c;
    @BindView(R.id.pu)
    EditText pu;
    @BindView(R.id.n)
    EditText n;
    @BindView(R.id.i)
    EditText i;
    @BindView(R.id.paircon)
    EditText paircon;


    SiteReleve siteReleve;

    @BindView(R.id.layout_aircon)
    LinearLayout layout_aircon;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gen_sizing_calcul, container, false);
        ButterKnife.bind(this, view);

        ArrayAdapter<SiteLocation> site_location_adapter = new ArrayAdapter<SiteLocation>
                (getContext(), android.R.layout.select_dialog_item, SiteLocation.values());
        site_location.setThreshold(2);
        site_location.setAdapter(site_location_adapter);

       /* TODO SiteViewModel siteViewModel = ViewModelProviders.of(requireActivity()).get(SiteViewModel.class);
        List<Site> sites = new ArrayList<>();
        SiteAutoCompleteViewAdapter siteAutoCompleteViewAdapter = new SiteAutoCompleteViewAdapter(getContext(),
                android.R.layout.select_dialog_item, sites,  siteViewModel);*/
        site_name.setThreshold(2);
       // site_name.setAdapter(siteAutoCompleteViewAdapter);
        site_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Site site = (Site) adapterView.getItemAtPosition(position);
                setSiteView(site);

            }
        });

        ArrayAdapter<SiteConfiguration> site_configuration_adapter = new ArrayAdapter<SiteConfiguration>
                (getContext(), android.R.layout.select_dialog_item, SiteConfiguration.values());
        site_configuration.setThreshold(1);
        site_configuration.setAdapter(site_configuration_adapter);
        site_configuration.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                (getContext(), android.R.layout.select_dialog_item, SiteTopology.values());
        site_topology.setThreshold(2);
        site_topology.setAdapter(site_topology_adapter);


       /* if (((String) getIntent().getSerializableExtra("i")) != null) {
            i.setText((String) getIntent().getSerializableExtra("i"));
            pu.setText((String) getIntent().getSerializableExtra("pu"));
            n.setText((String) getIntent().getSerializableExtra("n"));
            c.setText((String) getIntent().getSerializableExtra("c"));
            paircon.setText((String) getIntent().getSerializableExtra("paircon"));

            SiteReleve siteReleve = (SiteReleve) getIntent().getSerializableExtra("siteReleve");
            setSiteView(siteReleve.site);
            rectifier_module_size_w.setText(siteReleve.rectifierNumber + "");
            generator_capacity_kva.setText(siteReleve.generatorSize + "");

        }*/

        return view;
    }


    @OnClick(R.id.calculate)
    public void save(View view) {
        if (!validate()) {
            return;
        }
        Intent intent = new Intent(getContext(), MainActivity.class);

        siteReleve = new SiteReleve();

        Site site = new Site();
        site.ihsId = site_id.getText().toString();
        site.operatorId = site_operator_id.getText().toString();
        site.name = site_name.getText().toString();
        site.location = (site_location.getText().toString());
        site.configuration = (site_configuration.getText().toString());
        site.classe = (site_classe.getText().toString());
        site.topology = (site_topology.getText().toString());

        siteReleve.siteId = site.ihsId;
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

           //TODO siteDao.createOrUpdate(site);
            //TODO siteReleve = siteReleveDao.createIfNotExists(siteReleve);

        intent.putExtra("siteReleve", siteReleve);

        //TODO mettre en fragement
        startActivity(intent);

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


    public void setSiteView(Site site) {
        site_id.setText(site.ihsId);
        site_name.setText(site.name);
        site_operator_id.setText(site.operatorId);
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

}
