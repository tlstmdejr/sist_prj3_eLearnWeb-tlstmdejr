package kr.co.sist.common.main;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommonMainDomain {

    // 카테고리
    private String catId, catName;

    // 강의
    private String lectId, title, instructor, shortint, category, thumbnail;
    private int price, usercount, reviewCount;
    private double rating;

}
