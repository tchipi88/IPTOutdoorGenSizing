package ihstowers.iptoutdoorgensizing;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ResultActivity extends AppCompatActivity {

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
    @Bind(R.id.t)
    TextView t;
    @Bind(R.id.Ich)
    TextView Ich;
    @Bind(R.id.paircon)
    TextView paircon;

    @Bind(R.id.IchCardview)
    CardView IchCardview;

    boolean battery_nocharge;

    @Bind(R.id.layout_c)
    LinearLayout layout_c;
    @Bind(R.id.layout_n)
    LinearLayout layout_n;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getText(R.string.result));
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        ButterKnife.bind(this);
        i.setText((String) getIntent().getSerializableExtra("i"));
        pu.setText((String) getIntent().getSerializableExtra("pu"));
        n.setText((String) getIntent().getSerializableExtra("n"));
        c.setText((String) getIntent().getSerializableExtra("c"));
        t.setText((String) getIntent().getSerializableExtra("t"));

        paircon.setText((String) getIntent().getSerializableExtra("paircon"));

        battery_nocharge = (boolean) getIntent().getSerializableExtra("battery_nocharge");
        if (battery_nocharge) {
            IchCardview.setVisibility(View.GONE);
            layout_c.setVisibility(View.GONE);
            layout_n.setVisibility(View.GONE);
        }

        nn.setText((String) getIntent().getSerializableExtra("nn"));
        s.setText((String) getIntent().getSerializableExtra("s"));
        Ich.setText((String) getIntent().getSerializableExtra("Ich"));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), MainActivity.class);
                intent.putExtra("c", c.getText().toString());
                intent.putExtra("i", i.getText().toString());
                intent.putExtra("pu", pu.getText().toString());
                intent.putExtra("n", n.getText().toString());
                intent.putExtra("paircon", paircon.getText().toString());
                intent.putExtra("battery_nocharge", battery_nocharge);
                startActivity(intent);
                finish();
            }
        });

    }

}
