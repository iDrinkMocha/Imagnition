package mochainc.imagnition;

import java.util.ArrayList;

public class GalleryImageData {
    private ArrayList<String> imagePathArray;

    public GalleryImageData() {
    }

    public void setImagePathArray(ArrayList<String> imagePathArray) {
        this.imagePathArray = imagePathArray;
    }


    public ArrayList<String> getImagesPath() {
        return imagePathArray;
    }

}
