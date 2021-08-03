package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
//상속관계 SINGLE_TABLE 전략
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

//    비즈니스 로직(엔티티 안에 선언하는 것이 응집도가 높음/@Setter 사용하지 않고 비즈니스 로직으로 stockQuantity 증가)
    /**
     * 재고 증가
     * */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * 재고 감소
     * */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity = quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("Need more stock.");
        }
        this.stockQuantity = restStock;
    }
}
