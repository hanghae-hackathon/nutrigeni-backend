package org.nutrigenie.technopark.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name ="food_detail_tb")
@Getter
@Setter
@ToString
public class FoodDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_detail_id")
    private long id;

    @Column(name = "analysis_name")
    private String analysisName;

    //칼로리
    @Column(name = "calories")
    private int calories;
    //탄수화물
    @Column(name = "carbs")
    private String carbs;
    //단백질
    @Column(name = "protein")
    private String protein;
    //지방
    @Column(name = "fat")
    private String fat;

    @ManyToOne
    @JoinColumn(name = "upload_food_id")
    private UploadFood uploadFood;

    @Column(name = "create_dt")
    private Date createDate;


}
