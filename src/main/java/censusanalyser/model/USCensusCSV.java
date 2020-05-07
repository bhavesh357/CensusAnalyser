package censusanalyser.model;

import com.opencsv.bean.CsvBindByName;

public class USCensusCSV {

    @CsvBindByName(column = "State", required = true)
    public String state;

    @CsvBindByName(column = "StateId", required = true)
    public String stateId;

    @CsvBindByName(column = "Population", required = true)
    public int population;

    @CsvBindByName(column = "TotalArea", required = true)
    public double totalArea;

    @CsvBindByName(column = "PopulationDensity", required = true)
    public double populationDensity;
}
