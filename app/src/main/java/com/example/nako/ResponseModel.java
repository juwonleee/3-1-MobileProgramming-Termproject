package com.example.nako;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseModel {
    @SerializedName("I2790")
    private I2790Data i2790Data;

    public I2790Data getI2790Data() {
        return i2790Data;
    }


    public class I2790Data {
        @SerializedName("total_count")
        private String totalCount;

        @SerializedName("row")
        private List<RowItem> row;

        @SerializedName("RESULT")
        private Result result;


        public List<RowItem> getRow() {
            return row;
        }
    }

    public class RowItem {

        //  번호
        @SerializedName("NUM")
        private String num;

        //  식품코드
        @SerializedName("FOOD_CD")
        private String food_cd;

        //  지역명
        @SerializedName("SAMPLING_REGION_NAME")
        private String region_name;

        //  채취월
        @SerializedName("SAMPLING_MONTH_NAME")
        private String month;

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
        private String year;

        //  제조사명
        @SerializedName("MAKER_NAME")
        private String maker_name;

        //  자료출처
        @SerializedName("SUB_REF_NAME")
        private String ref_name;

        //  총내용량
        @SerializedName("SERVING_SIZE")
        private String total_size;

        //  내용량 단위
        @SerializedName("SERVING_UNIT")
        private String size_unit;

        //  열량
        @SerializedName("NUTR_CONT1")
        private String kcal;

        //  탄
        @SerializedName("NUTR_CONT2")
        private String carb;

        //  단
        @SerializedName("NUTR_CONT3")
        private String protein;

        //  지
        @SerializedName("NUTR_CONT4")
        private String fat;

        //  당
        @SerializedName("NUTR_CONT5")
        private String sugar;

        //  나트륨
        @SerializedName("NUTR_CONT6")
        private String natrium;

        //  콜레스테롤
        @SerializedName("NUTR_CONT7")
        private String chole;

        //  포화지방산
        @SerializedName("NUTR_CONT8")
        private String s_fat_acid;

        //  트랜스지방
        @SerializedName("NUTR_CONT9")
        private String trans_fat;

        public String getKcal() {
            return kcal;
        }
        public String getTotalSize() {
            return total_size;
        }
        public String getFoodName() {
            return food_name;
        }
    }

    public class Result {
        @SerializedName("MSG")
        private String msg;

        @SerializedName("CODE")
        private String code;

        // Getter and setter methods
    }
}
