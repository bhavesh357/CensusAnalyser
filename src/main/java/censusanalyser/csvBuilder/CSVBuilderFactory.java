package censusanalyser.csvBuilder;

import censusanalyser.OpenCSVBuilder;
import censusanalyser.csvBuilder.ICSVBuilder;

public class CSVBuilderFactory {
    public static ICSVBuilder createCSVBuilder() {
        return new OpenCSVBuilder();
    }
}
