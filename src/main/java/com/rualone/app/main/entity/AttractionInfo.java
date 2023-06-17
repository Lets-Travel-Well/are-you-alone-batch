package com.rualone.app.main.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AttractionInfo {
    @Id
    private Integer contentId;
    private Integer contentTypeId;
    private String title;
    private String addr1;
    private String addr2;
    private String zipcode;
    private String tel;
    private String firstImage;
    private String firstImage2;
    @Column(name = "readcount")
    private Integer readCount;
    private Double latitude;
    private Double longitude;
    private String mlevel;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "sido_code")
    private Sido sido;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "gugun_code")
    private Gugun gugun;
    private String modifiedTime;

    public boolean update(AttractionInfo newAttractionInfo) {
        if(isSame(newAttractionInfo)) {
            return false;
        }

        contentTypeId = newAttractionInfo.contentTypeId;
        title = newAttractionInfo.title;
        addr1 = newAttractionInfo.addr1;
        addr2 = newAttractionInfo.addr2;
        zipcode = newAttractionInfo.zipcode;
        tel = newAttractionInfo.tel;
        firstImage = newAttractionInfo.firstImage;
        firstImage2 = newAttractionInfo.firstImage2;
        latitude = newAttractionInfo.latitude;
        longitude = newAttractionInfo.longitude;
        mlevel = newAttractionInfo.mlevel;
        sido = newAttractionInfo.sido;
        gugun = newAttractionInfo.gugun;
        modifiedTime = newAttractionInfo.modifiedTime;

        return true;
    }

    private boolean isSame(AttractionInfo attractionInfo) {
        return modifiedTime.equals(attractionInfo.modifiedTime);
    }
}