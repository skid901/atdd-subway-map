package nextstep.subway.domain.repository;

import nextstep.subway.domain.entity.Line;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LineRepository extends JpaRepository<Line, Long> {

    List<Line> findByName(final String name);
}
