package ihstowers.iptoutdoorgensizing.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by tchipi on 3/19/18.
 */


public class SiteReleve implements Serializable {
    @DatabaseField(generatedId = true)
    public Long id;

    @DatabaseField(canBeNull = false, foreign = true)
    public Site site;

    @DatabaseField
    public String dateReleve;

    @DatabaseField
    public Integer generatorSize;

    @DatabaseField
    public Integer rectifierNumber;

    @DatabaseField
    public Integer generatorSizeCalculated;

    @DatabaseField
    public Integer rectifierNumberCalculated;


}
