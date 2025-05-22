package com.cob.ppa.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "excel_template")
@Getter
@Setter
public class ExcelTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String templateName;

    @Lob
    @Column(nullable = false)
    private byte[] fileContent;

    @Column(nullable = false)
    private String fileType;

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false)
    private Date uploadDate;

    @Column
    private String description;

    @PrePersist
    protected void onCreate() {
        uploadDate = new Date();
    }
}
