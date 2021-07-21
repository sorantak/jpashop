package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

//    내장타입
    @Embedded
    private Address address;

//    연관관계의 거울(order 테이블에 있는 member 필드에 의해 매핑된 것)
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
