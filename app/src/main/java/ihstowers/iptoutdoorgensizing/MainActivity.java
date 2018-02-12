package ihstowers.iptoutdoorgensizing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

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

    @Bind(R.id.battery_nocharge)
    SwitchCompat battery_nocharge;

    @Bind(R.id.layout_c)
    LinearLayout layout_c;
    @Bind(R.id.layout_n)
    LinearLayout layout_n;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getText(R.string.home));
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        ButterKnife.bind(this);

        battery_nocharge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    layout_c.setVisibility(View.GONE);
                    layout_n.setVisibility(View.GONE);
                    battery_nocharge.setText(getResources().getText(R.string.battery_without_charge));

                } else {
                    layout_c.setVisibility(View.VISIBLE);
                    layout_n.setVisibility(View.VISIBLE);
                    battery_nocharge.setText(getResources().getText(R.string.battery_charge));
                }
            }
        });


        if (((String) getIntent().getSerializableExtra("i")) != null) {
            i.setText((String) getIntent().getSerializableExtra("i"));
            pu.setText((String) getIntent().getSerializableExtra("pu"));
            n.setText((String) getIntent().getSerializableExtra("n"));
            c.setText((String) getIntent().getSerializableExtra("c"));
            paircon.setText((String) getIntent().getSerializableExtra("paircon"));
            battery_nocharge.setChecked((boolean) getIntent().getSerializableExtra("battery_nocharge"));
        }

    }

    @OnClick(R.id.calculate)
    public void save(View view) {
        if (!validate()) {
            return;
        }
        Intent intent = new Intent(this, ResultActivity.class);


        Integer iInt = Integer.parseInt(i.getText().toString());
        Integer pairconInt = Integer.parseInt(paircon.getText().toString());

        //constantes
        float k1 = 0.1f;
        float k2 = 54f;

        if (battery_nocharge.isChecked()) {
            //No Charge
            //S= [K2.(Iload)+Paircon]/600
            Float s = ((k2 * iInt) + pairconInt) / 600;
            intent.putExtra("s", Math.round(s) + "");
            //n'= (K2.(Iload))/Pu
            Float nn = (k2 * (iInt)) / Integer.parseInt(pu.getText().toString());
            intent.putExtra("nn", (Math.round(nn) + 1) + "");
        } else {
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
            Float t=cInt * nInt/iInt  *k3;

            int Ichint = Math.round(Ich);
           /* int lastDigitIch = Ichint % 10;

            if (lastDigitIch >= 6 && lastDigitIch <= 9) Ichint = Ichint + (10 - lastDigitIch);
            if (lastDigitIch >= 1 && lastDigitIch <= 4) Ichint = Ichint + (5 - lastDigitIch);*/

            int sInt = Math.round(s);
           /* if (s <= 9) sInt = 9;
            else {
                if (s <= 10) sInt = 10;
                else {
                    if (s <= 12) sInt = 12;
                    else {
                        if (s <= 13) sInt = 13;
                        else {
                            if (s <= 15) sInt = 15;
                            else {
                                if (s <= 16) sInt = 16;
                                else {
                                    if (s <= 17) sInt = 17;
                                    else {
                                        if (s <= 18) sInt = 18;
                                        else {
                                            if (s <= 20) sInt = 20;
                                            else {
                                                if (s <= 22) sInt = 22;
                                                else {
                                                    if (s <= 25) sInt = 25;
                                                    else {
                                                        if (s <= 30) sInt = 30;
                                                        else {
                                                            sInt = 33;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }*/


            intent.putExtra("s", sInt + "");
            intent.putExtra("Ich", Ichint + "");
            intent.putExtra("nn", (Math.round(nn) + 1) + "");
            intent.putExtra("t", (Math.round(t)) + "");
        }


        //inputs
        intent.putExtra("c", c.getText().toString());
        intent.putExtra("i", i.getText().toString());
        intent.putExtra("pu", pu.getText().toString());
        intent.putExtra("n", n.getText().toString());
        intent.putExtra("paircon", paircon.getText().toString());
        intent.putExtra("battery_nocharge", battery_nocharge.isChecked());

        startActivity(intent);
        finish();

    }

    public boolean validate() {
        boolean valid = true;

        if (!battery_nocharge.isChecked()) {
            if (c.getText().toString().isEmpty()) {
                c.setError(getResources().getText(R.string.error_field_required));
                valid = false;
            }
            if (n.getText().toString().isEmpty()) {
                n.setError(getResources().getText(R.string.error_field_required));
                valid = false;
            }
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
