package nextstep.subway.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Line extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    public Line() {
    }

    public Line(final String name, final String color) {
        validateName(name);
        validateColor(color);

        this.name = name;
        this.color = color;
    }

    private void validateName(final String name) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException("노선의 이름은 필수 입니다.");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("노선의 이름은 1 자 이상 이어야 합니다.");
        }
    }

    private void validateColor(final String color) {
        if (Objects.isNull(color)) {
            throw new IllegalArgumentException("노선의 색상은 필수 입니다.");
        }
        if (color.isEmpty()) {
            throw new IllegalArgumentException("노선의 색상은 1 자 이상 이어야 합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public void changeName(final String name) {
        validateName(name);

        this.name = name;
    }

    public void changeColor(final String color) {
        validateColor(color);

        this.color = color;
    }
}
