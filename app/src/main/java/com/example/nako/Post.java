package com.example.nako;

import com.google.gson.annotations.SerializedName;


public class Post {
    //  번호
    @SerializedName("NUM")
    private int num;

    //  식품코드
    @SerializedName("FOOD_CD")
    private String food_cd;

    //  지역명
    @SerializedName("SAMPLING_REGION_NAME")
    private String region_name;

    //  채취월
    @SerializedName("SAMPLING_MONTH_NAME")
    private int month;

    //  지역코드
    @SerializedName("SAMPLING_REGION_CD")
    private String region_cd;

    //  식품군
    @SerializedName("GROUP_NAME")
    private String group_name;

    //  식품이름
    @SerializedName("DESC_KOR")
    private String food_name;

    //  조사년도
    @SerializedName("RESEARCH_YEAR")
    private int year;

    //  제조사명
    @SerializedName("MAKER_NAME")
    private String maker_name;

    //  자료출처
    @SerializedName("SUB_REF_NAME")
    private String ref_name;

    //  총내용량
    @SerializedName("SERVING_SIZE")
    private int total_size;

    //  내용량 단위
    @SerializedName("SERVING_UNIT")
    private int size_unit;

    //  열량
    @SerializedName("NUTR_CONT1")
    private int kcal;

    //  탄
    @SerializedName("NUTR_CONT2")
    private int carb;

    //  단
    @SerializedName("NUTR_CONT3")
    private int protein;

    //  지
    @SerializedName("NUTR_CONT4")
    private int fat;

    //  당
    @SerializedName("NUTR_CONT5")
    private int sugar;

    //  나트륨
    @SerializedName("NUTR_CONT6")
    private int natrium;

    //  콜레스테롤
    @SerializedName("NUTR_CONT7")
    private int chole;

    //  포화지방산
    @SerializedName("NUTR_CONT8")
    private int s_fat_acid;

    //  트랜스지방
    @SerializedName("NUTR_CONT9")
    private int trans_fat;

    public int getCarb() {
        return carb;
    }

    public int getChole() {
        return chole;
    }

    public int getFat() {
        return fat;
    }

    public int getKcal() {
        return kcal;
    }

    public int getMonth() {
        return month;
    }

    public int getNatrium() {
        return natrium;
    }

    public int getNum() {
        return num;
    }

    public int getProtein() {
        return protein;
    }

    public int getS_fat_acid() {
        return s_fat_acid;
    }

    public int getSize_unit() {
        return size_unit;
    }

    public int getSugar() {
        return sugar;
    }

    public int getTotal_size() {
        return total_size;
    }

    public int getTrans_fat() {
        return trans_fat;
    }

    public int getYear() {
        return year;
    }

    public String getFood_cd() {
        return food_cd;
    }

    public String getFood_name() {
        return food_name;
    }

    public String getGroup_name() {
        return group_name;
    }

    public String getMaker_name() {
        return maker_name;
    }

    public String getRef_name() {
        return ref_name;
    }

    public String getRegion_cd() {
        return region_cd;
    }

    public String getRegion_name() {
        return region_name;
    }
}
