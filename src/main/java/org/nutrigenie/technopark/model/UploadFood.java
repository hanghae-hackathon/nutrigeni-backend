package org.nutrigenie.technopark.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Entity
@Table(name ="upload_food_tb")
@Getter
@Setter
@ToString
public class UploadFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "upload_food_id")
    private long id;

    @Column(name = "create_dt")
    private Date createDate = new Date();

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "food_name")
    private String foodName;

    @Column(name = "tot_calories")
    private int totalCalories;

//    @OneToMany(mappedBy = "uploadFood")
//    private List<FoodDetail> foodDetail;
}
