package ch.unifr.digits;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

public class Measurements {

    private final AtomicInteger counter = new AtomicInteger(0);
    private final Map<String, List<Duration>> series = new HashMap<>();
    private final Map<String, List<Long>> manualSeries = new HashMap<>();
    private final Map<Integer, Instant> map = new HashMap<>();

    public int begin() {
        int i = counter.getAndIncrement();
        Instant now = Instant.now();
        map.put(i, now);
        return i;
    }

    public void finish(String seriesName, int measurementId) {
        Instant now = Instant.now();
        Instant then = map.remove(measurementId);
        if (then == null) return;
        Duration duration = Duration.between(then, now);
        series.compute(seriesName, (key, list) -> {
           if (list == null) list = new ArrayList<>();
           list.add(duration);
           return list;
        });
    }

    public void manualMeasurement(String seriesName, Long value) {
        manualSeries.compute(seriesName, (key, list) -> {
            if (list == null) list = new ArrayList<>();
            list.add(value);
            return list;
        });
    }

    public List<Duration> getSeries(String seriesName) {
        List<Duration> values = series.get(seriesName);
        List<Duration> list = new ArrayList<>();
        if (values == null) return list;
        return Collections.unmodifiableList(values);
    }

    public List<Long> getManualSeries(String seriesName) {
        List<Long> values = manualSeries.get(seriesName);
        List<Long> list = new ArrayList<>();
        if (values == null) return list;
        return Collections.unmodifiableList(values);
    }


}
