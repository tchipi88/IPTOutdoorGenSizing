package ihstowers.iptoutdoorgensizing.domain;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;


import java.io.Serializable;

/**
 * Created by tchipi on 3/19/18.
 */

@Entity(foreignKeys = @ForeignKey(entity = Site.class,
        parentColumns = "ihsId",
        childColumns = "siteId",
        onDelete = CASCADE), indices = {@Index("siteId")})
public class SiteReleve implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public Long id;

    public String siteId;

    public String dateReleve;

    public Integer generatorSize;

    public Integer rectifierNumber;

    public Integer generatorSizeCalculated;

    public Integer rectifierNumberCalculated;


}
