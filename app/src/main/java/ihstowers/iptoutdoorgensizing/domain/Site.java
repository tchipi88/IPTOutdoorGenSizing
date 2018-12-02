package ihstowers.iptoutdoorgensizing.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import ihstowers.iptoutdoorgensizing.domain.enumeration.SiteClasse;
import ihstowers.iptoutdoorgensizing.domain.enumeration.SiteConfiguration;
import ihstowers.iptoutdoorgensizing.domain.enumeration.SiteLocation;
import ihstowers.iptoutdoorgensizing.domain.enumeration.SiteTopology;

/**
 * Created by tchipi on 3/19/18.
 */


public class Site  implements Serializable {
    @DatabaseField(id = true)
    public  String ihs_id;
    @DatabaseField
    public  String  operator_id;
    @DatabaseField
    public String name;
    @DatabaseField
    public String location;
    @DatabaseField
    public String configuration;
    @DatabaseField
    public String classe;
    @DatabaseField
    public String topology;
}
