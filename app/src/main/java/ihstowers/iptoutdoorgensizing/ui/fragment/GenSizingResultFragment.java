package ihstowers.iptoutdoorgensizing.ui.fragment;

import static ihstowers.iptoutdoorgensizing.util.CSVUtil.followCVSformat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import org.joda.time.LocalDate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import butterknife.BindView;
import butterknife.ButterKnife;
import ihstowers.iptoutdoorgensizing.MainActivity;
import ihstowers.iptoutdoorgensizing.R;
import ihstowers.iptoutdoorgensizing.domain.SiteReleve;

public class GenSizingResultFragment extends Fragment {

    @BindView(R.id.c)
    TextView c;
    @BindView(R.id.pu)
    TextView pu;
    @BindView(R.id.n)
    TextView n;
    @BindView(R.id.i)
    TextView i;
    @BindView(R.id.nn)
    TextView nn;
    @BindView(R.id.s)
    TextView s;
    @BindView(R.id.s_nocharge)
    TextView s_nocharge;
    @BindView(R.id.t)
    TextView t;
    @BindView(R.id.t_)
    TextView t_;
    @BindView(R.id.Ich)
    TextView Ich;
    @BindView(R.id.Ich_)
    TextView Ich_;
    @BindView(R.id.paircon)
    TextView paircon;
    @BindView(R.id.gen_power_limitation)
    TextView gen_power_limitation;


    @BindView(R.id.battery_nocharge)
    SwitchCompat battery_nocharge;

    @BindView(R.id.site)
    TextView site_name;


    SiteReleve siteReleve;
    private static final char DEFAULT_SEPARATOR = ',';

    final static String TAG = GenSizingResultFragment.class.getName();





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gen_sizing_result, container, false);
        ButterKnife.bind(this, view);
//TODO
        /*siteReleve = (SiteReleve) getIntent().getSerializableExtra("siteReleve");

        site_name.setText(siteReleve.site.ihs_id + " " + siteReleve.site.name);*/

       /* i.setText((String) getIntent().getSerializableExtra("i"));
        pu.setText((String) getIntent().getSerializableExtra("pu"));
        n.setText((String) getIntent().getSerializableExtra("n"));
        c.setText((String) getIntent().getSerializableExtra("c"));
        t.setText((String) getIntent().getSerializableExtra("t"));

        paircon.setText((String) getIntent().getSerializableExtra("paircon"));
*/
        battery_nocharge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                s_nocharge.setVisibility(checked ? View.VISIBLE : View.GONE);
                s.setVisibility(checked ? View.GONE : View.VISIBLE);
                battery_nocharge.setText(checked ? R.string.battery_without_charge : R.string.battery_charge);
            }
        });
//TODO
        /*nn.setText((String) getIntent().getSerializableExtra("nn"));
        s.setText((String) getIntent().getSerializableExtra("s"));
        s_nocharge.setText((String) getIntent().getSerializableExtra("s_nocharge"));
        Ich.setText((String) getIntent().getSerializableExtra("Ich"));
        gen_power_limitation.setText((String)getIntent().getSerializableExtra("gen_power_limitation"));*/

        return view;
    }


    public void generatefileResult(SiteReleve siteReleve) {
        //todo final String fileName = siteReleve.site.ihs_id + "_Releve_" + siteReleve.dateReleve + ".csv";
        final String fileName="";
        String data = writeLine(siteReleve, DEFAULT_SEPARATOR, '"');
        try {
            File file = new File(getActivity().getCacheDir(), fileName);

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);

            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            bufferedWriter.write(data);

            bufferedWriter.close();

            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"ngansop.arthur@gmail.com"});
            //TODO emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Site " + siteReleve.site.ihs_id + " Releve " + LocalDate.now().toString());
            Uri URI = Uri.parse("file://" + file.getAbsolutePath());
            if (URI != null) {
                emailIntent.putExtra(Intent.EXTRA_STREAM, URI);
            }
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, writeLine(siteReleve, DEFAULT_SEPARATOR, '"'));
            startActivity(Intent.createChooser(emailIntent, "Sending email..."));

        } catch (FileNotFoundException ex) {
            Log.d(TAG, ex.getMessage());
        } catch (IOException ex) {
            Log.d(TAG, ex.getMessage());
        }


    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_result, menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mail:
                generatefileResult(siteReleve);
                return true;
            case R.id.edit:
//TODO
                /*Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("c", c.getText().toString());
                intent.putExtra("i", i.getText().toString());
                intent.putExtra("pu", pu.getText().toString());
                intent.putExtra("n", n.getText().toString());
                intent.putExtra("paircon", paircon.getText().toString());

                intent.putExtra("siteReleve", siteReleve);
                startActivity(intent);
                finish();*/
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public String writeLine(SiteReleve siteReleve, char separators, char customQuote) {


        StringBuilder sb = new StringBuilder();


        //header
        sb.append(customQuote).append("Site ID Operator").append(customQuote);
        sb.append(separators);
        sb.append(customQuote).append("IHS  ID").append(customQuote);
        sb.append(separators);
        sb.append(customQuote).append("Site Name").append(customQuote);
        sb.append(separators);
        sb.append(customQuote).append("State").append(customQuote);
        sb.append(separators);
        sb.append(customQuote).append("Site Configuration").append(customQuote);
        sb.append(separators);
        sb.append(customQuote).append("Class").append(customQuote);
        sb.append(separators);
        sb.append(customQuote).append("Final Topology").append(customQuote);
        sb.append(separators);
        sb.append(customQuote).append("Generator Size").append(customQuote);
        sb.append(separators);
        sb.append(customQuote).append("Generator Size Calculated").append(customQuote);
        sb.append(separators);
        sb.append(customQuote).append("Rectifier Number").append(customQuote);
        sb.append(separators);
        sb.append(customQuote).append("Rectifier Number Calculated").append(customQuote);
        sb.append(separators);
        sb.append(customQuote).append("Date Checked").append(customQuote);

        sb.append("\n");

//TODO
       /* sb.append(followCVSformat(siteReleve.site.operator_id));
        sb.append(separators);
        sb.append(followCVSformat(siteReleve.site.ihs_id));
        sb.append(separators);
        sb.append(customQuote).append(followCVSformat(siteReleve.site.name)).append(customQuote);
        sb.append(separators);
        sb.append(customQuote).append(followCVSformat(siteReleve.site.location)).append(customQuote);
        sb.append(separators);
        sb.append(followCVSformat(siteReleve.site.configuration));
        sb.append(separators);
        sb.append(followCVSformat(siteReleve.site.classe));
        sb.append(separators);
        sb.append(customQuote).append(followCVSformat(siteReleve.site.topology)).append(customQuote);
        sb.append(separators);
        sb.append(followCVSformat(siteReleve.generatorSize + ""));
        sb.append(separators);
        sb.append(followCVSformat(siteReleve.generatorSizeCalculated + ""));
        sb.append(separators);
        sb.append(followCVSformat(siteReleve.rectifierNumber + ""));
        sb.append(separators);
        sb.append(followCVSformat(siteReleve.rectifierNumberCalculated + ""));
        sb.append(separators);
        sb.append(followCVSformat(siteReleve.dateReleve));*/

        sb.append("\n");
        return sb.toString();


    }
}
