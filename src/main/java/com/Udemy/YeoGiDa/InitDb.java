package com.Udemy.YeoGiDa;

import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.place.entity.Place;
import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

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
                    .nickname("ppirae1")
                    .imgUrl("imgUrl.com")
                    .role("ROLE_USER")
                    .build();

            Member member2 = Member.builder()
                    .email("test2@test2")
                    .password("$2a$10$quylBG2vlHIzcxb9zjqKZOlZ7S0ioHKxy2L85U6hXTjS1/RyOmk/O") //12345678
                    .nickname("ppirae2")
                    .imgUrl("imgUrl2.com")
                    .role("ROLE_USER")
                    .build();

            em.persist(member1);
            em.persist(member2);

            Trip trip1 = Trip.builder()
                    .region("제주도")
                    .title("공기맑은 제주")
                    .subTitle("제주도 혼저옵서예")
                    .member(member1)
                    .imgUrl("jejuUrl.com")
                    .build();

            Trip trip2 = Trip.builder()
                    .region("서울")
                    .title("강남")
                    .subTitle("강남의 맛집은 여기")
                    .member(member1)
                    .imgUrl("gangnamUrl.com")
                    .build();

            Trip trip3 = Trip.builder()
                    .region("서울")
                    .title("목동")
                    .subTitle("여기는 우리집")
                    .member(member2)
                    .imgUrl("mockdongUrl.com")
                    .build();

            em.persist(trip1);
            em.persist(trip2);
            em.persist(trip3);

            Place place1 = Place.builder()
                    .title("MyJuice")
                    .content("뷰 좋음")
                    .star(5D)
                    .address("서울시 종로구 34-1")
                    .trip(trip1)
                    .build();

            Place place2 = Place.builder()
                    .title("MyJuice2")
                    .content("뷰 좋음 22")
                    .star(4D)
                    .address("서울시 종로구 34-1")
                    .trip(trip1)
                    .build();

            Place place3 = Place.builder()
                    .title("MyJuice3")
                    .content("뷰 좋음 222")
                    .star(4D)
                    .address("서울시 종로구 34-1")
                    .trip(trip2)
                    .build();

            Place place4 = Place.builder()
                    .title("MyJuice4")
                    .content("뷰 좋음 444")
                    .star(4D)
                    .address("서울시 종로구 34-1")
                    .trip(trip2)
                    .build();

            em.persist(place1);
            em.persist(place2);
            em.persist(place3);
            em.persist(place4);
        }

    }

}
