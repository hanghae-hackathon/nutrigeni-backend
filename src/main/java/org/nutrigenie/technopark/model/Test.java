package org.nutrigenie.technopark.model;

import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "test_tb")
@Setter @Getter @ToString
public class Test {

    @Id
    @Column(name = "test_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "test1")
    private String test1;

    @Column(name = "test2")
    private String test2;


}
