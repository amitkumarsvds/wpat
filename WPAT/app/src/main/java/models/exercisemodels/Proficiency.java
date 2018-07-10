/**
 * Class for service json model
 */
package models.exercisemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Proficiency {

    @SerializedName("title")
    @Expose
    String title;
    @SerializedName("rows")
    @Expose
    private List<Row> rows = null;

    /**
     * get the title
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * set the title
     * @param title title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * get the rows
     * @return rows
     */
    public List<Row> getRows() {
        return rows;
    }

    /**
     * set the rows
     * @param rows rows
     */
    public void setRows(List<Row> rows) {
        this.rows = rows;
    }


    @Override
    public String toString() {
        return "Proficiency{" +
                "title='" + title + '\'' +
                ", rows=" + rows +
                '}';
    }
}
