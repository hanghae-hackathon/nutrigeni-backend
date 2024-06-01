package org.nutrigenie.technopark.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.bytebuddy.build.ToStringPlugin;

@Getter @Setter @ToString
public class UploadFoodDTO {

    private String foodName;
    private String imagePath;
    private int totCalories;


}
