package pjatk.pro.event_organizer_app.common.util;

import com.google.common.collect.ImmutableList;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Set;

@UtilityClass
public class CollectionUtil {

    public <T> ImmutableList<T> emptyListIfNull(Set<T> set) {
        if (CollectionUtils.isEmpty(set)) {
            return ImmutableList.of();
        } else {
            return ImmutableList.copyOf(set);
        }
    }

    public <T> ImmutableList<T> emptyListIfNull(List<T> set) {
        if (CollectionUtils.isEmpty(set)) {
            return ImmutableList.of();
        } else {
            return ImmutableList.copyOf(set);
        }
    }
}
