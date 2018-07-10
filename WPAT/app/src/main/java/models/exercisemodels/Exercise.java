package models.exercisemodels;

import android.widget.ImageView;

import java.io.Serializable;

/**
 * Class for item model
 */
public class Exercise implements Serializable {
    String title;
    String description;
    ImageView image;

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
     * get desc
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * set desc
     * @param description description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * get the Image URL
     * @return image
     */
    public ImageView getImage() {
        return image;
    }

    /**
     * set the Image URL
     * @param image image
     */
    public void setImage(ImageView image) {
        this.image = image;
    }
}
