package com.Udemy.YeoGiDa;

import com.Udemy.YeoGiDa.domain.comment.entity.Comment;
import com.Udemy.YeoGiDa.domain.heart.entity.Heart;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.member.entity.MemberImg;
import com.Udemy.YeoGiDa.domain.place.entity.Place;
import com.Udemy.YeoGiDa.domain.place.entity.PlaceImg;
import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import com.Udemy.YeoGiDa.domain.trip.entity.TripImg;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.doInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void doInit1() {
            Member member1 = Member.builder()
                    .email("test@test")
                    .password("$2a$10$zWBZQWdpSIB77Aq4mJGJgexR/SbIRto5Bo8PR2577YUkEmPbbLN9O") //123456789
                    .nickname("하현상")
                    .role("ROLE_USER")
                    .build();

            Member member2 = Member.builder()
                    .email("test2@test2")
                    .password("$2a$10$quylBG2vlHIzcxb9zjqKZOlZ7S0ioHKxy2L85U6hXTjS1/RyOmk/O") //12345678
                    .nickname("호피폴라")
                    .role("ROLE_USER")
                    .build();

            Member member3 = Member.builder()
                    .email("test3@test3")
                    .password("$2a$10$quylBG2vlHIzcxb9zjqKZOlZ7S0ioHKxy2L85U6hXTjS1/RyOmk/O") //12345678
                    .nickname("뚜벅이")
                    .role("ROLE_USER")
                    .build();

            Member member4 = Member.builder()
                    .email("test4@test4")
                    .password("$2a$10$quylBG2vlHIzcxb9zjqKZOlZ7S0ioHKxy2L85U6hXTjS1/RyOmk/O") //12345678
                    .nickname("여행왕")
                    .role("ROLE_USER")
                    .build();

            Member member5 = Member.builder()
                    .email("test5@test5")
                    .password("$2a$10$quylBG2vlHIzcxb9zjqKZOlZ7S0ioHKxy2L85U6hXTjS1/RyOmk/O") //12345678
                    .nickname("해적왕")
                    .role("ROLE_USER")
                    .build();

            member1.setMemberImg(new MemberImg("https://yeogida-bucket.s3.ap-northeast-2.amazonaws.com/member1.jpeg", member1));
            member2.setMemberImg(new MemberImg("https://yeogida-bucket.s3.ap-northeast-2.amazonaws.com/member2.jpeg", member2));
            member3.setMemberImg(new MemberImg("https://yeogida-bucket.s3.ap-northeast-2.amazonaws.com/member3.jpg", member3));
            member4.setMemberImg(new MemberImg("https://yeogida-bucket.s3.ap-northeast-2.amazonaws.com/member4.jpg", member4));
            member5.setMemberImg(new MemberImg("https://yeogida-bucket.s3.ap-northeast-2.amazonaws.com/member5.jpg", member5));

            em.persist(member1);
            em.persist(member2);
            em.persist(member3);
            em.persist(member4);
            em.persist(member5);

            Trip trip1 = Trip.builder()
                    .region("제주도")
                    .title("공기맑은 제주")
                    .subTitle("제주도 혼저옵서예")
                    .member(member1)
                    .build();

            Trip trip2 = Trip.builder()
                    .region("서울")
                    .title("강남")
                    .subTitle("강남의 맛집은 여기")
                    .member(member1)
                    .build();

            Trip trip3 = Trip.builder()
                    .region("부산")
                    .title("부산")
                    .subTitle("광안리 해수욕장")
                    .member(member2)
                    .build();

            Trip trip4 = Trip.builder()
                    .region("경주")
                    .title("경주")
                    .subTitle("경주 한국사 투어")
                    .member(member2)
                    .build();

            Trip trip5 = Trip.builder()
                    .region("서울")
                    .title("광화문")
                    .subTitle("광화문 맛집")
                    .member(member3)
                    .build();

            trip1.setTripImg(new TripImg("https://yeogida-bucket.s3.ap-northeast-2.amazonaws.com/trip1.jpg", trip1));
            trip2.setTripImg(new TripImg("https://yeogida-bucket.s3.ap-northeast-2.amazonaws.com/trip2.jpg", trip2));
            trip3.setTripImg(new TripImg("https://yeogida-bucket.s3.ap-northeast-2.amazonaws.com/trip3.jpg", trip3));
            trip4.setTripImg(new TripImg("https://yeogida-bucket.s3.ap-northeast-2.amazonaws.com/trip4.jpg", trip4));
            trip5.setTripImg(new TripImg("https://yeogida-bucket.s3.ap-northeast-2.amazonaws.com/trip5.jpg", trip5));

            em.persist(trip1);
            em.persist(trip2);
            em.persist(trip3);
            em.persist(trip4);
            em.persist(trip5);

            Place place1 = Place.builder()
                    .title("MyJuice")
                    .content("뷰 좋음")
                    .star(5D)
                    .address("제주도 서귀포시 34-1")
                    .latitude(123.312D)
                    .longitude(213.2131D)
                    .tag("카페")
                    .trip(trip1)
                    .build();

            Place place2 = Place.builder()
                    .title("MyJuice2")
                    .content("뷰 좋음 22")
                    .star(4D)
                    .address("제주도 제주시 34-1")
                    .latitude(123.312D)
                    .longitude(213.2131D)
                    .tag("숙소")
                    .trip(trip1)
                    .build();

            Place place3 = Place.builder()
                    .title("SOju")
                    .content("안주 맛집")
                    .star(4D)
                    .address("서울시 강남구 34-1")
                    .latitude(123.312D)
                    .longitude(213.2131D)
                    .tag("술집")
                    .trip(trip2)
                    .build();

            Place place4 = Place.builder()
                    .title("MAckJu")
                    .content("안주맛집22")
                    .star(4D)
                    .address("서울시 강남구 34-1")
                    .latitude(123.312D)
                    .longitude(213.2131D)
                    .tag("술집")
                    .trip(trip2)
                    .build();

            Place place5 = Place.builder()
                    .title("광안리 바닷가")
                    .content("요트타기")
                    .star(4D)
                    .address("부산광역시 어디 34-1")
                    .latitude(123.312D)
                    .longitude(213.2131D)
                    .tag("명소")
                    .trip(trip3)
                    .build();

            Place place6 = Place.builder()
                    .title("한라산")
                    .content("등산하기")
                    .star(4D)
                    .address("제주도 어디 34-1")
                    .latitude(123.312D)
                    .longitude(213.2131D)
                    .tag("명소")
                    .trip(trip2)
                    .build();

            Place place7 = Place.builder()
                    .title("해운대 바닷가")
                    .content("수영하기")
                    .star(4D)
                    .address("부산광역시 어디 34-1")
                    .latitude(123.312D)
                    .longitude(213.2131D)
                    .tag("명소")
                    .trip(trip2)
                    .build();

            Place place8 = Place.builder()
                    .title("북한산")
                    .content("트래킹 하기")
                    .star(4D)
                    .address("서울 어디 34-1")
                    .latitude(123.312D)
                    .longitude(213.2131D)
                    .tag("명소")
                    .trip(trip2)
                    .build();

            Place place9 = Place.builder()
                    .title("강남")
                    .content("노래방 가기")
                    .star(4D)
                    .address("서울 강남 어디 34-1")
                    .latitude(123.312D)
                    .longitude(213.2131D)
                    .tag("명소")
                    .trip(trip2)
                    .build();

            Place place10 = Place.builder()
                    .title("웅진")
                    .content("공부하기")
                    .star(4D)
                    .address("서울 어디 34-1")
                    .latitude(123.312D)
                    .longitude(213.2131D)
                    .tag("명소")
                    .trip(trip2)
                    .build();

            Place place11 = Place.builder()
                    .title("서해바다")
                    .content("갯벌")
                    .star(4D)
                    .address("강릉 어디 34-1")
                    .latitude(123.312D)
                    .longitude(213.2131D)
                    .tag("명소")
                    .trip(trip2)
                    .build();

            Place place12 = Place.builder()
                    .title("울릉도")
                    .content("지키기")
                    .star(4D)
                    .address("울릉도 어디 34-1")
                    .latitude(123.312D)
                    .longitude(213.2131D)
                    .tag("명소")
                    .trip(trip2)
                    .build();

            List<PlaceImg> placeImgs1 = new ArrayList<>();
            placeImgs1.add(new PlaceImg("https://yeogida-bucket.s3.ap-northeast-2.amazonaws.com/place1.jpeg", place1));
            placeImgs1.add(new PlaceImg("https://yeogida-bucket.s3.ap-northeast-2.amazonaws.com/place2.jpeg", place1));
            place1.setPlaceImgs(placeImgs1);
            List<PlaceImg> placeImgs2 = new ArrayList<>();
            placeImgs2.add(new PlaceImg("https://yeogida-bucket.s3.ap-northeast-2.amazonaws.com/place3.jpeg", place2));
            placeImgs2.add(new PlaceImg("https://yeogida-bucket.s3.ap-northeast-2.amazonaws.com/place4.jpeg", place2));
            place2.setPlaceImgs(placeImgs1);
            List<PlaceImg> placeImgs3 = new ArrayList<>();
            placeImgs3.add(new PlaceImg("https://yeogida-bucket.s3.ap-northeast-2.amazonaws.com/place5.jpeg", place3));
            place3.setPlaceImgs(placeImgs3);
            List<PlaceImg> placeImgs4 = new ArrayList<>();
            placeImgs4.add(new PlaceImg("https://yeogida-bucket.s3.ap-northeast-2.amazonaws.com/default_place.png", place4));
            place4.setPlaceImgs(placeImgs4);
            List<PlaceImg> placeImgs5 = new ArrayList<>();
            placeImgs5.add(new PlaceImg("https://yeogida-bucket.s3.ap-northeast-2.amazonaws.com/default_place.png", place5));
            place5.setPlaceImgs(placeImgs5);

            List<PlaceImg> placeImgs6 = new ArrayList<>();
            placeImgs6.add(new PlaceImg("https://yeogida-bucket.s3.ap-northeast-2.amazonaws.com/place6.jpeg", place6));
            place6.setPlaceImgs(placeImgs6);
            List<PlaceImg> placeImgs7 = new ArrayList<>();
            placeImgs7.add(new PlaceImg("https://yeogida-bucket.s3.ap-northeast-2.amazonaws.com/place7.jpeg", place7));
            place7.setPlaceImgs(placeImgs7);
            List<PlaceImg> placeImgs8 = new ArrayList<>();
            placeImgs8.add(new PlaceImg("https://yeogida-bucket.s3.ap-northeast-2.amazonaws.com/place8.jpeg", place8));
            place8.setPlaceImgs(placeImgs8);
            List<PlaceImg> placeImgs9 = new ArrayList<>();
            placeImgs9.add(new PlaceImg("https://yeogida-bucket.s3.ap-northeast-2.amazonaws.com/place9.jpeg", place9));
            place9.setPlaceImgs(placeImgs9);
            List<PlaceImg> placeImgs10 = new ArrayList<>();
            placeImgs10.add(new PlaceImg("https://yeogida-bucket.s3.ap-northeast-2.amazonaws.com/place10.jpeg", place10));
            place10.setPlaceImgs(placeImgs10);
            List<PlaceImg> placeImgs11 = new ArrayList<>();
            placeImgs11.add(new PlaceImg("https://yeogida-bucket.s3.ap-northeast-2.amazonaws.com/place11.jpeg", place11));
            place11.setPlaceImgs(placeImgs11);
            List<PlaceImg> placeImgs12 = new ArrayList<>();
            placeImgs12.add(new PlaceImg("https://yeogida-bucket.s3.ap-northeast-2.amazonaws.com/place12.jpeg", place12));
            place12.setPlaceImgs(placeImgs12);

            em.persist(place1);
            em.persist(place2);
            em.persist(place3);
            em.persist(place4);
            em.persist(place5);
            em.persist(place6);
            em.persist(place7);
            em.persist(place8);
            em.persist(place9);
            em.persist(place10);
            em.persist(place11);
            em.persist(place12);

            Comment comment1 = new Comment(member1, place3, "여기 짱");
            Comment comment2 = new Comment(member3, place3, "여기 짱2");
            Comment comment3 = new Comment(member1, place5, "여기 짱3");
            Comment comment4 = new Comment(member4, place5, "여기 짱4");
            Comment comment5 = new Comment(member5, place5, "여기 짱5");

            em.persist(comment1);
            em.persist(comment2);
            em.persist(comment3);
            em.persist(comment4);
            em.persist(comment5);

            Heart heart1 = new Heart(member1, trip1);
            Heart heart2 = new Heart(member2, trip1);
            Heart heart3 = new Heart(member3, trip1);
            Heart heart4 = new Heart(member4, trip1);
            Heart heart5 = new Heart(member5, trip1);
            Heart heart6 = new Heart(member1, trip2);
            Heart heart7 = new Heart(member2, trip2);
            Heart heart8 = new Heart(member3, trip2);
            Heart heart9 = new Heart(member4, trip2);
            Heart heart10 = new Heart(member1, trip3);
            Heart heart11 = new Heart(member2, trip3);
            Heart heart12 = new Heart(member3, trip3);
            Heart heart13 = new Heart(member1, trip4);
            Heart heart14 = new Heart(member2, trip4);
            Heart heart15 = new Heart(member1, trip5);

            em.persist(heart1);
            em.persist(heart2);
            em.persist(heart3);
            em.persist(heart4);
            em.persist(heart5);
            em.persist(heart6);
            em.persist(heart7);
            em.persist(heart8);
            em.persist(heart9);
            em.persist(heart10);
            em.persist(heart11);
            em.persist(heart12);
            em.persist(heart13);
            em.persist(heart14);
            em.persist(heart15);
        }

    }

}