package app.ewarehouse.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
public class CompositeKey {
    private Integer userId;
    private Integer hierarchyLevel;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompositeKey that = (CompositeKey) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(hierarchyLevel, that.hierarchyLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, hierarchyLevel);
    }
}
