package ihstowers.iptoutdoorgensizing;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.BinderThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.LocalDate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ihstowers.iptoutdoorgensizing.domain.Site;
import ihstowers.iptoutdoorgensizing.domain.SiteReleve;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static ihstowers.iptoutdoorgensizing.util.CSVUtil.followCVSformat;

public class ResultActivity extends AppCompatActivity  {

    private static final char DEFAULT_SEPARATOR = ',';

    final static String TAG = ResultActivity.class.getName();


    @Bind(R.id.c)
    TextView c;
    @Bind(R.id.pu)
    TextView pu;
    @Bind(R.id.n)
    TextView n;
    @Bind(R.id.i)
    TextView i;
    @Bind(R.id.nn)
    TextView nn;
    @Bind(R.id.s)
    TextView s;
    @Bind(R.id.s_nocharge)
    TextView s_nocharge;
    @Bind(R.id.t)
    TextView t;
    @Bind(R.id.t_)
    TextView t_;
    @Bind(R.id.Ich)
    TextView Ich;
    @Bind(R.id.Ich_)
    TextView Ich_;
    @Bind(R.id.paircon)
    TextView paircon;
    @Bind(R.id.gen_power_limitation)
    TextView gen_power_limitation;


    @Bind(R.id.battery_nocharge)
    SwitchCompat battery_nocharge;

    @Bind(R.id.site)
    TextView site_name;


    SiteReleve siteReleve;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getText(R.string.result));
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        ButterKnife.bind(this);

        siteReleve = (SiteReleve) getIntent().getSerializableExtra("siteReleve");

        site_name.setText(siteReleve.site.ihs_id + " " + siteReleve.site.name);

        i.setText((String) getIntent().getSerializableExtra("i"));
        pu.setText((String) getIntent().getSerializableExtra("pu"));
        n.setText((String) getIntent().getSerializableExtra("n"));
        c.setText((String) getIntent().getSerializableExtra("c"));
        t.setText((String) getIntent().getSerializableExtra("t"));

        paircon.setText((String) getIntent().getSerializableExtra("paircon"));

        battery_nocharge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                s_nocharge.setVisibility(checked ? View.VISIBLE : View.GONE);
                s.setVisibility(checked ? View.GONE : View.VISIBLE);
                battery_nocharge.setText(checked ? R.string.battery_without_charge : R.string.battery_charge);
            }
        });

        nn.setText((String) getIntent().getSerializableExtra("nn"));
        s.setText((String) getIntent().getSerializableExtra("s"));
        s_nocharge.setText((String) getIntent().getSerializableExtra("s_nocharge"));
        Ich.setText((String) getIntent().getSerializableExtra("Ich"));
        gen_power_limitation.setText((String)getIntent().getSerializableExtra("gen_power_limitation"));



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mail:
                generatefileResult(siteReleve);
                return true;
            case R.id.edit:
                Intent intent = new Intent(getApplication(), MainActivity.class);
                intent.putExtra("c", c.getText().toString());
                intent.putExtra("i", i.getText().toString());
                intent.putExtra("pu", pu.getText().toString());
                intent.putExtra("n", n.getText().toString());
                intent.putExtra("paircon", paircon.getText().toString());

                intent.putExtra("siteReleve", siteReleve);
                startActivity(intent);
                finish();
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


        sb.append(followCVSformat(siteReleve.site.operator_id));
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
        sb.append(followCVSformat(siteReleve.dateReleve));

        sb.append("\n");
        return sb.toString();


    }






    public void generatefileResult(SiteReleve siteReleve) {
            final String fileName = siteReleve.site.ihs_id + "_Releve_" + siteReleve.dateReleve + ".csv";

            String data = writeLine(siteReleve, DEFAULT_SEPARATOR, '"');
            try {
                File file = new File(getCacheDir(), fileName);

                FileOutputStream fileOutputStream = new FileOutputStream(file);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);

                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

                bufferedWriter.write(data);

                bufferedWriter.close();

                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"ngansop.arthur@gmail.com"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Site " + siteReleve.site.ihs_id + " Releve " + LocalDate.now().toString());
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

}
