package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.Multimap;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.io.PrintStream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-09-06
 */
public class Stats {

    public static <K, V> void dump(String title, Multimap<K, V> index, PrintStream stream) {
        DescriptiveStatistics statistics = new DescriptiveStatistics();
        index.keySet()
             .stream()
             .map(index::get)
             .forEach(axioms -> statistics.addValue(axioms.size()));
        stream.println(title);
        stream.print(statistics);
        stream.println("P75: " + statistics.getPercentile(75));
        stream.println("P80: " + statistics.getPercentile(80));
        stream.println("P85: " + statistics.getPercentile(85));
        stream.println("P90: " + statistics.getPercentile(90));
        stream.println("P95: " + statistics.getPercentile(95));
        stream.println("P99: " + statistics.getPercentile(99));
        stream.println("P100: " + statistics.getPercentile(100));
    }
}
