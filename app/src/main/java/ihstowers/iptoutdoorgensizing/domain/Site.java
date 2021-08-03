package ihstowers.iptoutdoorgensizing.domain;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ihstowers.iptoutdoorgensizing.R;
import ihstowers.iptoutdoorgensizing.domain.enumeration.SiteClasse;
import ihstowers.iptoutdoorgensizing.domain.enumeration.SiteConfiguration;
import ihstowers.iptoutdoorgensizing.domain.enumeration.SiteLocation;
import ihstowers.iptoutdoorgensizing.domain.enumeration.SiteTopology;

/**
 * Created by tchipi on 3/19/18.
 */


@Entity
public class Site  implements Serializable {
    @PrimaryKey
    @NonNull
    public  String ihsId;

    @ColumnInfo
    public  String  operatorId;

    @ColumnInfo
    public String name;

    @ColumnInfo
    public String location;

    @ColumnInfo
    public String configuration;

    @ColumnInfo
    public String classe;

    @ColumnInfo
    public String topology;

    public static Site[] readData(Context context) {
        List<Site> sites = new ArrayList<>();
        InputStream is = context.getResources().openRawResource(R.raw.ihs);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = "";

        try {
            while ((line = reader.readLine()) != null) {
                // Split the line into different tokens (using the comma as a separator).
                String[] tokens = line.split(",");

                // Read the data and store it in the WellData POJO.
                Site site = new Site();
                site.operatorId = tokens[0];
                site.ihsId = tokens[1];
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
        return sites.toArray(new Site[0]);
    }
}
