package com.example.ocr;

public class Equipment implements Comparable<Equipment>{
    public String name;//기자재 이름
    public String rental;//대여 가능, 불가능
    public String code;//품복 코드
    public String number;//자산 번호
    public String purchase_division;//구입 구분
    public String purchase_date;//대여한 날짜
    public String update_at;//대여해야하는 날짜
    public String standard;//물품 규격
    public String url;//이미지 URL
    public String rental_id;//렌탈 id
    public String day;//남은 날짜
    public int order;//순서 정렬 위한 변수

    @Override
    public int compareTo(Equipment e) {
        if(this.order < e.order) {
            return -1;
        }else return 1;
    }

    public int getViewtype() {
        return order;
    }
}
